package Services;

import com.musicg.fingerprint.FingerprintManager;
import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.fingerprint.FingerprintSimilarityComputer;
import com.musicg.wave.Wave;
import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;

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

        //Extraire les emprunts depuis chanquune des deux musics
        byte[] sound1Fingerprint = new FingerprintManager().extractFingerprint(sound1);
        byte[] sound2Fingerprint = new FingerprintManager().extractFingerprint(sound2);

        //calculer la similarité entre les deux sons
        FingerprintSimilarity similarity = new FingerprintSimilarityComputer(sound2Fingerprint, sound1Fingerprint).getFingerprintsSimilarity();

        return similarity.getSimilarity();
    }

}
