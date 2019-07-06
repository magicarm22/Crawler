package Crawler;

import com.google.gson.JsonObject;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Elastic
{
    TransportClient client;
    static int iter = 0;
    public Elastic()
    {
        try {
            Settings elasticsearchSettings = Settings.builder()
                    .put("cluster.name", "docker-cluster").build();
            this.client = new PreBuiltTransportClient(elasticsearchSettings);
            this.client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        } catch (UnknownHostException e) {
            System.out.println("Connect to elastic error");
            e.printStackTrace();
        }
    }
    void JSON_to_elastic(JsonObject jsonObject) {
        this.client.prepareIndex("crawler", "Wikipedia")
                .setSource(jsonObject.toString(), XContentType.JSON).get();
    }

    /* Read documents from elastic by iterator
        One func. call - one document
     */

    public Map<String, Object> download_next_doc_from_elastic()
    {
        SearchResponse response = null;
        response = client.prepareSearch("crawler")
                .setTypes("Wikipedia")
                .setQuery(QueryBuilders.matchAllQuery())
                .setSize(1)
                .setFrom(this.iter)
                .execute()
                .actionGet();
        iter++;
        if (response.getHits().getTotalHits() - 1 < this.iter)
            return null;
        System.out.println(this.iter);
        return response.getHits().getAt(0).getSource();
    }

}
