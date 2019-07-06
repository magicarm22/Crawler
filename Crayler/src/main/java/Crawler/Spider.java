package Crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;


public class Spider {

    private String start_url;
    private String url;
    private Document current_doc;
    private int count;
    private Mysql_connector mysql;
    private RabbitMQ rabbit;

    Spider(String start_url, String url, Mysql_connector mysql, RabbitMQ rabbit) throws IOException, SQLException {
        this.start_url = start_url;
        this.url = url;
        this.count = 1;
        this.mysql = mysql;
        this.rabbit = rabbit;
    }

    int strncmp(String str1, String str2, int n) {
        if (n > str1.length() || n > str2.length())
            return -1;
        str1 = str1.substring(0, n);
        str2 = str2.substring(0, n);
        return str1.equals(str2) ? 0 :
                str1.compareTo(str2) < 0 ? 1 : -1 ;
    }

    void start_spiding(int depth) throws IOException, SQLException {
        System.out.println("Current url: " + this.url + "\nStep: " + this.count);
        if (read_url() == 0)
            return ;
        if(depth > 9)
            return ;
        Elements links = this.current_doc.select("a[href]");
        for (Element link : links) {
            if (strncmp(link.attr("abs:href"), this.start_url, this.start_url.length()) == 0 &&
                    link.attr("abs:href").indexOf('#') == -1 && link.attr("abs:href").indexOf('&') == -1 &&
                    link.attr("abs:href").indexOf(':') == link.attr("abs:href").lastIndexOf(':') &&
                    !link.attr("abs:href").contains("disambiguation")) {

                this.url = link.attr("abs:href");

                if (this.mysql.mysql_insert(this.url, LocalDateTime.now()) == 1)
                {
                    rabbit.AddToQueue(this.url);
                    this.count++;
                }
                start_spiding(depth + 1);
            }
        }
    }

    int read_url() throws IOException {
        try {
            this.current_doc = Jsoup.connect(this.url).get();
            return 1;
        }
        catch (IOException err)
        {
            return 0;
        }
    }
}
