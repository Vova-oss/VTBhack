package com.example.securityframe.DAO;

import com.example.securityframe.Entity.Transaction;
import com.example.securityframe.ResponseModel.HistoryOfTransactions.OneEntry;
import com.example.securityframe.ResponseModel.TopSpendingCategories.OneCategory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionDAO {

    @Value("${spring.datasource.url}")
    String db_url;
    @Value("${spring.datasource.username}")
    String db_name;
    @Value("${spring.datasource.password}")
    String db_pass;

    public void createTransaction(Transaction transaction) {
        String sql = "insert into transaction (account_id, card_id, category, purpose,  date, time, value) VALUES (?, ?, ?, ?, ?, ?, ?)";

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
            ps.setString(4, transaction.getPurpose());
            ps.setDate(5, new Date(System.currentTimeMillis()));
            ps.setTime(6, new Time(System.currentTimeMillis()));
            ps.setLong(7, transaction.getValue());
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

    public List<OneEntry> transactionHistory(String where, String page, Long account_id) {
        String sql = "select * from\n" +
                "(select\n" +
                "        date\n" +
                "        , time\n" +
                "        , purpose\n" +
                "        , concat(w.surname, ' ', substring(w.name from 1 for 1), '. ', substring(w.patronymic from 1 for 1),'.' ) as fio\n" +
                "        , d.name\n" +
                "        , c.type\n" +
                "        , c.payment_system\n" +
                "        , c.card_number\n" +
                "        , value\n" +
                "        , c.currency\n" +
                "    from transaction\n" +
                "    join card c on transaction.card_id = c.id\n" +
                "    join worker w on c.worker_id = w.id\n" +
                "    join department d on w.department_id = d.id\n" +
                "where d.account_id = ?\n" +
                where+
                "\n" +
                "\n" +
                "union all\n" +
                "select\n" +
                "        date\n" +
                "        , time\n" +
                "        , purpose\n" +
                "        , concat(m.surname, ' ', substring(m.name from 1 for 1), '. ', substring(m.patronymic from 1 for 1), '.')\n" +
                "        , m.post\n" +
                "        , 'Пополнение карты'\n" +
                "        , 'Счёт'\n" +
                "        , a.account_number\n" +
                "        , value\n" +
                "        , a.currency\n" +
                "    from transaction\n" +
                "    join account a on transaction.account_id = a.id\n" +
                "    join manager m on a.manager_id = m.id\n" +
                "where a.id = ?\n" +
                where+
                ") " +
                "as big_table\n" +
                "\n" +
                "order by date, time\n" +
                "limit 10 offset 10*?;";

        System.out.println(sql);

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, account_id);
            ps.setLong(2, account_id);
            ps.setLong(3, Long.parseLong(page));
            ResultSet r = ps.executeQuery();
            List<OneEntry> list = new ArrayList<>();
            while (r.next()){
                OneEntry oneEntry = new OneEntry();
                oneEntry.setDate(r.getString("date"));
                oneEntry.setTime(r.getString("time").substring(0,5));
                oneEntry.setPurpose(r.getString("purpose"));
                oneEntry.setFio(r.getString("fio"));
                oneEntry.setName(r.getString("name"));
                oneEntry.setType(r.getString("type"));
                oneEntry.setPayment_system(r.getString("payment_system"));
                oneEntry.setCard_number(r.getString("card_number"));
                oneEntry.setValue(r.getString("value"));
                oneEntry.setCurrency(r.getString("currency"));
                list.add(oneEntry);
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

    public List<OneEntry> transactionHistoryByWorkerId(String where, String page, Long worker_id) {

        String sql = "select\n" +
                "    date\n" +
                "     , time\n" +
                "     , purpose\n" +
                "     , concat(w.surname, ' ', substring(w.name from 1 for 1), '. ', substring(w.patronymic from 1 for 1),'.' ) as fio\n" +
                "     , d.name\n" +
                "     , c.type\n" +
                "     , c.payment_system\n" +
                "     , c.card_number\n" +
                "     , value\n" +
                "     , c.currency\n" +
                "from transaction\n" +
                "         join card c on transaction.card_id = c.id\n" +
                "         join worker w on c.worker_id = w.id\n" +
                "         join department d on w.department_id = d.id\n" +
                "where w.id = ?\n" +
                where +
                "order by date, time\n" +
                "limit 10 offset 10*?";

        System.out.println(sql);

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, worker_id);
            ps.setLong(2, Long.parseLong(page));
            ResultSet r = ps.executeQuery();
            List<OneEntry> list = new ArrayList<>();
            while (r.next()){
                OneEntry oneEntry = new OneEntry();
                oneEntry.setDate(r.getString("date"));
                oneEntry.setTime(r.getString("time").substring(0,5));
                oneEntry.setPurpose(r.getString("purpose"));
                oneEntry.setFio(r.getString("fio"));
                oneEntry.setName(r.getString("name"));
                oneEntry.setType(r.getString("type"));
                oneEntry.setPayment_system(r.getString("payment_system"));
                oneEntry.setCard_number(r.getString("card_number"));
                oneEntry.setValue(r.getString("value"));
                oneEntry.setCurrency(r.getString("currency"));
                list.add(oneEntry);
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

    public List<OneCategory> topSpendingCategories(String where, Long id) {
        String sql = "select SUM(value), category\n" +
                "from (\n" +
                "      select *\n" +
                "      from (\n" +
                "               select category,\n" +
                "                      value * (-1) value,\n" +
                "                      purpose\n" +
                "               from transaction\n" +
                "                        join card c on transaction.card_id = c.id\n" +
                "                        join worker w on c.worker_id = w.id\n" +
                "                        join department d on w.department_id = d.id\n" +
                "               where d.account_id = ?\n" +
                "                 and value < 0\n" + where +
                "\n" +
                "\n" +
                "               union all\n" +
                "               select category,\n" +
                "                      value * (-1),\n" +
                "                      purpose\n" +
                "               from transaction\n" +
                "                        join account a on transaction.account_id = a.id\n" +
                "                        join manager m on a.manager_id = m.id\n" +
                "               where a.id = ?\n" +
                "                 and value < 0\n" + where +
                "           ) as tsc\n" +
                "      where purpose like '%'\n" +
                "  ) as tscTwoCol\n" +
                "group by category";

        System.out.println(sql);

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ps.setLong(2, id);
            ResultSet r = ps.executeQuery();
            List<OneCategory> list = new ArrayList<>();
            while (r.next()){
                OneCategory oneCategory = new OneCategory();
                oneCategory.setCategory(r.getString("category"));
                oneCategory.setSum(r.getString("sum"));
                list.add(oneCategory);
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
