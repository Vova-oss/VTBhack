package com.example.securityframe.Service;

import com.example.securityframe.DAO.DepartmentDAO;
import com.example.securityframe.Entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    @Autowired
    DepartmentDAO departmentDAO;


    public Long findIdOfIndividualByAccountId(Long account_id) {
        Department department = departmentDAO.findByAccountId(account_id);
        return department.getId();
    }

    public Department findByAccountId(Long account_id){
        return departmentDAO.findByAccountId(account_id);
    }
}
