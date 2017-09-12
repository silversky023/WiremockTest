package com.example.resttest.helper;

import ch.qos.logback.core.util.FileUtil;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.standalone.WireMockServerRunner;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Finn on 10-09-2017.
 */
public class MockHelper
{
    // Logger
    private static Logger log = LoggerFactory.getLogger(MockHelper.class);

    // values from properties file
    private String wireMockPort;
    private String wireMockRootDir;
    private String wireMockProxyHost;
    private String wireMockProxyPort;

    // static fields
//    private static WireMockServerRunner wireMockServerRunner;
    private static WireMockServer wireMockServer;

    // Helper Methods
    public void setOptions() throws IOException
    {
        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream("application.properties");
        properties.load(inputStream);
        wireMockPort = properties.getProperty("wiremock.port", "9999");
        wireMockRootDir = properties.getProperty("wiremock.root.dir");
        wireMockProxyHost = properties.getProperty("wiremock.proxy.host");
        wireMockProxyPort = properties.getProperty("wiremock.proxy.port");
    }

    public static void mockServerStart(Boolean record) throws IOException, InterruptedException
    {
        MockHelper mockHelper = new MockHelper();
        mockHelper.setOptions();

        log.info(" [x] Mock port : " + mockHelper.wireMockPort);
        log.info(" [x] Root dir : " + mockHelper.wireMockRootDir);
        log.info(" [x] Proxy port : " + mockHelper.wireMockProxyPort);
        log.info(" [x] Proxy host : " + mockHelper.wireMockProxyHost);

        wireMockServer = new WireMockServer(WireMockConfiguration.options()
                .port(Integer.parseInt(mockHelper.wireMockPort))
                .withRootDirectory(mockHelper.wireMockRootDir)
                .notifier(new ConsoleNotifier(true)));

        wireMockServer.start();

        log.info(" [x] Starting mock server");
        log.info(" [x] Starting mock server recording");

        if (record)
        {
            FileUtils.cleanDirectory(new java.io.File(mockHelper.wireMockRootDir + "mappings"));
            FileUtils.cleanDirectory(new java.io.File(mockHelper.wireMockRootDir + "__files"));
            wireMockServer.startRecording("http://" + mockHelper.wireMockProxyHost + ":" + mockHelper.wireMockProxyPort);
        }
    }

    public static void mockServerStop(Boolean isRecording)
    {
        if (isRecording)
            wireMockServer.stopRecording();

        wireMockServer.stop();
    }

//    public static void mockServerStart() throws IOException
//    {
//        MockHelper mockHelper = new MockHelper();
//        mockHelper.setOptions();
//
//        log.info(" [x] Mock port : " + mockHelper.wireMockPort);
//        log.info(" [x] Root dir : " + mockHelper.wireMockRootDir);
//        log.info(" [x] Proxy port : " + mockHelper.wireMockProxyPort);
//        log.info(" [x] Proxy host : " + mockHelper.wireMockProxyHost);
//
//        log.info(" [x] (MOCKSERVER) Starting mock server.");
//
//        wireMockServerRunner = new WireMockServerRunner();
//
//        wireMockServerRunner.run(
//                "--port", mockHelper.wireMockPort,
//                "--root-dir", mockHelper.wireMockRootDir,
//                "--proxy-all", "http://" + mockHelper.wireMockProxyHost + "/" + mockHelper.wireMockProxyPort,
//                "--record-mappings",
//                "--verbose");
//    }
//
//    public static void mockServerStop()
//    {
//            wireMockServerRunner.stop();
//    }
}
