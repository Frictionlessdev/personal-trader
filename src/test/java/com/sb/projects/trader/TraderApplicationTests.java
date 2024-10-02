package com.sb.projects.trader;

import com.sb.projects.trader.config.MockServiceConfig;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@SpringBootTest
@ActiveProfiles(profiles={"it", "mock"})
public class TraderApplicationTests {

	@Test
	void contextLoads() {
	}
}
