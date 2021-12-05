package com.example.securityframe.ResponseModel.DepartmentsWorkersCards;

import com.example.securityframe.Entity.Card;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CardDTO {

    private Long id;
    private String status;
    private String payment_system;
    private String last_figures;
    private Long account;
    private String type;

    public static CardDTO createCardDTO(Card card){
        CardDTO cardDTO = new CardDTO();
        cardDTO.setId(card.getId());
        cardDTO.setStatus(card.getStatus());
        cardDTO.setPayment_system(card.getPayment_system());
        cardDTO.setLast_figures(card.getCard_number().substring(11, 16));
        cardDTO.setAccount(card.getAccount());
        cardDTO.setType(card.getType());
        return cardDTO;
    }

    public static List<CardDTO> createListCardDTO(List<Card> cards){
        List<CardDTO> list = new ArrayList<>();
        for(Card card: cards)
            list.add(createCardDTO(card));

        return list;
    }

}
