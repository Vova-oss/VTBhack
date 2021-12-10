package com.example.securityframe.Service;

import com.example.securityframe.DAO.TransactionDAO;
import com.example.securityframe.Entity.Account;
import com.example.securityframe.Entity.Transaction;
import com.example.securityframe.ResponseModel.ExpenseSchedule.OneDay;
import com.example.securityframe.ResponseModel.HistoryOfTransactions.OneEntry;
import com.example.securityframe.ResponseModel.HistoryOfTransactions.OneGroupByDate;
import com.example.securityframe.ResponseModel.TopSpendingCategories.OneCategory;
import com.example.securityframe.ResponseModel.TopSpendingCategories.TopSpendingCategories;
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

        Long valueFrom = Long.MIN_VALUE;
        Long valueTo = Long.MAX_VALUE;

        if(from == null && to == null)
            from = new Date(System.currentTimeMillis() - 86_400_000 * 7);
        else if (from == null)
            from = new Date(to.getTime() - 86_400_000 * 7);

        if(to == null)
            to = new Date(System.currentTimeMillis());

        if(purpose == null)
            purpose = "%";

        if(whatWasSpentOn == null)
            whatWasSpentOn = "%";
        else whatWasSpentOn = "%" + whatWasSpentOn + "%";

        if(refillOrExpenses != null) {
            switch (refillOrExpenses){
                case "refill": valueFrom = 0L; break;
                case "expenses": valueTo = 0L; break;
            }
        }



//        if(from != null)
//            where+="\nand date >= '" + from + "'";
//
//        if(to != null)
//            where+="\nand date <= '" + to +"'";
//        else where+="\nand date + time <= now()";

//        if(refillOrExpenses != null && refillOrExpenses.equals("expenses"))
//            where+="\nand value < 0";
//        else if(refillOrExpenses != null && refillOrExpenses.equals("refill"))
//            where+="\nand value > 0";

//        if(purpose != null)
//            where+="\nand purpose = '" + purpose + "'";
//
//        if(whatWasSpentOn != null)
//            where+="\nand category like '%" + whatWasSpentOn + "%'";

        List<OneEntry> list = transactionDAO.transactionHistory(
                from, to, purpose, whatWasSpentOn, valueFrom, valueTo, page, account.getId());

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
            where+="\nand type = '" + purpose + "'";

        if(whatWasSpentOn != null)
            where+="\nand purpose like '%" + whatWasSpentOn + "%'";

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

    public TopSpendingCategories topSpendingCategories(Date from, Date to, String purpose,
                                      String whatWasSpentOn, HttpServletRequest request,
                                      HttpServletResponse response) {

        Account account = accountService.findByJwt(request);

//        String where = "";

        if(from == null && to == null)
            from = new Date(System.currentTimeMillis() - 86_400_000 * 7);
        else if (from == null)
            from = new Date(to.getTime() - 86_400_000 * 7);
        if(to == null)
            to = new Date(System.currentTimeMillis());
        if(purpose == null)
            purpose = "%";
        if(whatWasSpentOn == null)
            whatWasSpentOn = "%";
        else whatWasSpentOn = "%" + whatWasSpentOn + "%";


//        if(from != null)
//            where+="\nand date >= '" + from + "'";
//
//        if(to != null)
//            where+="\nand date <= '" + to +"'";
//        else where+="\nand date + time <= now()";
//
//        if(purpose != null)
//            where+="\nand type = '" + purpose + "'";
//
//        if(whatWasSpentOn != null)
//            where+="\nand purpose like '%" + whatWasSpentOn + "%'";

        List<OneCategory> list = transactionDAO.topSpendingCategories(from, to, purpose, whatWasSpentOn, account.getId());

        Long sum = 0L;
        for(OneCategory oneCategory: list){
            sum += Long.parseLong(oneCategory.getSum());
        }
        TopSpendingCategories topSpendingCategories = new TopSpendingCategories();
        topSpendingCategories.setMaxSum(String.valueOf(sum));
        topSpendingCategories.setList(list);
        return topSpendingCategories;
    }

    public TopSpendingCategories topSpendingCategoriesByWorker(
            Date from, Date to, String purpose, String whatWasSpentOn, Long worker_id,
            HttpServletRequest request, HttpServletResponse response) {

        if(from == null && to == null)
            from = new Date(System.currentTimeMillis() - 86_400_000 * 7);
        else if (from == null)
            from = new Date(to.getTime() - 86_400_000 * 7);
        if(to == null)
            to = new Date(System.currentTimeMillis());
        if(purpose == null)
            purpose = "%";
        if(whatWasSpentOn == null)
            whatWasSpentOn = "%";
        else whatWasSpentOn = "%" + whatWasSpentOn + "%";


        List<OneCategory> list = transactionDAO.topSpendingCategoriesByWorker(from, to, purpose, whatWasSpentOn, worker_id);

        Long sum = 0L;
        for(OneCategory oneCategory: list){
            sum += Long.parseLong(oneCategory.getSum());
        }
        TopSpendingCategories topSpendingCategories = new TopSpendingCategories();
        topSpendingCategories.setMaxSum(String.valueOf(sum));
        topSpendingCategories.setList(list);
        return topSpendingCategories;

    }

    public Long findMonthlyExpensesByAccountId(Long id) {
        return transactionDAO.findMonthlyExpensesByAccountId(id);
    }

    public List<OneDay> expenseSchedule(Date from, Date to, String purpose, String whatWasSpentOn,
                                                 HttpServletRequest request, HttpServletResponse response) {

        Account account = accountService.findByJwt(request);

//        String where = "";

        if(from == null && to == null)
            from = new Date(System.currentTimeMillis() - 86_400_000 * 7);
        else if (from == null)
            from = new Date(to.getTime() - 86_400_000 * 7);
        if(to == null)
            to = new Date(System.currentTimeMillis());
        if(purpose == null)
            purpose = "%";
        if(whatWasSpentOn == null)
            whatWasSpentOn = "%";
        else whatWasSpentOn = "%" + whatWasSpentOn + "%";

//        if(from != null)
//            where+="\nand date >= '" + from + "'";
//        else if(to!=null)
//            where+="\nand date >= date('"+to+"')-integer'7'";
//        else where+="\nand date >= date(now())-integer'7'";
//
//        if(to != null)
//            where+="\nand date <= '" + to +"'";
//        else where+="\nand date + time <= now()";
//
//        if(purpose != null)
//            where+="\nand type = '" + purpose + "'";
//
//        if(whatWasSpentOn != null)
//            where+="\nand purpose like '%" + whatWasSpentOn + "%'";

        List<OneDay> list = transactionDAO.expenseSchedule(from, to, purpose, whatWasSpentOn, account.getId());

        return list;

    }

    public List<OneDay> expenseScheduleByWorker(Date from, Date to, String purpose, String whatWasSpentOn, Long worker_id, HttpServletRequest request, HttpServletResponse response) {

        if(from == null && to == null)
            from = new Date(System.currentTimeMillis() - 86_400_000 * 7);
        else if (from == null)
            from = new Date(to.getTime() - 86_400_000 * 7);
        if(to == null)
            to = new Date(System.currentTimeMillis());
        if(purpose == null)
            purpose = "%";
        if(whatWasSpentOn == null)
            whatWasSpentOn = "%";
        else whatWasSpentOn = "%" + whatWasSpentOn + "%";

        List<OneDay> list = transactionDAO.expenseScheduleByWorker(from, to, purpose, whatWasSpentOn, worker_id);

        return list;

    }
}
