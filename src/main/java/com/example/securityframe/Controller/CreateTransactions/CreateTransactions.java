package com.example.securityframe.Controller.CreateTransactions;

import com.example.securityframe.DAO.CardDAO;
import com.example.securityframe.Entity.Card;
import com.example.securityframe.Entity.Transaction;
import com.example.securityframe.Entity.Worker;
import com.example.securityframe.ResponseModel.DepartmentsWorkersCards.CardDTO;
import com.example.securityframe.Service.CardService;
import com.example.securityframe.Service.DepartmentService;
import com.example.securityframe.Service.TransactionService;
import com.example.securityframe.Service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CreateTransactions {

    @Value("${spring.datasource.url}")
    String db_url;
    @Value("${spring.datasource.username}")
    String db_name;
    @Value("${spring.datasource.password}")
    String db_pass;

    @Autowired
    TransactionService transactionService;
    @Autowired
    CardService cardService;
    @Autowired
    CardDAO cardDAO;
    @Autowired
    WorkerService workerService;

    @GetMapping("/createTransactions")
    public void createTransactions(HttpServletRequest request, HttpServletResponse response){
        Worker worker = new Worker();
        worker.setName("Петр");
        worker.setSurname("Петров");
        worker.setPatronymic("Петрович");
        worker.setDepartment_id(1L);
        workerService.addWorker(worker, request, response);

        worker.setName("Александр");
        worker.setSurname("Александров");
        worker.setPatronymic("Никитин");
        worker.setDepartment_id(1L);
        workerService.addWorker(worker, request, response);

        Card card = new Card();
        card.setWorker_id(1L);
        card.setPayment_system("VISA");
        long v;
        do {
            v = (long) (Math.random() * 1_0000_0000_0000_0000L);
        } while (cardDAO.cardNumberExists(String.valueOf(v)) || v < 1_000_000_000_000_000L);
        card.setCard_number(String.valueOf(v).substring(0, 16));
        card.setAccount(0L);
        card.setType("Транспорт");
        card.setPurpose_of_creation("Поездка в метро");
        card.setStatus("ACTIVE");
        card.setCurrency("RUB");
        cardDAO.add(card);

        card.setWorker_id(1L);
        card.setPayment_system("VISA");
        do {
            v = (long) (Math.random() * 1_0000_0000_0000_0000L);
        } while (cardDAO.cardNumberExists(String.valueOf(v)) || v < 1_000_000_000_000_000L);
        card.setCard_number(String.valueOf(v).substring(0, 16));
        card.setAccount(0L);
        card.setType("Оборудование");
        card.setPurpose_of_creation("Нехватка видеокассет");
        card.setStatus("ACTIVE");
        card.setCurrency("RUB");
        cardDAO.add(card);

        card.setWorker_id(2L);
        card.setPayment_system("VISA");
        do {
            v = (long) (Math.random() * 1_0000_0000_0000_0000L);
        } while (cardDAO.cardNumberExists(String.valueOf(v)) || v < 1_000_000_000_000_000L);
        card.setCard_number(String.valueOf(v).substring(0, 16));
        card.setAccount(0L);
        card.setType("Подписка");
        card.setPurpose_of_creation("Figma");
        card.setStatus("ACTIVE");
        card.setCurrency("RUB");
        cardDAO.add(card);

//        List<String> categories = List.of("Подписочные сервисы", "Рекламные компании", "Транспорт","Отели");
//        List<String> podServices = List.of("Figma", "AWS", "Dropbox", "Mailchimp");
//        List<String> reclCompany = List.of("Facebook", "GoogleADS");
//        List<String> transport = List.of("Яндекс.Такси");
//        List<String> hotel = List.of("Trivago", "AIRBnB");

        List<CategoryAndPurpose> list = new ArrayList<>();
        list.add(new CategoryAndPurpose("Подписочные сервисы", "Figma"));
        list.add(new CategoryAndPurpose("Подписочные сервисы", "AWS"));
        list.add(new CategoryAndPurpose("Подписочные сервисы", "Dropbox"));
        list.add(new CategoryAndPurpose("Подписочные сервисы", "Mailchimp"));
        list.add(new CategoryAndPurpose("Рекламные компании", "Facebook"));
        list.add(new CategoryAndPurpose("Рекламные компании", "GoogleADS"));
        list.add(new CategoryAndPurpose("Транспорт", "Яндекс.Такси"));
        list.add(new CategoryAndPurpose("Отели", "Trivago"));
        list.add(new CategoryAndPurpose("Отели", "AIRBnB"));


        // 1000 = 1 секунда
        Long nowTime = System.currentTimeMillis();
        // 10 дней назад
        nowTime = nowTime - 864_000_000;
        for(int j = 0; j < 50; j++) {
            nowTime = nowTime + 86_400_000L;
            Date date = new Date(nowTime);
            System.out.println(date);
            Transaction transaction = new Transaction();
            transaction.setDate(date);

            for (int i = 0; i < 3; i++) {
//                if(Math.random()*2 > 1) {
                    transaction.setCard_id(i + 1L);
                    if(Math.random()*2>1.2){
                        transaction.setPurpose("");
                        cardService.transferToCard((long) i, (long) (Math.random()*7000), request, response);
                    }else {

                        CategoryAndPurpose temp = list.get((int) (Math.random()*8));
                        transaction.setCategory(temp.getCategory());
                        transaction.setPurpose(temp.getPurpose());

                        transaction.setValue((long) (Math.random()*(-500)));
                        card = cardService.findById(i + 1L);
                        if(card.getAccount()>=transaction.getValue()){
                            cardService.transferFromCard(card.getId(), transaction.getValue());
                            createTransaction(transaction);
                        }
                    }
//                }
            }
        }

    }


    public void createTransaction(Transaction transaction) {
        String sql = "insert into transaction (account_id, card_id, category, purpose, date, time, value) VALUES (?, ?, ?, ?, ?, ?, ?)";

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
            ps.setDate(5, transaction.getDate());
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

}
