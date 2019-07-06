package servlets;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;


import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.InetAddress;
import java.sql.*;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class SearchSubmit extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,IOException{
        Connection con = null;
        TransportClient client;
        List<String> results = new ArrayList<String>();

        Settings elasticsearchSettings = Settings.builder()
                .put("cluster.name", "docker-cluster").build();
        client = new PreBuiltTransportClient(elasticsearchSettings);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        String search = request.getParameter("search");
        System.out.println(search);
            try{
                BoolQueryBuilder qb = QueryBuilders
                        .boolQuery()
                        .must(QueryBuilders.matchQuery("article", search));
                        //.must(QueryBuilders.matchQuery("title", search))

                SearchResponse res = client.prepareSearch("crawler")
                        .setQuery(qb).setSize(100)
                        .execute().actionGet();

                SearchHit[] hits = res.getHits().getHits();
                for (SearchHit hit : hits)
                    results.add(hit.getSourceAsString());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        request.setAttribute("result", results);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/search_submit.jsp");
        dispatcher.forward(request, response);
    }
}