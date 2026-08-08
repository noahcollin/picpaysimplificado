package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private AuthorizationService authService;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User payer = this.userService.findUserById(transaction.payer());
        User payee = this.userService.findUserById(transaction.payee());

        // 🛑 NOVA VALIDAÇÃO: Bloqueia transferência para a própria conta
        if (payer.getId().equals(payee.getId())) {
            throw new Exception("Não é permitido realizar uma transferência para a própria conta.");
        }

        // 1. Validações de negócio (Saldo e Tipo de Usuário)
        this.userService.validateTransaction(payer, transaction.value());

        // 2. Consulta ao autorizador externo
        boolean isAuthorized = this.authService.authorizeTransaction(payer, transaction.value());
        if (!isAuthorized) {
            throw new Exception("Transação não autorizada pelo serviço externo.");
        }

        // 3. Monta e registra a transação
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setPayer(payer);
        newTransaction.setPayee(payee);
        newTransaction.setTimestamp(LocalDateTime.now());

        // 4. Executa o débito e o crédito nos saldos
        payer.setBalance(payer.getBalance().subtract(transaction.value()));
        payee.setBalance(payee.getBalance().add(transaction.value()));

        // 5. Salva alterações no banco
        this.repository.save(newTransaction);
        this.userService.saveUser(payer);
        this.userService.saveUser(payee);

        // 6. Notifica o recebedor
        this.notificationService.sendNotification(payee, "Você recebeu uma transferência com sucesso!");

        return newTransaction;
    }
}