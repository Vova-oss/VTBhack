package com.example.securityframe.ResponseModel.DepartmentsWorkersCards;

import com.example.securityframe.Entity.Card;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class CardDTO {

    private Long id;
    private String status;
    private String payment_system;
    private String card_number;
    private Long account;
    private String type;

    public static CardDTO createCardDTO(Card card){
        CardDTO cardDTO = new CardDTO();
        cardDTO.setId(card.getId());
        cardDTO.setStatus(card.getStatus());
        cardDTO.setPayment_system(card.getPayment_system());
        cardDTO.setCard_number(card.getCard_number());
        cardDTO.setAccount(card.getAccount());
        cardDTO.setType(card.getType());
        return cardDTO;
    }

    public static List<CardDTO> createListCardDTO(List<Card> cards){
        List<CardDTO> list = new ArrayList<>();
        for(Card card: cards)
            list.add(createCardDTO(card));

        list.sort(Comparator.comparing(CardDTO::getCard_number));
        return list;
    }

}
