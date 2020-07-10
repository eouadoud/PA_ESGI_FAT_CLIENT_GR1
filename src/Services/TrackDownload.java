package Services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class TrackDownload {
    public TrackDownload() {
    }
    public String runDownload(String DownloadLink,String TrackName ) throws IOException {
        ReadableByteChannel chanel;
        try{
            URL url = new URL(DownloadLink);
            chanel = Channels.newChannel(url.openStream());
        }catch (IOException e){
            throw new MalformedURLException("URL Non valid");
        }
        String fileLocation = "./tracksdb/tracks/"+ TrackName +".mp3" ;
        FileOutputStream dest = new FileOutputStream(fileLocation);
        dest.getChannel().transferFrom(chanel, 0, Long.MAX_VALUE);
        return fileLocation;
    }
}
