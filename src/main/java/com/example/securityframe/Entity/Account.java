package com.example.securityframe.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Account {

    private Long id;
    private Long manager_id;
    private String account_number;
    private Long current_account;
    private String currency;
    private Long total_balance;
    private Long allocated_funds;
    private Long monthly_expenses;

}
