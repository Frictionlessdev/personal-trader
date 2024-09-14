package com.sb.projects.trader;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles={"it", "mock"})
public class TraderApplicationTests {

	@Test
	void contextLoads() {
	}

}
