package com.example.resttest;

import com.example.resttest.helper.MockClassRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestTestApplicationTests
{
	//Logger
	private Logger log = LoggerFactory.getLogger(RestTestApplication.class);

	@ClassRule
	public static MockClassRule mockClassRule = new MockClassRule(8082);

	@Test
	public void A_test() throws InterruptedException
	{
		String url = "http://localhost:9082/start";
		ResponseEntity responseEntity = new RestTemplate().getForEntity(url, String.class);
		String result = responseEntity.getBody().toString();
		log.info(" [x] RESULT : " + result);
		assertEquals("Success", result);
	}

	@Test
	public void B_Test()
	{
		String url = "http://localhost:9082/welcome/Charles";
		ResponseEntity responseEntity = new RestTemplate().getForEntity(url, String.class);

		log.info(" [x] Body : " + responseEntity.getBody().toString());
		log.info(" [x] Status Code : " + responseEntity.getStatusCodeValue());

		assertEquals("Welcome Charles", responseEntity.getBody().toString());
		assertEquals(200, responseEntity.getStatusCodeValue());
	}

	@Test(expected = HttpServerErrorException.class)
	public void C_Test()
	{
		String url = "http://localhost:9082/welcome/Finney";
		ResponseEntity responseEntity = new RestTemplate().getForEntity(url, String.class);

		log.info(" [x] Body : " + responseEntity.getBody().toString());
		log.info(" [x] Status Code : " + responseEntity.getStatusCodeValue());

		assertEquals("Welcome Finney", responseEntity.getBody().toString());
	}
}
