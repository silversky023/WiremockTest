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
public class MockHelper
{
    // Logger
    private static Logger log = LoggerFactory.getLogger(MockHelper.class);

    // static fields
    private static String wireMockRootDir = "src\\test\\resources\\";
    private static int wireMockPort = 8080;
    private static Boolean wireMockRecord = true;
    private static String proxyHost = "http://localhost";
    private static int proxyPort;
    private static WireMockServer wireMockServer;

    // methods
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
        startRecording();
    }

    public static void mockServerStop()
    {
        if (wireMockRecord)
        {
            wireMockServer.stopRecording();
        }
        wireMockServer.stop();
    }


    // Helper Methods
    private static void startRecording()
    {
        if (wireMockRecord)
        {
            log.info(" [x] Starting mock server recording");

            if (proxyPort > 0)
                wireMockServer.startRecording(proxyHost + ":" + proxyPort);
            else
                wireMockServer.startRecording(proxyHost);
        }
    }

    private static void createDirectories()
    {
        log.info(" [x] Creating the directories");
        new SingleRootFileSource(wireMockRootDir + "mappings").createIfNecessary();
//        new SingleRootFileSource(wireMockRootDir + "__files").createIfNecessary();
    }


    // set different options
    public static void setOptions(int proxyPort) throws IOException
    {
        setRecord();
        MockHelper.proxyPort = proxyPort;
    }

    public static void setOptions(int port, int proxyPort) throws IOException
    {
        setRecord();
        MockHelper.proxyPort = proxyPort;
        wireMockPort = port;
    }

    public static void setOptions(int port, String proxyHost) throws IOException
    {
        setRecord();
        MockHelper.proxyHost = proxyHost;
        wireMockPort = port;
    }

    public static void setOptions(int port, String  proxyHost, int proxyPort) throws IOException
    {
        setRecord();
        MockHelper.proxyHost = proxyHost;
        MockHelper.proxyPort = proxyPort;
        wireMockPort = port;
    }

    private static void setRecord()
    {
        if(new File(wireMockRootDir + "mappings").exists())
            wireMockRecord = false;
    }
}


