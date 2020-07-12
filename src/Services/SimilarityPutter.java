package Services;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimilarityPutter {
    public SimilarityPutter() {
    }

    public void putSimilarity(String adress, String firstFile,
                              String secondFile, float similarity , String token) throws Exception{
        String link = adress+ firstFile + "/" + secondFile;
        link = link. replaceAll(" ", "%20");
        URL url = new URL(link);
        HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
        connexion.setRequestMethod("POST");
        connexion.setRequestProperty("Content-Type", "application/json");
        connexion.setRequestProperty("Content-Type", "application/json; UTF-8");
        connexion.setDoInput(true);
        connexion.setDoOutput(true);
        connexion.setRequestProperty("Authorization","Bearer " + token );
        connexion.setConnectTimeout(5000);
        connexion.setReadTimeout(5000);

        OutputStream os = connexion.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(os);
        writer.write("{ \"value\": \""+ similarity+"\" }");

        writer.close();

        connexion.connect();

        if(connexion.getResponseCode() == 200){
            System.out.println( "\u001B[32m" + " -------------------- Similarity of :" + firstFile + " and "+
                    secondFile + " has been posted to the server with success --------------------"
                    + "\u001B[0m");
        }else {
            System.out.println("Message :" + connexion.getResponseMessage());
        }
    }
}
