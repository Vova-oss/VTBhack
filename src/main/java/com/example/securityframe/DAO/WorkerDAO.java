package com.example.securityframe.DAO;

import com.example.securityframe.Entity.Worker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class WorkerDAO {

    @Value("${spring.datasource.url}")
    String db_url;
    @Value("${spring.datasource.username}")
    String db_name;
    @Value("${spring.datasource.password}")
    String db_pass;

    public void addWorker(Worker worker) {
        String sql = "insert into worker(name, surname, patronymic, department_id) VALUES (?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
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

    public List<Worker> findAllByAccount_id(Long account_id) {
        String sql = "select worker.*\n" +
                "    from worker\n" +
                "    join department d on worker.department_id = d.id\n" +
                "    where d.account_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, account_id);
            ResultSet r = ps.executeQuery();
            List<Worker> list = new ArrayList<>();
            while (r.next()){
                Worker worker = new Worker();
                worker.setId(r.getLong("id"));
                worker.setName(r.getString("name"));
                worker.setSurname(r.getString("surname"));
                worker.setPatronymic(r.getString("patronymic"));
                worker.setDepartment_id(r.getLong("department_id"));
                list.add(worker);
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

    public void replaceDepartmentId(String id, Long department_id) {
        String sql = "update worker set department_id = ? where id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, department_id);
            ps.setLong(2, Long.parseLong(id));
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

    public Worker findById(String id_worker) {
        String sql = "select * from worker where id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, Long.parseLong(id_worker));
            ResultSet r = ps.executeQuery();
            if (r.next()){
                Worker worker = new Worker();
                worker.setId(r.getLong("id"));
                worker.setName(r.getString("name"));
                worker.setSurname(r.getString("surname"));
                worker.setPatronymic(r.getString("patronymic"));
                worker.setDepartment_id(r.getLong("department_id"));
                return worker;
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

    public List<Worker> findAllByDepartmentId(Long department_id) {
        String sql = "select * from worker where department_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, department_id);
            ResultSet r = ps.executeQuery();
            List<Worker> list = new ArrayList<>();
            while (r.next()){
                Worker worker = new Worker();
                worker.setId(r.getLong("id"));
                worker.setName(r.getString("name"));
                worker.setSurname(r.getString("surname"));
                worker.setPatronymic(r.getString("patronymic"));
                worker.setDepartment_id(r.getLong("department_id"));
                list.add(worker);
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
