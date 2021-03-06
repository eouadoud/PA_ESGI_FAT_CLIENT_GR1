package Connection;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    public PropertiesReader() {
    }

    public Properties getConnecxionProps() throws Exception{
        System.out.println("Reading properties...");
        File propsFile = new File("./props/Config.properties");
        if ( ! propsFile.exists()){
            throw new Exception("Props file not found");
        }
        FileReader reader;
        try {
            reader = new FileReader(propsFile);
        }catch (IOException e){
            throw  new IOException("Error while reading properties");
        }
        Properties props = new Properties();
        props.load(reader);
        System.out.println( " ------------- Propoerties readed ------------- ");
        return props;
    }
}
