package com.example.securityframe.Service;

import com.example.securityframe.AuxiliaryClasses.StaticMethods;
import com.example.securityframe.DAO.CardDAO;
import com.example.securityframe.Entity.Account;
import com.example.securityframe.Entity.Card;
import com.example.securityframe.Entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class CardService {

    @Autowired
    private CardDAO cardDAO;

    @Autowired
    private WorkerService workerService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private LimitHistoryService limitHistoryService;

    public void addCard(String body, HttpServletRequest request, HttpServletResponse response) {
        String id_worker = StaticMethods.parsingJson(body, "id_worker", request, response);
        String type = StaticMethods.parsingJson(body, "type", request, response);
        String purpose_of_creation = StaticMethods.parsingJson(body, "purpose_of_creation", request, response);
        if(id_worker == null || type == null || purpose_of_creation == null)
            return;

        if(workerService.findById(id_worker) == null){
            StaticMethods.createResponse(request, response, 400, "Worker with this :id doesn't exists");
        }

        Card card = new Card();
        card.setWorker_id(Long.valueOf(id_worker));
        card.setPayment_system("VISA");

        long v;
        do {
            v = (long) (Math.random() * 1_0000_0000_0000_0000L);
        } while (cardDAO.cardNumberExists(String.valueOf(v)) || v < 1_000_000_000_000_000L);
        card.setCard_number(String.valueOf(v).substring(0, 16));

        card.setAccount(0L);
        card.setType(type);
        card.setPurpose_of_creation(purpose_of_creation);
        card.setStatus("ACTIVE");
        card.setCurrency("RUB");
        cardDAO.add(card);
    }

    public List<Card> findAllByWorkerId(Long worker_id) {
        return cardDAO.findAllByWorkerId(worker_id);
    }


    public Long findAccountByWorkerId(Long worker_id) {
        return cardDAO.findAccountByWorkerId(worker_id);
    }

    public void transferToCard(Long card_id, Long amount, HttpServletRequest request, HttpServletResponse response) {
        Account account = accountService.findByJwt(request);
        if(account.getCurrent_account() < amount) {
            StaticMethods.createResponse(request, response, 400, "Insufficient funds");
            return;
        }

        if(cardDAO.findById(card_id) == null){
            StaticMethods.createResponse(request, response, 400, "Card with this :id doesn't exists");
            return;
        }

        if(accountService.withdrawalOfFunds(account.getId(), amount)){
            this.topUpAccount(card_id, amount);
        }
    }

    private void topUpAccount(Long card_id, Long amount) {
        cardDAO.topUpAccount(card_id, amount);
        Transaction transaction = new Transaction();
        transaction.setCard_id(card_id);
        transaction.setCategory("Переводы");
        transaction.setValue(amount);
        transactionService.createTransaction(transaction);
    }

    public void perpetualCardBlocking(Long card_id) {
        cardDAO.changeStatusOfCard(card_id, "BLOCKING");
    }

    public void lockUnlockCard(Long card_id, HttpServletRequest request, HttpServletResponse response) {
        Card card = cardDAO.findById(card_id);
        switch (card.getStatus()) {
            case "ACTIVE":
                cardDAO.changeStatusOfCard(card_id, "TEMPORARY BLOCKING");
                break;
            case "TEMPORARY BLOCKING":
                cardDAO.changeStatusOfCard(card_id, "ACTIVE");
                break;
            case "BLOCKING":
                StaticMethods.createResponse(request, response, 400, "The card is blocked forever. You can't unlock it");
                break;
        }
    }

    public void setLimitOnCard(Long card_id, Long limit, Long term, Boolean autoUpdate) {

        cardDAO.updateLimitById(card_id, limit, term, autoUpdate);
        if(autoUpdate)
            limitHistoryService.createLimitHistory(card_id, limit, term);



    }

    public List<Card> findAll() {
        return cardDAO.findAll();
    }
}
