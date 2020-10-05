package Services;

import com.musicg.dsp.Resampler;
import com.musicg.fingerprint.FingerprintManager;
import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.fingerprint.FingerprintSimilarityComputer;
import com.musicg.wave.Wave;
import com.musicg.wave.WaveHeader;
import com.musicg.wave.extension.Spectrogram;
import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;

import java.util.Base64;
import java.util.List;

public class SimilarityCalculator {
    private String pathTack1;
    private String pathTack2;

    public SimilarityCalculator(String pathTack1, String pathTack2) {
        this.pathTack1 = pathTack1;
        this.pathTack2 = pathTack2;
    }

    public float getSimilarity(){
        float res = 0;
        // chargement des musics
        Wave sound1 = new Wave(pathTack1);
        Wave sound2 = new Wave(pathTack2);
        int dist = 0;
        //Extraire les emprunts depuis chanquune des deux musics
        byte[] sound1Fingerprint = new FingerprintManager().extractFingerprint(sound1);
        byte[] sound2Fingerprint = new FingerprintManager().extractFingerprint(sound2);

        String ftS64 = Base64.getEncoder().encodeToString(sound1Fingerprint);
        String secS64 = Base64.getEncoder().encodeToString(sound2Fingerprint);
        int iterations = 0 ;

        if(ftS64.length() > secS64.length()){
            iterations = secS64.length() / 100;
        }else{
            iterations = ftS64.length()/100;
        }

        for (long  i = 0 ; i < iterations - 1 ; i++){

            int currentDist = new DistanceCalculator().calculate(ftS64.substring((int)i*100, (int)(i+1) * 100), secS64.substring((int)i*100,(int) (i+1) * 100));
            dist += currentDist;
        }

        float similariryScore = 100 - (float) dist/(iterations*10);

        return similariryScore;
    }

}
