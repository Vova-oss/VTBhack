package com.example.securityframe.Service;

import com.example.securityframe.AuxiliaryClasses.StaticMethods;
import com.example.securityframe.DAO.WorkerDAO;
import com.example.securityframe.Entity.*;
import com.example.securityframe.ResponseModel.WorkerInfo;
import com.example.securityframe.Security.SService.JWTokenService;
import liquibase.pro.packaged.W;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static com.example.securityframe.Security.SecurityConstants.HEADER_JWT_STRING;
import static com.example.securityframe.Security.SecurityConstants.TOKEN_PREFIX;

@Service
public class WorkerService {

    @Autowired
    private WorkerDAO workerDAO;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private CardService cardService;
    @Autowired
    private AccountService accountService;


    public void addWorker(Worker worker, HttpServletRequest request, HttpServletResponse response) {
        if (worker.getDepartment_id() == -1){
            Account account = accountService.findByJwt(request);
            Long individualDepartment_id = departmentService.findIdOfIndividualByAccountId(account.getId());
            worker.setDepartment_id(individualDepartment_id);
        }
        workerDAO.addWorker(worker);
    }


    public List<Worker> findAllByAccount_id(HttpServletRequest request, HttpServletResponse response) {
        Account account = accountService.findByJwt(request);
        return workerDAO.findAllByAccount_id(account.getId());
    }

    public void replaceDepartmentId(String id, Long department_id) {
        workerDAO.replaceDepartmentId(id, department_id);
    }

    public void updateDepartmentOfWorker(String body, HttpServletRequest request, HttpServletResponse response) {
        String id = StaticMethods.parsingJson(body, "id", request, response);
        String department_id = StaticMethods.parsingJson(body, "department_id", request, response);
        if(id == null || department_id == null)
            return;

        replaceDepartmentId(id, Long.valueOf(department_id));
    }

    public Worker findById(String id_worker) {
        return workerDAO.findById(id_worker);
    }

    public List<Worker> findAllByDepartmentId(Long department_id) {
        return workerDAO.findAllByDepartmentId(department_id);
    }

    public WorkerInfo getWorkerInfo(Long worker_id) {
        Worker worker = workerDAO.findById(String.valueOf(worker_id));
        Department department = departmentService.findById(worker.getDepartment_id());
        Long account = cardService.findAccountByWorkerId(worker_id);
        WorkerInfo workerInfo = new WorkerInfo();
        workerInfo.setName(worker.getName());
        workerInfo.setSurname(worker.getSurname());
        workerInfo.setPatronymic(worker.getPatronymic());
        workerInfo.setDepartmentType(department.getName());
        workerInfo.setAccount(account);
        return workerInfo;
    }

    public Long amountOfWorkersByAccountId(Long account_id) {
        return workerDAO.amountOfWorkersByAccountId(account_id);
    }
}
