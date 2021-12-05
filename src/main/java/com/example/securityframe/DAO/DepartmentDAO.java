package com.example.securityframe.DAO;

import com.example.securityframe.Entity.Department;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DepartmentDAO {

    @Value("${spring.datasource.url}")
    String db_url;
    @Value("${spring.datasource.username}")
    String db_name;
    @Value("${spring.datasource.password}")
    String dp_pass;

    public Department findByAccountId(Long account_id) {
        String sql = "select * from department where account_id = ? and name = 'Индивидуальные'";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, dp_pass);
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

    public List<Department> findAllByAccount_id(Long account_id) {
        String sql = "select * from department where account_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, dp_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, account_id);
            ResultSet r = ps.executeQuery();
            List<Department> list = new ArrayList<>();
            while (r.next()){
                Department department = new Department();
                department.setId(r.getLong("id"));
                department.setAccount_id(r.getLong("account_id"));
                department.setName(r.getString("name"));
                list.add(department);
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

    public boolean existenceOfName(String name, Long account_id) {
        String sql = "select * from department where account_id = ? and name = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, dp_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, account_id);
            ps.setString(2, name);
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

    public void save(Department department) {
        String sql = "insert into department (name, account_id) VALUES (?, ?)";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, dp_pass);
            ps = con.prepareStatement(sql);
            ps.setString(1, department.getName());
            ps.setLong(2, department.getAccount_id());
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

    public Department findByName(String name, Long account_id) {
        String sql = "select * from department where account_id = ? and name = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, dp_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, account_id);
            ps.setString(2, name);
            ResultSet r = ps.executeQuery();
            if (r.next()){
                Department department = new Department();
                department.setId(r.getLong("id"));
                department.setName(r.getString("name"));
                department.setAccount_id(r.getLong("account_id"));
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

    public Department findById(Long department_id) {
        String sql = "select * from department where id = ? ";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, dp_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, department_id);
            ResultSet r = ps.executeQuery();
            if (r.next()){
                Department department = new Department();
                department.setId(r.getLong("id"));
                department.setName(r.getString("name"));
                department.setAccount_id(r.getLong("account_id"));
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
