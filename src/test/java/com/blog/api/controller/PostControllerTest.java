package com.blog.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class PostControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/post 요청시 Hello World 를 출력한다.")
    void test() throws Exception {

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\" :  \"제목입니다.\", \"content\" :  \"내용입니다.\"}")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello World"))
                .andDo(print());
    }

    @Test
    @DisplayName("/post 요청시 title 값은 필수다.")
    void test2() throws Exception {

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\" :  null, \"content\" :  \"내용입니다.\"}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

}