package Services;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
    public void askToDelete(String adress, int musicId, String token) throws IOException {
        System.out.println("deleting music with id : "+ musicId);
        URL url = new URL(adress+musicId);
        HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
        connexion.setRequestMethod("DELETE");
        connexion.setConnectTimeout(1000);
        connexion.setReadTimeout(1000);
        connexion.setRequestProperty("Authorization","Bearer " + token );
        if(connexion.getResponseCode() == 200){
            System.out.println( "\u001B[32m" + " -------------------- SERVER :: file has been deleted with success --------------------"
                    + "\u001B[0m");
        }else{
            System.out.println(connexion.getResponseMessage() + connexion.getResponseCode() + " ---- " +
                    connexion.getContent());
        }
    }
}
