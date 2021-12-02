package com.example.securityframe.Service;

import com.example.securityframe.AuxiliaryClasses.StaticMethods;
import com.example.securityframe.DAO.ManagerDAO;
import com.example.securityframe.Entity.Manager;
import com.example.securityframe.Security.SService.JWTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.securityframe.Security.SecurityConstants.TOKEN_PREFIX;

@Service
public class ManagerService {

    @Autowired
    ManagerDAO managerDAO;
    @Autowired
    JWTokenService jwTokenService;

    public Manager findByLogin(String login){
        return managerDAO.findByEmail(login);
    }


    public Manager findByJWToken(String tokenWithPrefix, HttpServletRequest request, HttpServletResponse response){
        String token = tokenWithPrefix.replace(TOKEN_PREFIX, "");
        String login = jwTokenService.getLoginFromJWT(token);
        if(login == null){
            StaticMethods.createResponse(request, response, 400, "Incorrect JWToken");
            return null;
        }

        return findByLogin(login);
    }

    public Manager findById(Long manager_id) {
        return managerDAO.findById(manager_id);
    }
}
