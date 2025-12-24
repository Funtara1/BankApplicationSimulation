package com.fuad.bank.infrastructure.messaging;

import com.fuad.bank.infrastructure.messaging.dto.TransactionEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventListener {

    @RabbitListener(queues = RabbitConfig.TX_QUEUE)
    public void onMessage(TransactionEvent event) {
        System.out.println("TX EVENT: " + event);
    }
}
