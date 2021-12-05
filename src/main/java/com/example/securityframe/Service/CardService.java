package com.example.securityframe.Service;

import com.example.securityframe.AuxiliaryClasses.StaticMethods;
import com.example.securityframe.DAO.CardDAO;
import com.example.securityframe.Entity.Card;
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
        } while (cardDAO.cardNumberExists(String.valueOf(v)));
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
}
