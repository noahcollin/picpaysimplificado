package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AuthorizationService {

    @Autowired
    private RestTemplate restTemplate;

    public boolean authorizeTransaction(User sender, BigDecimal value) {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                String status = (String) body.get("status");
                Map<String, Object> data = (Map<String, Object>) body.get("data");

                // Valida se o status é "success" e a autorização é true
                if ("success".equalsIgnoreCase(status) && data != null) {
                    Boolean isAuthorized = (Boolean) data.get("authorization");
                    return Boolean.TRUE.equals(isAuthorized);
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("⚠️ Autorizador externo negou ou falhou: " + e.getMessage());
            return false;
        }
    }
}