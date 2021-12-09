package com.example.securityframe.Service;

import com.example.securityframe.DAO.TransactionDAO;
import com.example.securityframe.Entity.Account;
import com.example.securityframe.Entity.Transaction;
import com.example.securityframe.ResponseModel.HistoryOfTransactions.OneEntry;
import com.example.securityframe.ResponseModel.HistoryOfTransactions.OneGroupByDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionDAO transactionDAO;
    @Autowired
    AccountService accountService;


    public void createTransaction(Transaction transaction) {
        transactionDAO.createTransaction(transaction);
    }

    public List<OneGroupByDate> transactionHistory(
            Date from, Date to, String refillOrExpenses, String purpose, String whatWasSpentOn, String page,
            HttpServletRequest request, HttpServletResponse response) {
        Account account = accountService.findByJwt(request);

        String where = "";
        String common_where = "";

        if(from != null)
            where+="\nand date >= '" + from + "'";

        if(to != null)
            where+="\nand date <= '" + to +"'";
        else where+="\nand date + time <= now()";

        if(refillOrExpenses != null && refillOrExpenses.equals("expenses"))
            where+="\nand value < 0";
        else if(refillOrExpenses != null && refillOrExpenses.equals("refill"))
            where+="\nand value > 0";

        if(purpose != null)
            common_where+="\nwhere type = '" + purpose + "'";

        if(whatWasSpentOn != null)
            where+="\nand category like '" + whatWasSpentOn + "'";

        List<OneEntry> list = transactionDAO.transactionHistory(where, common_where, page, account.getId());
        List<OneGroupByDate> finallyList = new ArrayList<>();
        OneGroupByDate oneGroupByDate = null;

        String date = "";
        for(OneEntry oneEntry: list){
            if(!oneEntry.getDate().equals(date)){
                if(!date.equals(""))
                    finallyList.add(oneGroupByDate);
                date = oneEntry.getDate();
                oneGroupByDate = new OneGroupByDate();
                oneGroupByDate.setGroup(new ArrayList<>());
                oneGroupByDate.setDate(date);
            }
            oneGroupByDate.getGroup().add(oneEntry);
        }
        finallyList.add(oneGroupByDate);

        return finallyList;
    }

    public List<OneGroupByDate> transactionHistoryByWorkerId(
            Date from, Date to, String refillOrExpenses, String purpose, String whatWasSpentOn, String page,
            Long worker_id, HttpServletRequest request, HttpServletResponse response) {

        String where = "";

        if(from != null)
            where+="\nand date >= '" + from + "'";

        if(to != null)
            where+="\nand date <= '" + to +"'";
        else where+="\nand date + time <= now()";

        if(refillOrExpenses != null && refillOrExpenses.equals("expenses"))
            where+="\nand value < 0";
        else if(refillOrExpenses != null && refillOrExpenses.equals("refill"))
            where+="\nand value > 0";

        if(purpose != null)
            where+="\nand c.type = '" + purpose + "'";

        if(whatWasSpentOn != null)
            where+="\nand category like '" + whatWasSpentOn + "'";

        List<OneEntry> list = transactionDAO.transactionHistoryByWorkerId(where, page, worker_id);
        List<OneGroupByDate> finallyList = new ArrayList<>();
        OneGroupByDate oneGroupByDate = null;

        String date = "";
        for(OneEntry oneEntry: list){
            if(!oneEntry.getDate().equals(date)){
                if(!date.equals(""))
                    finallyList.add(oneGroupByDate);
                date = oneEntry.getDate();
                oneGroupByDate = new OneGroupByDate();
                oneGroupByDate.setGroup(new ArrayList<>());
                oneGroupByDate.setDate(date);
            }
            oneGroupByDate.getGroup().add(oneEntry);
        }
        finallyList.add(oneGroupByDate);

        return finallyList;

    }
}
