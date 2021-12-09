package com.example.securityframe.Service;

import com.example.securityframe.AuxiliaryClasses.StaticMethods;
import com.example.securityframe.DAO.ManagerDAO;
import com.example.securityframe.Entity.Account;
import com.example.securityframe.Entity.Manager;
import com.example.securityframe.ResponseModel.FiveGeneralInformation;
import com.example.securityframe.ResponseModel.WidgetCurrentAccount;
import com.example.securityframe.Security.SService.JWTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.securityframe.Security.SecurityConstants.HEADER_JWT_STRING;
import static com.example.securityframe.Security.SecurityConstants.TOKEN_PREFIX;

@Service
public class ManagerService {

    @Autowired
    private ManagerDAO managerDAO;
    @Autowired
    private AccountService accountService;
    @Autowired
    private JWTokenService jwTokenService;
    @Autowired
    private CardService cardService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private WorkerService workerService;

    public Manager findByEmail(String email){
        return managerDAO.findByEmail(email);
    }


    public Manager findByJWToken(String tokenWithPrefix, HttpServletRequest request, HttpServletResponse response){
        String token = tokenWithPrefix.replace(TOKEN_PREFIX, "");
        String login = jwTokenService.getLoginFromJWT(token);
        if(login == null){
            StaticMethods.createResponse(request, response, 400, "Incorrect JWToken");
            return null;
        }

        return findByEmail(login);
    }

    public Manager findById(Long manager_id) {
        return managerDAO.findById(manager_id);
    }

    public WidgetCurrentAccount getInfoAboutWidgetCurrentAccount(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(HEADER_JWT_STRING);
        String token = header.replace(TOKEN_PREFIX, "");
        String email = jwTokenService.getLoginFromJWT(token);

        Manager manager = findByEmail(email);
        Account account = accountService.findByManagerId(manager.getId());
        WidgetCurrentAccount widgetCurrentAccount = new WidgetCurrentAccount();
        widgetCurrentAccount.setCurrent_account(account.getCurrent_account());
        widgetCurrentAccount.setAccount_number(account.getAccount_number());
        widgetCurrentAccount.setCurrency(account.getCurrency());
        return widgetCurrentAccount;

    }

    public FiveGeneralInformation fiveGeneralInformation(HttpServletRequest request) {

        Account account = accountService.findByJwt(request);
        Long current_account = account.getCurrent_account();

        // Выделено средств
        Long allocated_funds = cardService.findAccountByAccountId(account.getId());
        // Общий баланс
        Long total_balance = account.getCurrent_account() + allocated_funds;
        // Траты за месяц
        Long monthlyExpenses = transactionService.findMonthlyExpensesByAccountId(account.getId());
        // Активных карт
        Long amountOfCards = cardService.amountOfActiveCardsByAccountId(account.getId());
        // Сотрудников
        Long amountOfWorkers = workerService.amountOfWorkersByAccountId(account.getId());

        FiveGeneralInformation fiveGeneralInformation = new FiveGeneralInformation(
                allocated_funds, total_balance, monthlyExpenses, amountOfCards, amountOfWorkers);

        return fiveGeneralInformation;
    }
}
