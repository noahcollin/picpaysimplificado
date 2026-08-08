package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository repository;

    @Mock
    private AuthorizationService authService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("Deve lançar exceção quando o autorizador externo negar a transação")
    void createTransactionCase1() throws Exception {
        User payer = new User(1L, "João", "Silva", "123", "joao@email.com", "123", new BigDecimal("500.00"), UserType.COMMON);
        User payee = new User(2L, "Loja", "Tech", "456", "loja@email.com", "123", new BigDecimal("1000.00"), UserType.MERCHANT);

        when(userService.findUserById(1L)).thenReturn(payer);
        when(userService.findUserById(2L)).thenReturn(payee);
        when(authService.authorizeTransaction(payer, new BigDecimal("100.00"))).thenReturn(false);

        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            TransactionDTO request = new TransactionDTO(new BigDecimal("100.00"), 1L, 2L);
            transactionService.createTransaction(request);
        });

        Assertions.assertEquals("Transação não autorizada pelo serviço externo.", thrown.getMessage());
    }
}