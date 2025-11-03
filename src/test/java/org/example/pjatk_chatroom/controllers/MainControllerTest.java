package org.example.pjatk_chatroom.controllers;

import org.example.pjatk_chatroom.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(MainController.class)
@ExtendWith(MockitoExtension.class)
class MainControllerTest {

    @MockitoBean
    MessageService messageService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new MainController(messageService)).build();
    }

    @WithMockUser(username = "test")
    @Test
    void index() throws Exception {
        //when
        mockMvc.perform(get("/"))
                .andExpect(view().name("index"))
                .andExpect(status().isOk());
    }
}