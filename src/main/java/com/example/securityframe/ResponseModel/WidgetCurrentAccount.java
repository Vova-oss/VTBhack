package com.example.securityframe.ResponseModel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WidgetCurrentAccount {

    private String account_number;
    private Long current_account;
    private String currency;

}
