package com.example.securityframe.DAO;

import com.example.securityframe.Entity.Account;
import com.example.securityframe.Entity.Manager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class AccountDAO {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String name;
    @Value("${spring.datasource.password}")
    String pass;

    public Account findByManagerId(Long id) {
        String sql = "select * from account where manager_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(url, name, pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet r = ps.executeQuery();
            if(r.next()){
                Account account = new Account();
                account.setId(r.getLong("id"));
                account.setManager_id(r.getLong("manager_id"));
                account.setAccount_number(r.getString("account_number"));
                account.setCurrent_account(r.getLong("current_account"));
                account.setCurrency(r.getString("currency"));
                account.setTotal_balance(r.getLong("total_balance"));
                account.setAllocated_funds(r.getLong("allocated_funds"));
                account.setMonthly_expenses(r.getLong("monthly_expenses"));
                return account;
            }
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
        return null;
    }
}
