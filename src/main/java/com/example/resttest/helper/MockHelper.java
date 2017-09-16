package com.example.resttest.helper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.common.SingleRootFileSource;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Finn on 10-09-2017.
 */
@Component
public class MockHelper
{
    // Logger
    private static Logger log = LoggerFactory.getLogger(MockHelper.class);

    // static fields
    private static String wireMockRootDir;
    private static int wireMockPort;
    private static Boolean wireMockRecord = true;
    private static int proxyPort;
    private static WireMockServer wireMockServer;

    // Helper Methods
    public static void mockServerStart() throws IOException, InterruptedException
    {
        log.info(" [x] Root dir : " + wireMockRootDir);

        log.info(" [x] Mock Port : " + wireMockPort);

        log.info(" [x] Proxy Port : " + proxyPort);

        log.info(" [x] Record : " + wireMockRecord);

        wireMockServer = new WireMockServer(WireMockConfiguration.options()
                                            .port(wireMockPort)
                                            .withRootDirectory(wireMockRootDir)
                                            .notifier(new ConsoleNotifier(true)));

        createDirectories();

        log.info(" [x] Starting mock server");

        wireMockServer.start();

        if (wireMockRecord)
        {
            log.info(" [x] Starting mock server recording");

            wireMockServer.startRecording("http://localhost:" + proxyPort);
        }
    }

    public static void mockServerStop()
    {
        if (wireMockRecord)
        {
            wireMockServer.stopRecording();
        }
        wireMockServer.stop();
    }

    public static void setOptions(int port) throws IOException
    {
        Properties properties = new Properties();

        properties.load(new FileInputStream("application.properties"));

        wireMockRootDir = properties.getProperty("wiremock.root.dir");

        if(new File(wireMockRootDir + "mappings").exists())
        {
            wireMockRecord = false;
        }

        proxyPort = port;

        wireMockPort = port + 1000;
    }

    private static void createDirectories()
    {
        log.info(" [x] Creating the directories");

        new SingleRootFileSource(wireMockRootDir + "mappings").createIfNecessary();

//        new SingleRootFileSource(wireMockRootDir + "__files").createIfNecessary();
    }
}


