<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: magicarm
  Date: 25.03.19
  Time: 18:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Results</title>
</head>
<body>
<%
    List result;
    result=(ArrayList)request.getAttribute("result");
    if(result!=null && result.size()>0 ){
%>
<h3 align="center">Search result</h3>
<table>
<tr>
    <th>URL</th>
    <th>Title</th>
</tr>
<%
    for(int i=0;i<result.size();i++){
        String value = (String) result.get(i);
        value = value.substring(1, value.length()-1);           //remove curly brackets
        String[] keyValuePairs = value.split("\",\"");              //split the string to creat key-value pairs
        Map<String,String> map = new HashMap<>();
        System.out.println(value);
        for(int j = 0; j < 3; j++)                        //iterate over the pairs
        {
            String pair = keyValuePairs[j];
            System.out.println(pair);
            String[] entry = pair.split("\":\"");                   //split the pairs to get key and value
            System.out.println(entry[0].trim() + " " + entry[1].trim());
            map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
        }
 //       System.out.println(article);
%>
<tr>
    <td><a href=<%=map.get("\"url") %>><%=map.get("\"url") %></a></td>
    <td><%=map.get("title") %></td>
</tr>

<%
    }
    }
%>
</table>
</body>
</html>
