package Crawler;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class Main {
    private static int spider_threads = 1;
    private static int writer_threads = 8;

    public static void main(String[] args) throws IOException, SQLException, TimeoutException {
        Mysql_connector mysql = new Mysql_connector();
        RabbitMQ rabbit = new RabbitMQ();
        for (int i = 0; i < writer_threads; i++) {
            ThreadingWriter thread_w = new ThreadingWriter(rabbit);
            thread_w.start();
        }
        for (int i = 0; i < spider_threads; i++) {
            ThreadingSpider thread_s = new ThreadingSpider(mysql, rabbit);
            thread_s.start();
        }
    }
}
