package com.example.securityframe.DAO;

import com.example.securityframe.Entity.Manager;
import com.example.securityframe.Entity.Worker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class WorkerDAO {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String name;
    @Value("${spring.datasource.password}")
    String pass;

    public void addWorker(Worker worker) {
        String sql = "insert into worker(name, surname, patronymic, department_id) VALUES (?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(url, name, pass);
            ps = con.prepareStatement(sql);
            ps.setString(1, worker.getName());
            ps.setString(2, worker.getSurname());
            ps.setString(3, worker.getPatronymic());
            ps.setLong(4, worker.getDepartment_id());
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
