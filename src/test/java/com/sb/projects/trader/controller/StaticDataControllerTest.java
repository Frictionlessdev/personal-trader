package com.sb.projects.trader.controller;

import com.sb.projects.trader.service.StaticDataService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static net.bytebuddy.matcher.ElementMatchers.isArray;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(StaticDataController.class)
class StaticDataControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StaticDataService staticDataService;

    @Test
    void getETFMasterList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/static-data/security/master/ETF")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}