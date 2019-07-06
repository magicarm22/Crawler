package Crawler;

import com.google.gson.JsonObject;
import com.rabbitmq.client.DeliverCallback;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Writer {

    private Elastic elastic;
    private RabbitMQ rabbit;

    Writer(RabbitMQ rabbit) throws IOException {
        elastic = new Elastic();
        this.rabbit = rabbit;
    }


    public void GetFromQueue() throws IOException {
        this.rabbit.channel.queueDeclare("Crawler", true, false, false, null);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] Received '" + message + "'");
            try {
                start_write(message);
                this.rabbit.channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
            catch (IOException e){
                System.out.println(e);
            }
            finally {
                System.out.println(" [x] Done: " + message);
            }
        };
        this.rabbit.channel.basicConsume("Crawler", false, deliverCallback, consumerTag -> { });
    }


    void start_write(String url) throws IOException {
        JsonObject article = new JsonObject();
        article.addProperty("url", url);

        Document current_doc = Jsoup.connect(url).get();
        Element elem = current_doc.select("h1[id=firstHeading]").first();
        String pageTitle = elem.text();
        article.addProperty("title", pageTitle);

        elem=current_doc.select("div[id=content]").first();
        pageTitle = elem.text();
        article.addProperty("article", pageTitle);

        article.addProperty("category", "");
        this.elastic.JSON_to_elastic(article);

    }

}

