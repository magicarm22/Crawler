package Crawler;

import java.io.IOException;
import java.sql.SQLException;

class ThreadingSpider extends Thread {
    private Spider spider;

    ThreadingSpider(Mysql_connector mysql, RabbitMQ rabbit) throws IOException, SQLException {
        this.spider = new Spider("https://en.wikipedia.org/", "https://en.wikipedia.org/", mysql, rabbit);
    }

    public void run() {
        try {
            spider.start_spiding(0);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}

class ThreadingWriter extends Thread {

    private Writer writer;
    ThreadingWriter(RabbitMQ rabbit) throws IOException {
        this.writer = new Writer(rabbit);
    }

    public void run() {
        try {
            writer.GetFromQueue();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


