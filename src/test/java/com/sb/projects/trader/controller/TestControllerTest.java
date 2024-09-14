package com.sb.projects.trader.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestController.class)
public class TestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void requestTestController() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.get("/test")
                        .accept(MediaType.TEXT_PLAIN))
                .andDo((h) -> System.out.println(h.getResponse()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello World"));
    }

}