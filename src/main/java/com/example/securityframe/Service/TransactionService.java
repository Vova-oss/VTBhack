package com.example.securityframe.Service;

import com.example.securityframe.DAO.TransactionDAO;
import com.example.securityframe.Entity.Account;
import com.example.securityframe.Entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;

@Service
public class TransactionService {

    @Autowired
    TransactionDAO transactionDAO;
    @Autowired
    AccountService accountService;


    public void createTransaction(Transaction transaction) {
        transactionDAO.createTransaction(transaction);
    }

    public void transactionHistory(
            Date from, Date to, String refillOrExpenses, String purpose, String whatWasSpentOn, String page,
            HttpServletRequest request, HttpServletResponse response) {
        Account account = accountService.findByJwt(request);

        transactionDAO.transactionHistory();
    }
}
