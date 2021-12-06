package com.example.securityframe.Controller;

import com.example.securityframe.AuxiliaryClasses.StaticMethods;
import com.example.securityframe.Entity.Card;
import com.example.securityframe.ResponseModel.WorkerInfo;
import com.example.securityframe.Service.CardService;
import com.example.securityframe.Service.WorkerService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
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
        List<Card> cards = cardService.findAllByWorkerId(worker_id);
        cards.sort(Comparator.comparing(Card::getCard_number));
        return cards;
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

    @ApiOperation(value = "Перевод со счёта на карту")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Incorrect JSON\nInsufficient funds\nCard with this :id doesn't exists")
    })
    @PostMapping("/transferToCard")
    public void transferToCard(
            @ApiParam(
                    name = "card_id",
                    value = "card_id - :id карты, на которую хотим совершить перевод",
                    example = "1",
                    required = true
            )
            @RequestParam(name = "card_id") Long card_id,
            @ApiParam(
                    name = "amount",
                    value = "amount - сумма, на которую хотим совершить перевод",
                    example = "5000",
                    required = true
            )
            @RequestParam(name = "amount") Long amount,
            HttpServletRequest request,
            HttpServletResponse response){
        cardService.transferToCard(card_id, amount, request, response);
        StaticMethods.createResponse(request, response, 201, "Created");
    }

    @ApiOperation(value = "Вечная блокировка карты")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Created")
    })
    @PutMapping("/perpetualCardBlocking")
    public void perpetualCardBlocking(
            @ApiParam(
                    name = "card_id",
                    value = "card_id - :id карты, на которую хотим заблокировать",
                    example = "1",
                    required = true
            )
            @RequestParam("card_id") Long card_id,
            HttpServletRequest request,
            HttpServletResponse response){
        cardService.perpetualCardBlocking(card_id);
        StaticMethods.createResponse(request, response, 201, "Created");
    }

    @ApiOperation(value = "Временная блокировка/разблокировка карты")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Incorrect JSON\nThe card is blocked forever. You can't unlock it")
    })
    @PutMapping("/lockUnlockCard")
    public void lockUnlockCard(
            @ApiParam(
                    name = "card_id",
                    value = "card_id - :id карты, на которую хотим заблокировать/разблокировать",
                    example = "1",
                    required = true
            )
            @RequestParam("card_id") Long card_id,
            HttpServletRequest request,
            HttpServletResponse response){
        cardService.lockUnlockCard(card_id, request, response);
        StaticMethods.createResponse(request, response, 201, "Created");
    }

    @ApiOperation(value = "Установка финансового лимита на карту")
    @PutMapping("/setLimitOnCard")
    public void setLimitOnCard(
            @RequestParam("card_id") Long card_id,
            @RequestParam("limit") Long limit,
            @RequestParam("term") Long term,
            @RequestParam("autoUpdate") Boolean autoUpdate,
            HttpServletRequest request,
            HttpServletResponse response){
        cardService.setLimitOnCard(card_id, limit, term, autoUpdate);
        StaticMethods.createResponse(request, response, 201, "Created");
    }

}
