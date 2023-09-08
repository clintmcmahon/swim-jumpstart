/*
 * Copyright (c) 2020 L3Harris Technologies
 */
package com.l3harris.swim.outputs;

import com.typesafe.config.Config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class RabbitMQOutput extends Output {
    private Config _config;

    public RabbitMQOutput(Config config) {
        super(config);
        _config = config;
    }

    @Override
    public void output(String message) {
        String QUEUE_NAME = _config.getString("dataSource");
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            try (Connection connection = factory.newConnection();
                    Channel channel = connection.createChannel()) {
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(QUEUE_NAME + " Sent message");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
