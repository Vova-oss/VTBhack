package com.example.securityframe.Controller;

import com.example.securityframe.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("/transactionHistory")
    public void transactionHistory(
            @RequestParam(value = "from", required = false) Date from,
            @RequestParam(value = "to", required = false) Date to,
            @RequestParam(value = "refillOrExpenses", required = false) String refillOrExpenses,
            @RequestParam(value = "purpose", required = false) String purpose,
            @RequestParam(value = "whatWasSpentOn", required = false) String whatWasSpentOn,
            @RequestParam(value = "page", required = false, defaultValue = "0") String page,
            HttpServletRequest request,
            HttpServletResponse response){

        transactionService.transactionHistory(from, to, refillOrExpenses, purpose, whatWasSpentOn, page, request, response);

    }

}
