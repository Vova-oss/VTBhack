package com.example.securityframe.ResponseModel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FiveGeneralInformation {
    private Long allocated_funds;
    private Long total_balance;
    private Long monthlyExpenses;
    private Long amountOfCards;
    private Long amountOfWorkers;
}
