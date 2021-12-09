package com.example.securityframe.DAO;

import com.example.securityframe.Entity.Card;
import com.example.securityframe.Entity.Worker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CardDAO {

    @Value("${spring.datasource.url}")
    String db_url;
    @Value("${spring.datasource.username}")
    String db_name;
    @Value("${spring.datasource.password}")
    String db_pass;

    public boolean cardNumberExists(String card_number) {
        String sql = "select * from card where card_number = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setString(1, card_number);
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

    public void add(Card card) {
        String sql = "insert into card (worker_id, payment_system, card_number, account, type, purpose_of_creation, status, currency) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, card.getWorker_id());
            ps.setString(2, card.getPayment_system());
            ps.setString(3, card.getCard_number());
            ps.setLong(4, card.getAccount());
            ps.setString(5, card.getType());
            ps.setString(6, card.getPurpose_of_creation());
            ps.setString(7, card.getStatus());
            ps.setString(8, card.getCurrency());
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

    public List<Card> findAllByWorkerId(Long worker_id) {
        String sql = "select * from card where worker_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, worker_id);
            ResultSet r = ps.executeQuery();
            List<Card> list = new ArrayList<>();
            while (r.next()){
                Card card = new Card();
                card.setId(r.getLong("id"));
                card.setWorker_id(worker_id);
                card.setPayment_system(r.getString("payment_system"));
                card.setCard_number(r.getString("card_number"));
                card.setAccount(r.getLong("account"));
                card.setType(r.getString("type"));
                card.setPurpose_of_creation(r.getString("purpose_of_creation"));
                card.setStatus(r.getString("status"));
                card.setLimit(r.getLong("limit"));
                card.setTerm(r.getLong("term"));
                card.setRemains(r.getLong("remains"));
                card.setAutoUpdate(r.getBoolean("auto_update"));
                card.setCurrency(r.getString("currency"));
                list.add(card);
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

    public Long findAccountByWorkerId(Long worker_id) {
        String sql = "select sum(account)\n" +
                "from card\n" +
                "where worker_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, worker_id);
            ResultSet r = ps.executeQuery();
            if (r.next()){
                return r.getLong("sum");
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

    /**
     * Пополнение счёта карты
     * @param card_id - :id карты
     * @param amount - сумма, на которую хотим пополнить
     */
    public void topUpAccount(Long card_id, Long amount) {
        String sql = "update card set account = account + ? where id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, amount);
            ps.setLong(2, card_id);
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

    public Card findById(Long card_id) {
        String sql = "select * from card where id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, card_id);
            ResultSet r = ps.executeQuery();
            if (r.next()){
                Card card = new Card();
                card.setId(card_id);
                card.setWorker_id(r.getLong("worker_id"));
                card.setPayment_system(r.getString("payment_system"));
                card.setCard_number(r.getString("card_number"));
                card.setAccount(r.getLong("account"));
                card.setType(r.getString("type"));
                card.setPurpose_of_creation(r.getString("purpose_of_creation"));
                card.setStatus(r.getString("status"));
                card.setLimit(r.getLong("limit"));
                card.setTerm(r.getLong("term"));
                card.setRemains(r.getLong("remains"));
                card.setAutoUpdate(r.getBoolean("auto_update"));
                card.setCurrency(r.getString("currency"));
                return card;
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

    /**
     * Вечная блокировка карты
     * @param card_id = :id карты, которую желаем заблокировать
     */
    public void changeStatusOfCard(Long card_id, String status) {
        String sql = "update card set status = ? where id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setLong(2, card_id);
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

    public void updateLimitById(Long card_id, Long limit, Long term, Boolean autoUpdate) {
        String sql = "update card set \"limit\" = ?, term = ?, auto_update = ?, remains = ? where id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, limit);
            ps.setLong(2, term);
            ps.setBoolean(3, autoUpdate);
            ps.setLong(4, limit);
            ps.setLong(5, card_id);
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

    public List<Card> findAll() {
        String sql = "select * from card";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ResultSet r = ps.executeQuery();
            List<Card> list = new ArrayList<>();
            while (r.next()){
                Card card = new Card();
                card.setId(r.getLong("id"));
                card.setWorker_id(r.getLong("worker_id"));
                card.setPayment_system(r.getString("payment_system"));
                card.setCard_number(r.getString("card_number"));
                card.setAccount(r.getLong("account"));
                card.setType(r.getString("type"));
                card.setPurpose_of_creation(r.getString("purpose_of_creation"));
                card.setStatus(r.getString("status"));
                card.setLimit(r.getLong("limit"));
                card.setTerm(r.getLong("term"));
                card.setRemains(r.getLong("remains"));
                card.setAutoUpdate(r.getBoolean("auto_update"));
                card.setCurrency(r.getString("currency"));
                list.add(card);
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

    public void transferFromCard(Long card_id, Long amount) {
        String sql = "update card set account = account - ? where id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, amount);
            ps.setLong(2, card_id);
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


    public Long findAccountByAccountId(Long id) {
        String sql = "select sum(account)\n" +
                "from card\n" +
                "join worker w on card.worker_id = w.id\n" +
                "join department d on w.department_id = d.id\n" +
                "where account_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet r = ps.executeQuery();
            if (r.next()){
                return r.getLong("sum");
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

    public Long amountOfActiveCardsByAccountId(Long account_id) {
        String sql = "select count(*)\n" +
                "    from card\n" +
                "    join worker w on card.worker_id = w.id\n" +
                "    join department d on d.id = w.department_id\n" +
                "where d.account_id = ?\n" +
                "and status = 'ACTIVE';";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DriverManager.getConnection(db_url, db_name, db_pass);
            ps = con.prepareStatement(sql);
            ps.setLong(1, account_id);
            ResultSet r = ps.executeQuery();
            if (r.next()){
                return r.getLong("count");
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
