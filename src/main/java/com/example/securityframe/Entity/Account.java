package com.example.securityframe.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Account {

    @ApiModelProperty(notes = ":id", name = "id", required = true, example = "1")
    private Long id;

    @ApiModelProperty(notes = ":id Менеждера", name = "manager_id", required = true, example = "3")
    private Long manager_id;

    @ApiModelProperty(notes = "Номер счёта", name = "account_number",example = "6379292541630651")
    private String account_number;

    @ApiModelProperty(notes = "Баланс (текущий счёт)", name = "current_account", example = "2821650")
    private Long current_account;

    @ApiModelProperty(notes = "Валюта", name = "currency", example = "RUB")
    private String currency;

    @ApiModelProperty(notes = "Общий баланс", name = "total_balance", example = "205409")
    private Long total_balance;

    @ApiModelProperty(notes = "Выделено средств", name = "allocated_funds", example = "432125")
    private Long allocated_funds;

    @ApiModelProperty(notes = "Траты за месяц", name = "monthly_expenses", example = "1023")
    private Long monthly_expenses;

}
