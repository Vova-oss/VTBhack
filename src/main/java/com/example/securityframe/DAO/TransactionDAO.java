package com.example.securityframe.DAO;

import com.example.securityframe.Entity.Transaction;
import com.example.securityframe.ResponseModel.ExpenseSchedule.OneDay;
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

    public List<OneEntry> transactionHistory(Date from, Date to, String typeOfCard, String purpose, Long valueFrom,
                                             Long valueTo, String page, Long account_id) {
        String sql = "select * from\n" +
                "(select\n" +
                "        to_char(date, 'DD.MM.YY') datet\n" +
                "        , time\n" +
                "        , purpose\n" +
                "        , concat(w.surname, ' ', substring(w.name from 1 for 1), '. ', substring(w.patronymic from 1 for 1),'.' ) as fio\n" +
                "        , d.name\n" +
                "        , c.type\n" +
                "        , c.payment_system\n" +
                "        , c.card_number\n" +
                "        , transaction.value\n" +
                "        , c.currency\n" +
                "    from transaction\n" +
                "    join card c on transaction.card_id = c.id\n" +
                "    join worker w on c.worker_id = w.id\n" +
                "    join department d on w.department_id = d.id\n" +
                "where d.account_id = ?\n" +
                "       and date >= ?\n" +
                "       and date <= ?\n" +
                "       and c.type like ?\n" +
                "       and purpose like ?\n" +
                "       and value < ?\n" +
                "       and value > ?\n" +
                "\n" +
                "\n" +
                "union all\n" +
                "select\n" +
                "        to_char(date, 'DD.MM.YY')\n" +
                "        , time\n" +
                "        , purpose\n" +
                "        , concat(m.surname, ' ', substring(m.name from 1 for 1), '. ', substring(m.patronymic from 1 for 1), '.')\n" +
                "        , m.post\n" +
                "        , 'Пополнение карты'\n" +
                "        , 'Счёт'\n" +
                "        , a.account_number\n" +
                "        , transaction.value\n" +
                "        , a.currency\n" +
                "    from transaction\n" +
                "    join account a on transaction.account_id = a.id\n" +
                "    join manager m on a.manager_id = m.id\n" +
                "where a.id = ?\n" +
                "       and date >= ?\n" +
                "       and date <= ?\n" +
                "       and 'Счёт' like ?\n" +
                "       and purpose like ?\n" +
                "       and purpose != 'Банковская карта'\n" +
                "       and value < ?\n" +
                "       and value > ?\n" +
                ") " +
                "as big_table\n" +
                "\n" +
                "order by datet desc, time desc\n" +
                "limit 10 offset 10*?;";


        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, account_id);
            ps.setDate(2, from);
            ps.setDate(3, to);
            ps.setString(4, typeOfCard);
            ps.setString(5, purpose);
            ps.setLong(6, valueTo);
            ps.setLong(7, valueFrom);
            ps.setLong(8, account_id);
            ps.setDate(9, from);
            ps.setDate(10, to);
            ps.setString(11, typeOfCard);
            ps.setString(12, purpose);
            ps.setLong(13, valueTo);
            ps.setLong(14, valueFrom);
            ps.setLong(15, Long.parseLong(page));
            ResultSet r = ps.executeQuery();
            List<OneEntry> list = new ArrayList<>();
            while (r.next()){
                OneEntry oneEntry = new OneEntry();
                oneEntry.setDate(r.getString("datet"));
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
                "    to_char(date, 'DD.MM.YY') datet\n" +
                "     , time\n" +
                "     , purpose\n" +
                "     , concat(w.surname, ' ', substring(w.name from 1 for 1), '. ', substring(w.patronymic from 1 for 1),'.' ) as fio\n" +
                "     , d.name\n" +
                "     , c.type\n" +
                "     , c.payment_system\n" +
                "     , c.card_number\n" +
                "     , transaction.value\n" +
                "     , c.currency\n" +
                "from transaction\n" +
                "         join card c on transaction.card_id = c.id\n" +
                "         join worker w on c.worker_id = w.id\n" +
                "         join department d on w.department_id = d.id\n" +
                "where w.id = ?\n" +
                where +
                "order by datet, time\n" +
                "limit 10 offset 10*?";


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
                oneEntry.setDate(r.getString("datet"));
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

    public List<OneCategory> topSpendingCategories(Date from, Date to, String typeOfCard, String purpose, Long id) {
        String sql = "select SUM(val), category\n" +
                "from (\n" +
                "      select *\n" +
                "      from (\n" +
                "               select category,\n" +
                "                      transaction.value * (-1) val,\n" +
                "                      purpose\n" +
                "               from transaction\n" +
                "                        join card c on transaction.card_id = c.id\n" +
                "                        join worker w on c.worker_id = w.id\n" +
                "                        join department d on w.department_id = d.id\n" +
                "               where d.account_id = ?\n" +
                "                 and value < 0\n" +
                "       and date >= ?\n" +
                "       and date <= ?\n" +
                "       and c.type like ?\n" +
                "       and purpose like ?\n" +
                "\n" +
                "\n" +
                "               union all\n" +
                "               select category,\n" +
                "                      transaction.value * (-1),\n" +
                "                      purpose\n" +
                "               from transaction\n" +
                "                        join account a on transaction.account_id = a.id\n" +
                "                        join manager m on a.manager_id = m.id\n" +
                "               where a.id = ?\n" +
                "                 and value < 0\n" +
                "       and date >= ?\n" +
                "       and date <= ?\n" +
                "       and 'Счёт' like ?\n" +
                "       and purpose like ?\n" +
                "and purpose != 'Банковская карта'\n" +
                "           ) as tsc\n" +
                "      where purpose like '%'\n" +
                "  ) as tscTwoCol\n" +
                "group by category";


        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ps.setDate(2, from);
            ps.setDate(3, to);
            ps.setString(4, typeOfCard);
            ps.setString(5, purpose);
            ps.setLong(6, id);
            ps.setDate(7, from);
            ps.setDate(8, to);
            ps.setString(9, typeOfCard);
            ps.setString(10, purpose);
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

    public List<OneCategory> topSpendingCategoriesByWorker(String where, Long worker_id) {

        String sql = "select SUM(val), category\n" +
                "from (\n" +
                "         select *\n" +
                "         from (\n" +
                "                  select category,\n" +
                "                         value * (-1) val,\n" +
                "                         purpose\n" +
                "                  from transaction\n" +
                "                           join card c on transaction.card_id = c.id\n" +
                "                           join worker w on c.worker_id = w.id\n" +
                "                           join department d on w.department_id = d.id\n" +
                "                  where w.id = ?\n" +
                "                    and value < 0\n" + where +
                "              ) as tsc\n" +
                "         where purpose like '%'\n" +
                "     ) as tscTwoCol\n" +
                "group by category";


        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, worker_id);
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

    public Long findMonthlyExpensesByAccountId(Long id) {

        String sql = "select SUM(t.value) * (-1) val\n" +
                "    from transaction t\n" +
                "join card c on c.id = t.card_id\n" +
                "join worker w on c.worker_id = w.id\n" +
                "join department d on w.department_id = d.id\n" +
                "where t.card_id is not null\n" +
                "and d.account_id = ?\n" +
                "and t.value < 0\n" +
                "and to_char(t.date, 'YYYY') = to_char(NOW(), 'YYYY')\n" +
                "and to_char(t.date, 'MM') = to_char(NOW(), 'MM')\n" +
                "and t.date + t.time <= now();";


        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet r = ps.executeQuery();
            List<OneCategory> list = new ArrayList<>();
            if (r.next())
                return r.getLong("val");

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

    public List<OneDay> expenseSchedule(Date from, Date to, String typeOfCard, String purpose, Long id) {

        String sql = "select SUM(val), datet\n" +
                "from (\n" +
                "      select to_char(date, 'DD.MM.YY') datet,\n" +
                "             transaction.value * (-1) val\n" +
                "      from transaction\n" +
                "               join card c on transaction.card_id = c.id\n" +
                "               join worker w on c.worker_id = w.id\n" +
                "               join department d on w.department_id = d.id\n" +
                "      where d.account_id = ?\n" +
                "        and value < 0\n" +
                "       and date >= ?\n" +
                "       and date <= ?\n" +
                "       and type like ?\n" +
                "       and purpose like ?\n" +
                "\n" +
                "      union all\n" +
                "      select to_char(date, 'DD.MM.YY'),\n" +
                "             transaction.value * (-1)\n" +
                "      from transaction\n" +
                "               join account a on transaction.account_id = a.id\n" +
                "               join manager m on a.manager_id = m.id\n" +
                "      where a.id = ?\n" +
                "       and value < 0" +
                "       and date >= ?\n" +
                "       and date <= ?\n" +
                "       and 'Счёт' like ?\n" +
                "       and purpose like ?\n" +
                "       and purpose != 'Банковская карта'\n" +
                "  ) as tsc\n" +
                "\n" +
                "group by datet\n" +
                "order by datet;";


        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ps.setDate(2, from);
            ps.setDate(3, to);
            ps.setString(4, typeOfCard);
            ps.setString(5, purpose);
            ps.setLong(6, id);
            ps.setDate(7, from);
            ps.setDate(8, to);
            ps.setString(9, typeOfCard);
            ps.setString(10, purpose);
            ResultSet r = ps.executeQuery();
            List<OneDay> list = new ArrayList<>();
            while (r.next()){
                OneDay oneDay = new OneDay();
                oneDay.setDate(r.getString("datet"));
                oneDay.setAmount(r.getString("sum"));
                list.add(oneDay);
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

    public List<OneDay> expenseScheduleByWorker(Date from, Date to, String typeOfCard, String purpose, Long worker_id) {

        String sql = "select date,\n" +
                "        sum(transaction.value * (-1)) val\n" +
                " from transaction\n" +
                "          join card c on transaction.card_id = c.id\n" +
                "          join worker w on c.worker_id = w.id\n" +
                " where worker_id = ?\n" +
                "   and value < 0\n" +
                "   and date >= ?\n" +
                "   and date <= ?\n" +
                "   and type like ?\n" +
                "   and purpose like ?\n" +
                "group by date\n" +
                "order by date;";


        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, worker_id);
            ps.setDate(2, from);
            ps.setDate(3, to);
            ps.setString(4, typeOfCard);
            ps.setString(5, purpose);
            ResultSet r = ps.executeQuery();
            List<OneDay> list = new ArrayList<>();
            while (r.next()){
                OneDay oneDay = new OneDay();
                oneDay.setDate(r.getString("date"));
                oneDay.setAmount(r.getString("val"));
                list.add(oneDay);
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
