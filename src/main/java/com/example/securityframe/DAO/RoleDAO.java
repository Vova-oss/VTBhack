package com.example.securityframe.DAO;

import com.example.securityframe.Entity.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoleDAO {

    @Value("${spring.datasource.url}")
    String db_url;
    @Value("${spring.datasource.username}")
    String db_name;
    @Value("${spring.datasource.password}")
    String db_pass;

    public List<Role> findByManagerId(Long id) {

        String sql = "select r.id, r.role\n" +
                "\tfrom role_manager rm\n" +
                "\tjoin \"role\" r on r.id = rm.role_id\n" +
                "\twhere rm.manager_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet r = ps.executeQuery();
            List<Role> list = new ArrayList<>();
            while(r.next()){
                Role role = new Role();
                role.setId(r.getLong("id"));
                role.setRole(r.getString("role"));
                list.add(role);
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
