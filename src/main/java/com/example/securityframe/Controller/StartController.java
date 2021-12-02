package com.example.securityframe.Controller;

import com.example.securityframe.DAO.ManagerDAO;
import com.example.securityframe.DAO.RoleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StartController {

    @Autowired
    RoleDAO roleDAO;
    @Autowired
    ManagerDAO managerDAO;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/home")
    public String homePage(){
        return "Hello";
    }

    @GetMapping("/work")
    public String workPage(){
        return "Hello on work";
    }

}
