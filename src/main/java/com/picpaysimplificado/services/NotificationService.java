package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(User user, String message) {
        String email = user.getEmail();
        NotificationDTO notificationRequest = new NotificationDTO(email, message);

        try {
            // Chamada para a API Mock externa
            ResponseEntity<String> notificationResponse = this.restTemplate.postForEntity("https://util.devi.tools/api/v1/notify", notificationRequest, String.class);

            // Log de sucesso impresso no terminal do IntelliJ!
            System.out.println("----------------------------------------------------------------------");
            System.out.println("📲 [SERVIÇO DE NOTIFICAÇÃO] Notificação enviada com sucesso para: " + email);
            System.out.println("📩 Mensagem: " + message);
            System.out.println("----------------------------------------------------------------------");

        } catch (Exception e) {
            // Tratamento para quando o serviço mock estiver indisponível/instável
            System.out.println("----------------------------------------------------------------------");
            System.out.println("⚠️ [SERVIÇO DE NOTIFICAÇÃO] Falha ao enviar notificação para " + email + ": " + e.getMessage());
            System.out.println("----------------------------------------------------------------------");
        }
    }
}