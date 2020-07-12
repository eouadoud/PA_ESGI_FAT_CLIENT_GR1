package com.company;

import Connection.ConnectionManager;
import Connection.PropertiesReader;
import Models.Music;
import Services.*;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Running...");

        float simScore = 0;
        SimilarityPutter similarityPutter = new SimilarityPutter();
        List<Music> musicsToAnalyse ;
        TrackDownload trservice = new TrackDownload();
        MusicConverter converter = new MusicConverter();
        PropertiesReader pr = new PropertiesReader();

        String dLink = "";

        Properties Cpr = pr.getConnecxionProps();
        dLink = (String) Cpr.get("download_adress");

        ConnectionManager cnm = new ConnectionManager( (String) Cpr.get("login_adress"),
                (String) Cpr.get("login"), (String) Cpr.get("passwd") );
        String token = cnm.Connect();

        System.out.println("{{ ------------- READY TO START TASKS ---------- }}");
        MusicsListLoader musicsListLoader = new MusicsListLoader();
        try{
            while (true){
                musicsToAnalyse = musicsListLoader.loadList((String) Cpr.get("musics_list_adress") ,token );
                for (int i = 0 ; i< musicsToAnalyse.size(); i++) {
                    //musicsToAnalyse.get(i).getName();
                    System.out.println(musicsToAnalyse.get(i).getName() + "::" + musicsToAnalyse.get(i).getDownloadLink());
                    String fileLocation = trservice.runDownload(dLink + musicsToAnalyse.get(i).getDownloadLink(),
                            musicsToAnalyse.get(i).getDownloadLink().substring(0, musicsToAnalyse.get(i).getDownloadLink().length() - 4), token);
                    String convertedTrackPath = converter.convertTowav(musicsToAnalyse.get(i).getDownloadLink().substring(0, musicsToAnalyse.get(i).getDownloadLink().length() - 4), fileLocation);

                    File floder = new File("./tracksdb/analysed/");
                    File[] analysedFiles = floder.listFiles();
                    if (analysedFiles.length == 0) {
                        System.out.println(" ---------- First music loaded, No similarity need to be done ---------- ");
                        Path mover = Files.move(Paths.get(convertedTrackPath),
                                Paths.get("./tracksdb/analysed/" +
                                        musicsToAnalyse.get(i).getDownloadLink().substring(0, musicsToAnalyse.get(i).getDownloadLink().length() - 4) + ".wav"));
                    } else {
                        boolean trackExists = false;
                        for (int j = 0; j < analysedFiles.length; j++) {
                            if (analysedFiles[j].isFile()) {

                                SimilarityCalculator similarityCalculator = new SimilarityCalculator(convertedTrackPath
                                        , "./tracksdb/analysed/" + analysedFiles[j].getName());
                                simScore = similarityCalculator.getSimilarity();
                                System.out.println("Tracks : " + analysedFiles[j].getName().substring(0, analysedFiles[j].getName().length() - 4) + " -- and  --"
                                        + musicsToAnalyse.get(i).getDownloadLink().substring(0, musicsToAnalyse.get(i).getDownloadLink().length() - 4));
                                System.out.println("  -------------------- Similarity: " + simScore + " -------------------- ");
                                similarityPutter.putSimilarity((String) Cpr.get("similarity_adress"), analysedFiles[j].getName().substring(0, analysedFiles[j].getName().length() - 4)
                                        , musicsToAnalyse.get(i).getDownloadLink().substring(0, musicsToAnalyse.get(i).getDownloadLink().length() - 4)
                                        , simScore, token);
                                if (simScore == 1) {
                                    trackExists = true;
                                    System.out.println(" ---------- Track allrady exists in database! removing... ---------- ");
                                    File track = new File(convertedTrackPath);
                                    if (track.exists()) {
                                        track.delete();
                                    }
                                }
                            }
                        }
                        if (!trackExists) {
                            System.out.println(" ---------- Similarities calculating done! moving to analysed directory... ---------- ");
                            Path mover = Files.move(Paths.get(convertedTrackPath),
                                    Paths.get("./tracksdb/analysed/"
                                            + musicsToAnalyse.get(i).getDownloadLink().substring(0, musicsToAnalyse.get(i).getDownloadLink().length() - 4)
                                            + ".wav"));
                        }
                    }
                    Thread.sleep(5000);
                }
                Thread.sleep(120 * 1000);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
