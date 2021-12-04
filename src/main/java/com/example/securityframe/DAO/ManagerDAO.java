package com.example.securityframe.DAO;

import com.example.securityframe.Entity.Manager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class ManagerDAO {

    @Value("${spring.datasource.url}")
    String db_url;
    @Value("${spring.datasource.username}")
    String db_name;
    @Value("${spring.datasource.password}")
    String db_pass;


    public Manager findByEmail(String email){
        String sql = "select * from manager where email = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet r = ps.executeQuery();
            if(r.next()){
                Manager manager = new Manager();
                manager.setId(r.getLong("id"));
                manager.setEmail(r.getString("email"));
                manager.setPassword(r.getString("password"));
                return manager;
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

    public Manager findById(Long manager_id) {
        String sql = "select * from manager where id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, manager_id);
            ResultSet r = ps.executeQuery();
            if(r.next()){
                Manager manager = new Manager();
                manager.setId(r.getLong("id"));
                manager.setEmail(r.getString("email"));
                manager.setPassword(r.getString("password"));
                return manager;
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
