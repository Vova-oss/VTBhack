package com.example.securityframe.Service;

import com.example.securityframe.DAO.WorkerDAO;
import com.example.securityframe.Entity.Account;
import com.example.securityframe.Entity.Manager;
import com.example.securityframe.Entity.Worker;
import com.example.securityframe.Security.SService.JWTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.securityframe.Security.SecurityConstants.HEADER_JWT_STRING;
import static com.example.securityframe.Security.SecurityConstants.TOKEN_PREFIX;

@Service
public class WorkerService {

    @Autowired
    WorkerDAO workerDAO;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    JWTokenService jwTokenService;
    @Autowired
    ManagerService managerService;
    @Autowired
    AccountService accountService;


    public void addWorker(Worker worker, HttpServletRequest request, HttpServletResponse response) {
        if (worker.getDepartment_id() == -1){
            String header = request.getHeader(HEADER_JWT_STRING);
            String token = header.replace(TOKEN_PREFIX, "");
            String email = jwTokenService.getLoginFromJWT(token);
            Manager manager = managerService.findByEmail(email);
            Account account = accountService.findByManagerId(manager.getId());
            Long individualDepartment_id = departmentService.findIdOfIndividualByAccountId(account.getId());
            worker.setDepartment_id(individualDepartment_id);
        }
        workerDAO.addWorker(worker);
    }


}
