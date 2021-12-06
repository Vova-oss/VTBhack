package com.example.securityframe.Service;

import com.example.securityframe.DAO.AccountDAO;
import com.example.securityframe.Entity.Account;
import com.example.securityframe.Entity.Manager;
import com.example.securityframe.Entity.Transaction;
import com.example.securityframe.Security.SService.JWTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.example.securityframe.Security.SecurityConstants.HEADER_JWT_STRING;
import static com.example.securityframe.Security.SecurityConstants.TOKEN_PREFIX;

@Service
public class AccountService {

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    JWTokenService jwTokenService;
    @Autowired
    ManagerService managerService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;

    public Account findByManagerId(Long id) {
        return accountDAO.findByManagerId(id);
    }

    public Account findByJwt(HttpServletRequest request){
        String header = request.getHeader(HEADER_JWT_STRING);
        String token = header.replace(TOKEN_PREFIX, "");
        String email = jwTokenService.getLoginFromJWT(token);
        Manager manager = managerService.findByEmail(email);
        return accountService.findByManagerId(manager.getId());
    }

    public boolean withdrawalOfFunds(Long account_id, Long amount) {
        if(accountDAO.withdrawalOfFunds(account_id, amount)){
            Transaction transaction = new Transaction();
            transaction.setAccount_id(account_id);
            transaction.setCategory("Переводы");
            transaction.setValue(-amount);
            transactionService.createTransaction(transaction);
            return true;
        }
        return false;
    }
}
