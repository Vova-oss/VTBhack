package com.example.securityframe.Service;

import com.example.securityframe.DAO.CardDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    @Autowired
    CardDAO cardDAO;

}
