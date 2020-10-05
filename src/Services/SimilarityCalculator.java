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
    //denant BL est la langueur de l'emprunt du song plus long Appelé BS
    // et SL est la longeur de l'mprunt du song moins long SS
    // TODO : Comparetre la sequence 0 => 100 du SS avec d => f du BS (avec d allant de 0 à BL-SL et f allant de BL-SL à BL-SL+100 )
    public int checkShift(String LongerFP, String ShorterFP){
        int iters = LongerFP.length() - ShorterFP.length();
        for (int i = 0; i< iters; i++){
            int distL = new DistanceCalculator().calculate(ShorterFP.substring(  0,  100), LongerFP.substring(i, i + 100));
            if(distL == 0){
                return i;
            }
        }
        return -1;
    }

    public int computeSimilarity(String finger1, String finger2){
        int dist = 0;
        int iteration = finger1.length()/100;
        for (long  i = 0 ; i < iteration - 3 ; i++){
            int currentDist = new DistanceCalculator().calculate(finger1.substring((int)i*100, (int)(i+1) * 100),
                    finger2.substring((int)i*100,(int) (i+1) * 100));
            dist += currentDist;
        }
        return dist;
    }
    public float getSimilarity(){
        float res = 0;
        Wave sound1 = new Wave(pathTack1);
        Wave sound2 = new Wave(pathTack2);
        int dist = -99;
        byte[] sound1Fingerprint = new FingerprintManager().extractFingerprint(sound1);
        byte[] sound2Fingerprint = new FingerprintManager().extractFingerprint(sound2);

        String ftS64 = Base64.getEncoder().encodeToString(sound1Fingerprint);
        String secS64 = Base64.getEncoder().encodeToString(sound2Fingerprint);
        int iterations = 0 ;
        // si y a pas de shift entre les musics (-1):
        //      calculer la similarité sur le plus petit SF et la partie BF.substring(stratshift, SF.Length)
        // sinon
        //      calcul de 0 à SF

        if(ftS64.length() == secS64.length()){
            iterations = ftS64.length()/100;
            dist = computeSimilarity(ftS64 , secS64);
        }else if(ftS64.length() > secS64.length()){
            iterations = secS64.length()/100;
            if(checkShift(ftS64, secS64) > -1){
                int startPoint = checkShift(ftS64, secS64);
                dist = computeSimilarity(ftS64.substring(startPoint, startPoint+ftS64.length()), secS64);
            }else{
                dist = computeSimilarity(ftS64.substring(0, secS64.length()) , secS64);
            }
        }else{
            iterations = ftS64.length()/100;
            if(checkShift(secS64, ftS64) > -1){
                int startPoint = checkShift(secS64, ftS64);
                dist = computeSimilarity(secS64.substring(startPoint, startPoint+ftS64.length()), ftS64);
            }else{
                dist = computeSimilarity(secS64.substring(0, ftS64.length() ) , ftS64);
            }
        }

        float similarityScore = 100 - (float) dist/(iterations*10);
        return similarityScore;
    }

}
