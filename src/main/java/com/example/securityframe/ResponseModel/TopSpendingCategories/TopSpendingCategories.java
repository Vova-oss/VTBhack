package com.example.securityframe.ResponseModel.TopSpendingCategories;

import lombok.Data;

import java.util.List;

@Data
public class TopSpendingCategories {

    private String maxSum;
    private List<OneCategory> list;

}
