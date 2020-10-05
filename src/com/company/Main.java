package com.company;

import Connection.ConnectionManager;
import Connection.PropertiesReader;
import Models.Music;
import Services.*;
import com.musicg.fingerprint.FingerprintManager;
import com.musicg.wave.Wave;
import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;
import org.json.JSONObject;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Running...");

        AMQPConsumer amqpConsumer = new AMQPConsumer();
        amqpConsumer.consumeMessage(amqpConsumer.initConnection());
        
    }

}
