package com.example.securityframe.Controller;

import com.example.securityframe.Entity.Card;
import com.example.securityframe.ResponseModel.WorkerInfo;
import com.example.securityframe.Service.CardService;
import com.example.securityframe.Service.WorkerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import liquibase.pro.packaged.S;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Api(tags = "Worker")
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    CardService cardService;
    @Autowired
    WorkerService workerService;

    @ApiOperation(value = "Получение данных о картах сотрудника")
    @GetMapping("/getInfoOfCardsByWorkerId")
    public List<Card> getInfoOfCardsByWorkerId(
            @ApiParam(
                    name = "WorkerId",
                    value = "worker_id - :id сотрудника, по картам которого хотим получить инфу",
                    example = "3",
                    required = true
            )
            @RequestParam("worker_id") Long worker_id){
        return cardService.findAllByWorkerId(worker_id);
    }

    @ApiOperation(value = "Получение данных о сотруднике")
    @GetMapping("/getWorkerInfo")
    public WorkerInfo getWorkerInfo(
            @ApiParam(
                    name = "WorkerId",
                    value = "worker_id - :id сотрудника, по которому хотим получить инфу",
                    example = "3",
                    required = true
            )
            @RequestParam("worker_id") Long worker_id){
        return workerService.getWorkerInfo(worker_id);
    }


}
