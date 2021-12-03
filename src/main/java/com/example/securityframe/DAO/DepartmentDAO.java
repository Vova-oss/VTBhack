package com.example.securityframe.DAO;

import com.example.securityframe.Entity.Department;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class DepartmentDAO {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String name;
    @Value("${spring.datasource.password}")
    String pass;

    public Department findByAccountId(Long account_id) {
        String sql = "select * from department where account_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(url, name, pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, account_id);
            ResultSet r = ps.executeQuery();
            if(r.next()){
                Department department = new Department();
                department.setId(r.getLong("id"));
                department.setAccount_id(r.getLong("account_id"));
                department.setName(r.getString("name"));
                return department;
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
