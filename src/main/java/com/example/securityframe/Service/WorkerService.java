package com.example.securityframe.Service;

import com.example.securityframe.DAO.WorkerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerService {

    @Autowired
    WorkerDAO workerDAO;



}
