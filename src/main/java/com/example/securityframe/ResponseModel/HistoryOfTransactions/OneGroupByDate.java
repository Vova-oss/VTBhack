package com.example.securityframe.ResponseModel.HistoryOfTransactions;

import lombok.Data;

import java.util.List;

@Data
public class OneGroupByDate {
    private String date;
    private List<OneEntry> group;
}
