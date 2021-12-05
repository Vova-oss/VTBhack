package com.example.securityframe.DAO;

import com.example.securityframe.Entity.Card;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class CardDAO {

    @Value("${spring.datasource.url}")
    String db_url;
    @Value("${spring.datasource.username}")
    String db_name;
    @Value("${spring.datasource.password}")
    String db_pass;

    public boolean cardNumberExists(String card_number) {
        String sql = "select * from card where card_number = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setString(1, card_number);
            ResultSet r = ps.executeQuery();
            if (r.next())
                return true;
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
        return false;
    }

    public void add(Card card) {
        String sql = "insert into card (worker_id, payment_system, card_number, account, type, purpose_of_creation, status, currency) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, card.getWorker_id());
            ps.setString(2, card.getPayment_system());
            ps.setString(3, card.getCard_number());
            ps.setLong(4, card.getAccount());
            ps.setString(5, card.getType());
            ps.setString(6, card.getPurpose_of_creation());
            ps.setString(7, card.getStatus());
            ps.setString(8, card.getCurrency());
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
