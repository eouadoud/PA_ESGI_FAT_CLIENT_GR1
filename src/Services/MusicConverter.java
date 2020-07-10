package Services;

import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;

public class MusicConverter {
    public MusicConverter() {
    }

    public String convertTowav(String trackName, String trackPath){
        String destination = "./tracksdb/notanalysed/"+trackName+".wav";
        try {
            new Converter().convert(trackPath, destination);
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
        return destination;

    }

}
