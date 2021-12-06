package com.example.securityframe.Service;

import com.example.securityframe.DAO.LimitHistoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LimitHistoryService {

    @Autowired
    LimitHistoryDAO limitHistoryDAO;



}
