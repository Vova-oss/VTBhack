package com.example.securityframe.DAO;

import com.example.securityframe.Entity.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class AccountDAO {

    @Value("${spring.datasource.url}")
    String db_url;
    @Value("${spring.datasource.username}")
    String db_name;
    @Value("${spring.datasource.password}")
    String db_pass;

    public Account findByManagerId(Long id) {
        String sql = "select * from account where manager_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
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

    public boolean withdrawalOfFunds(Long account_id, Long amount) {
        String sql = "update account set current_account = current_account - ? where id = ?;";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, amount);
            ps.setLong(2, account_id);
            ps.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return false;
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
        return true;
    }
}
