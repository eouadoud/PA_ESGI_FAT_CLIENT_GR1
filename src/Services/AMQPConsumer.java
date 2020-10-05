package Services;

import com.rabbitmq.client.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Connection.PropertiesReader;
import Connection.ConnectionManager;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class AMQPConsumer {
    String AMQPUrl = "amqps://styxlnpf:wxDA71QSZ8Z60Blo6PtxpujjG9vE0p0N@antelope.rmq.cloudamqp.com/styxlnpf";
    public Channel initConnection() throws IOException, NoSuchAlgorithmException,
            KeyManagementException, URISyntaxException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(AMQPUrl);

        //Recommended settings
        factory.setRequestedHeartbeat(30);
        factory.setConnectionTimeout(0);

        Connection connection = factory.newConnection();

        return connection.createChannel();
    }
    public void consumeMessage(Channel channel) throws Exception {
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume("uploadQueue", true, consumer);
        TrackDownload trackDownload = new TrackDownload();
        MusicConverter converter = new MusicConverter();
        PropertiesReader pr = new PropertiesReader();
        Properties Cpr = pr.getConnecxionProps();
        String dLink = (String) Cpr.get("download_adress");
        ConnectionManager cnm = new ConnectionManager( (String) Cpr.get("login_adress"),
                (String) Cpr.get("login"), (String) Cpr.get("passwd") );
        String token = cnm.Connect();
        SimilarityPutter similarityPutter = new SimilarityPutter();
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            float simScore = 0;
            if (!message.isBlank() && !message.isEmpty()) {
                JSONObject music = new JSONObject(message);
                String fileLocation = trackDownload.runDownload(dLink + music.get("fileName"),
                        (String) music.get("musicTitle"), token);
                String convertedTrackPath = converter.convertTowav((String) music.get("musicTitle"), fileLocation);
                File floder = new File("./tracksdb/analysed/");
                File[] analysedFiles = floder.listFiles();
                if (analysedFiles.length == 0) {
                    System.out.println(" ---------- First music loaded, No similarity need to be done ---------- ");
                    Path mover = Files.move(Paths.get(convertedTrackPath),
                            Paths.get("./tracksdb/analysed/" +
                                    (String) music.get("musicTitle") + ".wav"));
                } else {
                    boolean trackExists = false;
                    for (int j = 0; j < analysedFiles.length; j++) {
                        if (analysedFiles[j].isFile()) {

                            SimilarityCalculator similarityCalculator = new SimilarityCalculator(convertedTrackPath
                                    , "./tracksdb/analysed/" + analysedFiles[j].getName());
                            simScore = similarityCalculator.getSimilarity();
                            System.out.println("Tracks : " + analysedFiles[j].getName().substring(0, analysedFiles[j].getName().length() - 4) + " -- and  --"
                                    + (String) music.get("musicTitle"));
                            System.out.println("  -------------------- Similarity: " + simScore + " -------------------- ");
                            similarityPutter.putSimilarity((String) Cpr.get("similarity_adress"), analysedFiles[j].getName().substring(0, analysedFiles[j].getName().length() - 4)
                                    , (String) music.get("musicTitle")
                                    , simScore, token);
                            if (simScore == 100) {
                                trackExists = true;
                                System.out.println(" ---------- Track allrady exists in database! removing... ---------- ");
                                File track = new File(convertedTrackPath);
                                similarityPutter.askToDelete((String) Cpr.get("musics_adress"), (int) music.get("musicId"), token);
                                if (track.exists()) {
                                    track.delete();
                                }
                                break;
                            }
                        }
                    }
                    if (!trackExists) {
                        System.out.println(" ---------- Similarities calculating done! moving to analysed directory... ---------- ");
                        Path mover = Files.move(Paths.get(convertedTrackPath),
                                Paths.get("./tracksdb/analysed/"
                                        + (String) music.get("musicTitle")
                                        + ".wav"));
                    }
                }
                System.gc();

            }
        }
    }
}
