package com.example.resttest;

import com.example.resttest.helper.MockHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestTestApplicationTests
{
	//Logger
	private Logger log = LoggerFactory.getLogger(RestTestApplication.class);

	@BeforeClass
	public static void setUp() throws IOException, InterruptedException
	{
		Properties properties = new Properties();
		InputStream inputStream = new FileInputStream("application.properties");
		properties.load(inputStream);
		MockHelper.mockServerStart(Boolean.valueOf(properties.getProperty("wiremock.record", "false")));
	}

	@AfterClass
	public static void shutdown() throws IOException
	{
		Properties properties = new Properties();
		InputStream inputStream = new FileInputStream("application.properties");
		properties.load(inputStream);
		MockHelper.mockServerStop(Boolean.valueOf(properties.getProperty("wiremock.record", "false")));
	}

	@Test
	public void A_test() throws InterruptedException
	{
		String url = "http://localhost:9999/start";
		ResponseEntity responseEntity = new RestTemplate().getForEntity(url, String.class);
		String result = responseEntity.getBody().toString();
		log.info(" [x] RESULT : " + result);
		assertEquals("Success", result);
	}

	@Test
	public void B_Test()
	{
		String url = "http://localhost:9999/welcome/Charles";
		ResponseEntity responseEntity = new RestTemplate().getForEntity(url, String.class);

		log.info(" [x] Body : " + responseEntity.getBody().toString());
		log.info(" [x] Status Code : " + responseEntity.getStatusCodeValue());

		assertEquals("Welcome Charles", responseEntity.getBody().toString());
		assertEquals(200, responseEntity.getStatusCodeValue());
	}

	@Test(expected = HttpServerErrorException.class)
	public void C_Test()
	{
		String url = "http://localhost:9999/welcome/Finney";
		ResponseEntity responseEntity = new RestTemplate().getForEntity(url, String.class);

		log.info(" [x] Body : " + responseEntity.getBody().toString());
		log.info(" [x] Status Code : " + responseEntity.getStatusCodeValue());

		assertEquals("Welcome Finney", responseEntity.getBody().toString());
	}
}
