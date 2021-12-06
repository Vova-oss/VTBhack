package com.example.securityframe.Service;

import com.example.securityframe.DAO.LimitHistoryDAO;
import com.example.securityframe.Entity.LimitHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LimitHistoryService {

    @Autowired
    private LimitHistoryDAO limitHistoryDAO;


    public void createLimitHistory(Long card_id, Long limit, Long term) {
        LimitHistory limitHistory = new LimitHistory();
        limitHistory.setCard_id(card_id);
        limitHistory.setLimit(limit);
        limitHistory.setTerm(term);
        limitHistoryDAO.deleteByCardId(card_id);
        limitHistoryDAO.add(limitHistory);
    }
}
