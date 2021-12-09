package com.example.securityframe.Controller;

import com.example.securityframe.ResponseModel.HistoryOfTransactions.OneGroupByDate;
import com.example.securityframe.ResponseModel.TopSpendingCategories.TopSpendingCategories;
import com.example.securityframe.Service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.List;

@RestController
@Api(tags = "Transaction")
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @ApiOperation(value = "Получение истории транзакций")
    @GetMapping("/transactionHistory")
    public List<OneGroupByDate> transactionHistory(
            @ApiParam(
                    name = "from",
                    value = "Период от (включительно)",
                    example = "2021-11-29"
            )
            @RequestParam(value = "from", required = false) Date from,
            @ApiParam(
                    name = "to",
                    value = "Период до (включительно)",
                    example = "2021-12-08"
            )
            @RequestParam(value = "to", required = false) Date to,
            @ApiParam(
                    name = "refillOrExpenses",
                    value = "Пополнение или траты. refill - пополнение, expenses - траты, ничего - всё вместе. Это 'Тип' в фильтре",
                    example = "refill"
            )
            @RequestParam(value = "refillOrExpenses", required = false) String refillOrExpenses,
            @ApiParam(
                    name = "purpose",
                    value = "Тип карты (грубо говоря). Синие слова на макете. Это 'Назначение' в фильтре",
                    example = "Транспорт"
            )
            @RequestParam(value = "purpose", required = false) String purpose,
            @ApiParam(
                    name = "whatWasSpentOn",
                    value = "На что было потрачено. Это 'Поиск' в фильтре",
                    example = "Apple.com"
            )
            @RequestParam(value = "whatWasSpentOn", required = false) String whatWasSpentOn,
            @ApiParam(
                    name = "page",
                    value = "Страница отображение, по умолчанию = 0. Хочешь увидеть следующую - пиши 1 и тд",
                    example = "Транспорт"
            )
            @RequestParam(value = "page", required = false, defaultValue = "0") String page,
            HttpServletRequest request,
            HttpServletResponse response){

        return transactionService.transactionHistory(from, to, refillOrExpenses, purpose, whatWasSpentOn, page, request, response);
    }

    @ApiOperation(value = "Получение истории транзакций для конкретного сотрудника")
    @GetMapping("/transactionHistoryByWorkerId")
    public List<OneGroupByDate> transactionHistoryByWorkerId(
            @ApiParam(
                    name = "from",
                    value = "Период от (включительно)",
                    example = "2021-11-29"
            )
            @RequestParam(value = "from", required = false) Date from,
            @ApiParam(
                    name = "to",
                    value = "Период до (включительно)",
                    example = "2021-12-08"
            )
            @RequestParam(value = "to", required = false) Date to,
            @ApiParam(
                    name = "refillOrExpenses",
                    value = "Пополнение или траты. refill - пополнение, expenses - траты, ничего - всё вместе. Это 'Тип' в фильтре",
                    example = "refill"
            )
            @RequestParam(value = "refillOrExpenses", required = false) String refillOrExpenses,
            @ApiParam(
                    name = "purpose",
                    value = "Тип карты (грубо говоря). Синие слова на макете. Это 'Назначение' в фильтре",
                    example = "Транспорт"
            )
            @RequestParam(value = "purpose", required = false) String purpose,
            @ApiParam(
                    name = "whatWasSpentOn",
                    value = "На что было потрачено. Это 'Поиск' в фильтре",
                    example = "Apple.com"
            )
            @RequestParam(value = "whatWasSpentOn", required = false) String whatWasSpentOn,
            @ApiParam(
                    name = "page",
                    value = "Страница отображение, по умолчанию = 0. Хочешь увидеть следующую - пиши 1 и тд",
                    example = "Транспорт"
            )
            @RequestParam(value = "page", required = false, defaultValue = "0") String page,
            @ApiParam(
                    name = "worker_id",
                    value = ":id сотрудника, по которому ищется инфа",
                    example = "Транспорт"
            )
            @RequestParam(value = "worker_id") Long worker_id,
            HttpServletRequest request,
            HttpServletResponse response){

        return transactionService.transactionHistoryByWorkerId(from, to, refillOrExpenses, purpose, whatWasSpentOn, page, worker_id, request, response);
    }

    @ApiOperation(value = "Получение топ категорий трат")
    @GetMapping("/topSpendingCategories")
    public TopSpendingCategories topSpendingCategories(
            @ApiParam(
                    name = "from",
                    value = "Период от (включительно)",
                    example = "2021-11-29"
            )
            @RequestParam(value = "from", required = false) Date from,
            @ApiParam(
                    name = "to",
                    value = "Период до (включительно)",
                    example = "2021-12-08"
            )
            @RequestParam(value = "to", required = false) Date to,
            @ApiParam(
                    name = "purpose",
                    value = "Тип карты (грубо говоря). Синие слова на макете. Это 'Назначение' в фильтре",
                    example = "Транспорт"
            )
            @RequestParam(value = "purpose", required = false) String purpose,
            @ApiParam(
                    name = "whatWasSpentOn",
                    value = "На что было потрачено. Это 'Поиск' в фильтре",
                    example = "Apple.com"
            )
            @RequestParam(value = "whatWasSpentOn", required = false) String whatWasSpentOn,
            HttpServletRequest request,
            HttpServletResponse response){
        return transactionService.topSpendingCategories(from, to, purpose, whatWasSpentOn, request, response);
    }

}
