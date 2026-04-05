package com.example.blackjack.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private Map<String, Object> messages;

    public GlobalControllerAdvice(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadMessages() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:text.json");
        messages = objectMapper.readValue(resource.getInputStream(), new TypeReference<Map<String, Object>>() {});
    }

    @ModelAttribute
    public void addMessagesToModel(Model model) {
        model.addAttribute("msg", messages);
    }
}
