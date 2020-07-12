package Services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class TrackDownload {
    public TrackDownload() {
    }
    public String runDownload(String DownloadLink,String TrackName, String token ) throws IOException {
        System.out.println( " ------------- Downloading [[ "+TrackName+" ]] ------------- ");
        ReadableByteChannel chanel;
        HttpURLConnection connexion;
        try{
            URL url = new URL(DownloadLink);
            connexion = (HttpURLConnection) url.openConnection();
            connexion.setRequestMethod("GET");
            connexion.setRequestProperty("Authorization","Bearer " + token);
            //chanel = Channels.newChannel(url.openStream());
        }catch (IOException e){
            throw new IOException( e.getMessage());
        }
        InputStream inputStream = connexion.getInputStream();
        String fileLocation = "./tracksdb/tracks/"+ TrackName +".mp3" ;
        FileOutputStream dest = new FileOutputStream(fileLocation);

        int bytesRead = -1;
        byte[] buffer = new byte[4096];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            dest.write(buffer, 0, bytesRead);
        }

        dest.close();
        inputStream.close();

        return fileLocation;
    }
}
