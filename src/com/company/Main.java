package com.company;

import Connection.ConnectionManager;
import Connection.PropertiesReader;
import Services.MusicConverter;
import Services.MusicsListLoader;
import Services.SimilarityCalculator;
import Services.TrackDownload;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Running...");

        PropertiesReader pr = new PropertiesReader();
        Properties Cpr = pr.getConnecxionProps();
        System.out.println( " ------------- Propoerties readed ------------- ");

        //String connectURL, String username, String password
        ConnectionManager cnm = new ConnectionManager( (String) Cpr.get("login_adress"),
                (String) Cpr.get("login"), (String) Cpr.get("passwd") );
        String token = cnm.Connect();
        //System.out.println( " ------ TOKEN :" + token);
         System.out.println("{{ ------------- READY TO START TASKS ---------- }}");

        MusicsListLoader musicsListLoader = new MusicsListLoader();
        musicsListLoader.loadList((String) Cpr.get("musics_list_adress") ,token );
        /*TrackDownload trservice = new TrackDownload();

        String fileLocation1 = trservice.runDownload("https://files.freemusicarchive.org/storage-freemusicarchive-org/music/no_curator/My_Yearnings/You_get_the_Blues_ID_1201.mp3",
                "test2");
        //String fileLocation2 = trservice.runDownload("https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_700KB.mp3", "test4");

        System.out.println(fileLocation1);
        //System.out.println(fileLocation2);
        MusicConverter converter = new MusicConverter();
        String track1 = converter.convertTowav("test2" ,fileLocation1);
        System.out.println(track1);
        //String track2 = converter.convertTowav("test4", fileLocation2);
        File floder = new File("./tracksdb/analysed/");
        File[] analysedFiles = floder.listFiles();
        if(analysedFiles.length == 0 ){
            System.out.println(" ---------- First music loaded, No similarity need to be done ---------- ");
            Path mover = Files.move(Paths.get(track1), Paths.get("./tracksdb/analysed/"+"test2.wav"));
        }else{
            boolean trackExists = false;
            for(int i = 0 ; i< analysedFiles.length; i++){
                if(analysedFiles[i].isFile()){
                    System.out.println(analysedFiles[i].getName());
                    SimilarityCalculator similarityCalculator = new SimilarityCalculator( track1,  "./tracksdb/analysed/"+analysedFiles[i].getName());
                    float simScore = similarityCalculator.getSimilarity();
                    System.out.println(simScore);
                    if(simScore == 1){
                        trackExists = true;
                        // signalé que la music existe déja
                        System.out.println(" ---------- Track allrady exists in database! removing... ---------- ");
                        File track = new File(track1);
                        if(track.exists()){
                            track.delete();
                        }
                        //./tracksdb/notanalysed/
                    }
                }
            }
            if(! trackExists){
                System.out.println(" ---------- Similarities calculating done! moving to analysed directory... ---------- ");
                Path mover = Files.move(Paths.get(track1), Paths.get("./tracksdb/analysed/"+"test2.wav"));
            }

        }

        // le client va récupérer les props de connexion => compte pour client lourd
        // Route pour récuprer l'ensmble des musics qui sont pas traités json {nom, lien pour download, ....}
        // traitement loud
        // Route post pour envoyé les résultats de traitement
		/*try {
			while (true) {
				TrackDownload downloader = new TrackDownload();
				MusicsListLoader loader = new MusicsListLoader();
				MusicConverter converter = new MusicConverter();
				List<Music> list = loader.loadList("LINK");
				File convertedFloder = new File("./src/main/resources/converted/");
				if(list.size()<1){
					System.out.println("{{ ------------- NO TASK FOUND ---------- }}");
				}else{
					Music currentMusic;
					for(int i = 0; i< list.size(); i++){
						currentMusic = list.get(i);
						String downloadDestination = downloader.runDownload(currentMusic.getDownloadLink(), currentMusic.getName());
						converter.convertTowav(currentMusic.getName(), downloadDestination);

					}
				}

				Thread.sleep(120 * 1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
    }
}
