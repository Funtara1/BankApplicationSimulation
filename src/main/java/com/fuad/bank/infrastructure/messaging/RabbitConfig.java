package com.fuad.bank.infrastructure.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String TX_EXCHANGE = "bank.tx.exchange";
    public static final String TX_QUEUE = "bank.tx.queue";
    public static final String TX_ROUTING_KEY = "bank.tx.event";

    @Bean
    public DirectExchange txExchange() {
        return new DirectExchange(TX_EXCHANGE);
    }

    @Bean
    public Queue txQueue() {
        return new Queue(TX_QUEUE, true);
    }

    @Bean
    public Binding txBinding(Queue txQueue, DirectExchange txExchange) {
        return BindingBuilder.bind(txQueue).to(txExchange).with(TX_ROUTING_KEY);
    }
}
