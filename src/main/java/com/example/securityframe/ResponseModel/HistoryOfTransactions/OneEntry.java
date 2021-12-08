package com.example.securityframe.ResponseModel.HistoryOfTransactions;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OneEntry {

    @ApiModelProperty(notes = "Дата", name = "date", example = "2021-12-08")
    private String date;

    @ApiModelProperty(notes = "Время", name = "time", example = "02:50")
    private String time;

    @ApiModelProperty(notes = "Категория", name = "category", example = "Figma.com")
    private String category;

    @ApiModelProperty(notes = "ФИО", name = "fio", example = "Иванов И. И.")
    private String fio;

    @ApiModelProperty(notes = "Название отдела", name = "name", example = "Figma.com")
    private String name;

    @ApiModelProperty(notes = "Тип карты (грубо говоря)", name = "type", example = "Оборудование")
    private String type;

    @ApiModelProperty(notes = "Платёжная система", name = "payment_system", example = "VISA")
    private String payment_system;

    @ApiModelProperty(notes = "Номер карты (либо 16, либо 20 цифр)", name = "card_number", example = "1851500503771723")
    private String card_number;

    @ApiModelProperty(notes = "Сумма", name = "value", example = "-5654")
    private String value;

    @ApiModelProperty(notes = "Валюта", name = "currency", example = "RUB")
    private String currency;

}
