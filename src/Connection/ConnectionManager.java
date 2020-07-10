package Connection;



import Exceptions.ConnectionFailedException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class ConnectionManager {

    String connectURL;
    String username;
    String password;

    public ConnectionManager(){

    }

    public ConnectionManager(String connectURL, String username, String password) {
        this.connectURL = connectURL;
        this.username = username;
        this.password = password;
    }

    public String Connect() throws Exception{
        System.out.println(" ------------- Connecting to server... -------------");
        String token = "";

        URL url = new URL(connectURL );
        HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
        connexion.setRequestMethod("POST");
        connexion.setRequestProperty("Content-Type", "application/json");
        connexion.setRequestProperty("Content-Type", "application/json; UTF-8");
        connexion.setDoInput(true);
        connexion.setDoOutput(true);

        connexion.setConnectTimeout(5000);
        connexion.setReadTimeout(5000);

        OutputStream os = connexion.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(os);
        writer.write("{ \"username\": \""+  username
                +"\", \"password\": \""+ password +"\" }");


        writer.close();

        connexion.connect();
        int responseCode = connexion.getResponseCode();
        System.out.println(" ------------- Login response code :" + connexion.getResponseCode() + "" +
                " -------------");
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("HTTP error code: " + responseCode);
        }

        InputStream stream = connexion.getInputStream();
        JSONObject res = null;
        if (stream != null) {
             res = readStream(stream, 500);
        }


        if(connexion.getResponseCode() == 200){
            System.out.println( "\u001B[32m" + " ------ Connexion success ! ------ " + "\u001B[0m" );
            token = (String) res.get("token");
            return token;
        }else{
            throw new ConnectionFailedException("Connexion error ! " +
                    "please check the provided properties and restart the client.");
        }

    }

    private static JSONObject readStream(InputStream stream, int maxReadSize)
            throws IOException, UnsupportedEncodingException, JSONException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] rawBuffer = new char[maxReadSize];
        int readSize;
        StringBuilder buffer = new StringBuilder();
        while (((readSize = reader.read(rawBuffer)) != -1) && maxReadSize > 0) {
            if (readSize > maxReadSize) {
                readSize = maxReadSize;
            }
            buffer.append(rawBuffer, 0, readSize);
            maxReadSize -= readSize;
        }
        return new JSONObject(buffer.toString());
    }

}
