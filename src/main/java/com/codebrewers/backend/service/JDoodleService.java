package com.codebrewers.backend.service;

import com.codebrewers.backend.dao.JDoodleRequest;
import com.codebrewers.backend.dao.JDoodleResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JDoodleService {

    private final String API_URL = "https://api.jdoodle.com/v1/execute";

    public JDoodleResponse executeScript(JDoodleRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        // Set headers if needed
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create the request entity
        HttpEntity<JDoodleRequest> requestEntity = new HttpEntity<>(request, headers);

        // Send POST request
        ResponseEntity<JDoodleResponse> responseEntity = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                requestEntity,
                JDoodleResponse.class
        );

        // Return the response body
        return responseEntity.getBody();
    }
}
