package Crawler;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQ {

    protected Channel channel;
    protected Connection connection;
    protected ConnectionFactory factory;

    RabbitMQ() throws IOException {
        this.factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
        } catch (TimeoutException e) {
            System.out.println("No connection: " + e);
        }
    }

    public void AddToQueue(String url) throws IOException {
        channel.queueDeclare("Crawler", true, false, false, null);
        channel.basicPublish("", "Crawler", null, url.getBytes());
    }
}
