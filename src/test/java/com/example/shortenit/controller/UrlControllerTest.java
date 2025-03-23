package com.example.shortenit.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.shortenit.service.IUrlShorteningService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UrlController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.example.shortenit.security.RateLimitingFilter.class)
})
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUrlShorteningService urlShorteningService;

    @Test
    void testShortenUrl() throws Exception {
        when(urlShorteningService.shortenUrl("https://openai.com")).thenReturn("abc123");

        mockMvc.perform(post("/api/v1/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"longUrl\": \"https://openai.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("abc123"));
    }
}