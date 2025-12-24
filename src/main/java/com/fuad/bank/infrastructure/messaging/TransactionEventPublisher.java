package com.fuad.bank.infrastructure.messaging;

import com.fuad.bank.domain.entity.Transaction;
import com.fuad.bank.infrastructure.messaging.dto.TransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(Transaction tx) {
        TransactionEvent event = new TransactionEvent(
                tx.getId(),
                tx.getTransactionType(),
                tx.getTransactionStatus(),
                tx.getAmount(),
                tx.getFromAccount() == null ? null : tx.getFromAccount().getId(),
                tx.getToAccount() == null ? null : tx.getToAccount().getId(),
                tx.getTimestamp(),
                tx.getErrorMessage()
        );

        rabbitTemplate.convertAndSend(
                RabbitConfig.TX_EXCHANGE,
                RabbitConfig.TX_ROUTING_KEY,
                event
        );
    }
}
