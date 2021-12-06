package com.example.securityframe.DAO;

import com.example.securityframe.Entity.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.Calendar;
import java.util.Optional;

@Component
public class TransactionDAO {

    @Value("${spring.datasource.url}")
    String db_url;
    @Value("${spring.datasource.username}")
    String db_name;
    @Value("${spring.datasource.password}")
    String db_pass;

    public void createTransaction(Transaction transaction) {
        String sql = "insert into transaction (account_id, card_id, category, date, value) VALUES (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);

            if(transaction.getAccount_id()!=null)
                ps.setLong(1, transaction.getAccount_id());
            else ps.setNull(1, Types.INTEGER);

            if(transaction.getCard_id()!=null)
                ps.setLong(2, transaction.getCard_id());
            else ps.setNull(2, Types.INTEGER);

            ps.setString(3, transaction.getCategory());
            ps.setDate(4, new Date(System.currentTimeMillis()));
            ps.setLong(5, transaction.getValue());
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
