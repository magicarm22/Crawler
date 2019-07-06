package Crawler;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;

public class Mysql_connector {
    String ip = "jdbc:mysql://localhost:3306/wiki_crawler?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    String user = "root";
    String password = "4312160";
    Connection con;
    Statement stmt;

    Mysql_connector() throws SQLException{
        this.con = DriverManager.getConnection(ip, user, password);
        this.stmt = this.con.createStatement();
    }

    int mysql_insert(String site_name, LocalDateTime time) throws SQLException {
        try {
            this.stmt.executeUpdate("insert into crawled (url, time) Values ('" + site_name + "', '" + Timestamp.valueOf(time) +"')");
            return 1;
        }
        catch (SQLException sqlEx) {
            return 0;
        }
    }
}
