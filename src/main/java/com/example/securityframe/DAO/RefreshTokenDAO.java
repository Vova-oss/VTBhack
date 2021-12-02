package com.example.securityframe.DAO;

import com.example.securityframe.Entity.Manager;
import com.example.securityframe.Entity.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RefreshTokenDAO {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String name;
    @Value("${spring.datasource.password}")
    String pass;

    public void save(RefreshToken refreshToken) {
        String sql = "insert into refresh_token (manager_id, token, expiry_date) VALUES (?, ?, ?)";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(url, name, pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, refreshToken.getManager_id());
            ps.setString(2, refreshToken.getToken());
            ps.setLong(3, refreshToken.getExpiryDate());
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

    public RefreshToken findByToken(String token) {

        String sql = "select * from refresh_token where token = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(url, name, pass);
            ps = con.prepareStatement(sql);
            ps.setString(1, token);
            ResultSet r = ps.executeQuery();
            if(r.next()){
                RefreshToken refreshToken = new RefreshToken();
                refreshToken.setId(r.getLong("id"));
                refreshToken.setToken(r.getString("token"));
                refreshToken.setExpiryDate(r.getLong("expiry_date"));
                refreshToken.setManager_id(r.getLong("manager_id"));
                return refreshToken;
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

    public void delete(RefreshToken refreshToken) {

        String sql = "delete from refresh_token where id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(url, name, pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, refreshToken.getId());
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

    public List<RefreshToken> findAllByUserEntity(Manager manager) {

        String sql = "select * from refresh_token where manager_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(url, name, pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, manager.getId());
            ResultSet r = ps.executeQuery();
            List<RefreshToken> list = null;
            while (r.next()){
                list = new ArrayList<>();
                RefreshToken refreshToken = new RefreshToken();
                refreshToken.setId(r.getLong("id"));
                refreshToken.setToken(r.getString("token"));
                refreshToken.setExpiryDate(r.getLong("expiry_date"));
                refreshToken.setManager_id(r.getLong("manager_id"));
                list.add(refreshToken);
            }
            return list;
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
