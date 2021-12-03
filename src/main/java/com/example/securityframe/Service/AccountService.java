package com.example.securityframe.Service;

import com.example.securityframe.DAO.AccountDAO;
import com.example.securityframe.Entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    AccountDAO accountDAO;

    public Account findByManagerId(Long id) {
        return accountDAO.findByManagerId(id);
    }
}
