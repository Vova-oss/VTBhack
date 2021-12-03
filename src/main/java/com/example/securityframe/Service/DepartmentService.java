package com.example.securityframe.Service;

import com.example.securityframe.DAO.DepartmentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    @Autowired
    DepartmentDAO departmentDAO;



}
