package com.example.securityframe.Controller;

import com.example.securityframe.DAO.ManagerDAO;
import com.example.securityframe.DAO.RoleDAO;
import com.example.securityframe.ResponseModel.WidgetCurrentAccount;
import com.example.securityframe.Service.ManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(tags = "Start")
@RequestMapping("/start")
public class StartController {

    @Autowired
    ManagerService managerService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @ApiOperation(value = "Получение данных для виджета 'Текущий счёт'")
    @GetMapping("/currentAccount")
    public WidgetCurrentAccount currentAccount(HttpServletRequest request, HttpServletResponse response){
        return managerService.getInfoAboutWidgetCurrentAccount(request, response);
    }


}
