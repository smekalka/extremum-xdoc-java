package io.extremum.functions.doc.controller;

import io.extremum.functions.doc.FunctionsPackage;
import io.extremum.functions.doc.service.DocService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FunctionsPackage.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class DocControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    DocService docService;

    @InjectMocks
    private DocController docController;

    @Test
    public void should_return_yaml_result() throws Exception {
        this.mockMvc.perform(get("/xdoc").header("Accept", "application/yaml"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/yaml;charset=UTF-8"));
    }

    @Test
    public void should_return_json_result_unwrapped() throws Exception {
        this.mockMvc.perform(get("/xdoc").header("Accept", "application/json").param("unwrapped", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void should_return_json_result() throws Exception {
        this.mockMvc.perform(get("/xdoc").header("Accept", "application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}