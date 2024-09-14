package com.sb.projects.trader.controller.IT;

import com.sb.projects.trader.TraderApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("mock")
public class StaticDataControllerTest extends TraderApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getETFMasterListAndVerifySBINifty50ETF() throws Exception {
        mockMvc.perform(get("http://localhost:8080/static-data/security/master/ETF")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo((res) -> System.out.println(res.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.[?(@.name==\"SBI Nifty 50 ETF\")]").exists());
    }
}
