package Services;

import Exceptions.LoadMusicsListFailedException;
import Models.Music;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

public class MusicsListLoader {
    private List<Music> musics;

    public List<Music> loadList( String loadLink, String token ) throws Exception{
        this.musics = new ArrayList<Music>();
        System.out.println(" ------------- Loading none analysed musics... -------------");

        //HttpHeaders  header = new HttpHeaders ();
        //header.add("Authorization", "Basic"+token);

        URL url = new URL(loadLink);
        HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
        connexion.setRequestMethod("GET");
        connexion.setConnectTimeout(1000);
        connexion.setReadTimeout(1000);
        connexion.setRequestProperty("Authorization","Bearer " + token );

        BufferedReader In = new BufferedReader(
                new InputStreamReader(connexion.getInputStream()));
        String inputLine;
        StringBuilder  content = new StringBuilder ();
        while ((inputLine = In.readLine()) != null) {
            content.append(inputLine);
        }

        In.close();
        JSONArray resarray = new JSONArray(content.toString());

        if(connexion.getResponseCode() == 200){
            JSONObject currentresobject = new JSONObject();
            for (int i =0 ; i<resarray.length() ; i++){
                currentresobject = resarray.getJSONObject(i);
                Music actMus = new Music((int) currentresobject.get("id"), (String) currentresobject.get("title"),
                        (String) currentresobject.get("fileName"), false, 0);
                musics.add(actMus);
            }
        }else{
            throw new LoadMusicsListFailedException("Error while loading musics list ! " +
                    "please check the provided properties, and check the server.");
        }
        System.out.println( "\u001B[32m" + " ------------- None" +
                " analysed musics list loaded with success... -------------" + "\u001B[0m");
        System.out.println(musics.size() + " tracks to analyse");
        return musics;
    }
}
