package com.example.securityframe.Service;

import com.example.securityframe.DAO.TransactionDAO;
import com.example.securityframe.Entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    TransactionDAO transactionDAO;


    public void createTransaction(Transaction transaction) {
        transactionDAO.createTransaction(transaction);
    }
}
