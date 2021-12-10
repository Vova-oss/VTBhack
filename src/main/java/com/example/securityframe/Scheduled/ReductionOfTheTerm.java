package com.example.securityframe.Scheduled;

import com.example.securityframe.Entity.Card;
import com.example.securityframe.Service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReductionOfTheTerm {

    @Value("${spring.datasource.url}")
    private String db_url;
    @Value("${spring.datasource.username}")
    private String db_name;
    @Value("${spring.datasource.password}")
    private String db_pass;

    @Scheduled(cron = "0 0 0 * * *")
    public void reductionOfTheTerm(){
        decrementTerm();
        autoUpdateOfLimit();
    }

    private void decrementTerm() {
        String sql = "update card set term = term -1 where term > 0";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }finally {
            try {
                if(con != null)
                    con.close();
                if(ps != null)
                    ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void autoUpdateOfLimit(){
        String sql = "update card " +
                "set " +
                    "term = (select limit_history.term from limit_history where card_id = card.id), " +
                    "remains = (select limit_history.\"limit\" from limit_history where card_id = card.id), " +
                    "\"limit\" = (select limit_history.\"limit\" from limit_history where card_id = card.id) " +
                "where term = 0 and auto_update = true";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }finally {
            try {
                if(con != null)
                    con.close();
                if(ps != null)
                    ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

}
