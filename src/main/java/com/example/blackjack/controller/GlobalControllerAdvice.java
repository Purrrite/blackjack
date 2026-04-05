package com.example.blackjack.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private Map<String, Map<String, Object>> langMessages = new HashMap<>();

    public GlobalControllerAdvice(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadMessages() throws IOException {
        Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                .getResources("classpath:lang/*.json");
        
        for (Resource resource : resources) {
            String filename = resource.getFilename();
            if (filename != null && filename.endsWith(".json")) {
                String langCode = filename.replace(".json", "");
                Map<String, Object> content = objectMapper.readValue(
                        resource.getInputStream(), 
                        new TypeReference<Map<String, Object>>() {}
                );
                langMessages.put(langCode, content);
            }
        }
    }

    @ModelAttribute
    public void addMessagesToModel(
            @RequestParam(name = "lang", required = false) String langParam,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {
        
        String selectedLang = "en"; // 기본값

        // 1. 파라미터 확인
        if (langParam != null && langMessages.containsKey(langParam)) {
            selectedLang = langParam;
            // 쿠키 저장
            Cookie cookie = new Cookie("language", selectedLang);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 30); // 30일
            response.addCookie(cookie);
        } else {
            // 2. 쿠키 확인
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("language".equals(c.getName()) && langMessages.containsKey(c.getValue())) {
                        selectedLang = c.getValue();
                        break;
                    }
                }
            }
        }

        model.addAttribute("msg", langMessages.get(selectedLang));
        model.addAttribute("currentLang", selectedLang);
    }
}
