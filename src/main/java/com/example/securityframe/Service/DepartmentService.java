package com.example.securityframe.Service;

import com.example.securityframe.DAO.DepartmentDAO;
import com.example.securityframe.Entity.Account;
import com.example.securityframe.Entity.Department;
import com.example.securityframe.Entity.Manager;
import com.example.securityframe.Security.SService.JWTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.example.securityframe.Security.SecurityConstants.HEADER_JWT_STRING;
import static com.example.securityframe.Security.SecurityConstants.TOKEN_PREFIX;

@Service
public class DepartmentService {

    @Autowired
    DepartmentDAO departmentDAO;

    @Autowired
    JWTokenService jwTokenService;
    @Autowired
    ManagerService managerService;
    @Autowired
    AccountService accountService;


    public Long findIdOfIndividualByAccountId(Long account_id) {
        Department department = departmentDAO.findByAccountId(account_id);
        return department.getId();
    }

    public Department findByAccountId(Long account_id){
        return departmentDAO.findByAccountId(account_id);
    }

    public List<Department> findAllByAccount_id(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(HEADER_JWT_STRING);
        String token = header.replace(TOKEN_PREFIX, "");
        String email = jwTokenService.getLoginFromJWT(token);
        Manager manager = managerService.findByEmail(email);
        Account account = accountService.findByManagerId(manager.getId());
        return departmentDAO.findAllByAccount_id(account.getId());
    }
}
