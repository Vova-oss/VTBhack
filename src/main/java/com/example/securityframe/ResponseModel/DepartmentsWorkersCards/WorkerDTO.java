package com.example.securityframe.ResponseModel.DepartmentsWorkersCards;

import com.example.securityframe.Entity.Card;
import com.example.securityframe.Entity.Worker;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WorkerDTO {

    private Long id;

    private String name;
    private String surname;
    private String patronymic;

    private List<CardDTO> cards;

    public static WorkerDTO createWorkerDTO(Worker worker, List<Card> cards){
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setId(worker.getId());
        workerDTO.setName(worker.getName());
        workerDTO.setSurname(worker.getSurname());
        workerDTO.setPatronymic(worker.getPatronymic());
        workerDTO.setCards(CardDTO.createListCardDTO(cards));
        return workerDTO;
    }


}
