package com.example.securityframe.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Card {

    @ApiModelProperty(notes = ":id", name = "id", required = true, example = "1")
    private Long id;

    @ApiModelProperty(notes = ":id сотрудника, которому принадлежит карта", name = "worker_id", required = true, example = "1")
    private Long worker_id;

    @ApiModelProperty(notes = "Платёжная система (по умолчанию VISA)", name = "payment_system", example = "VISA")
    private String payment_system;

    @ApiModelProperty(notes = "Номер карты (16 цифр)", name = "card_number", example = "6523124895742365")
    private String card_number;

    @ApiModelProperty(notes = "Счёт карты (В рублях)", name = "account", example = "5000")
    private Long account;

    @ApiModelProperty(notes = "Тип карты", name = "type", example = "Транспорт")
    private String type;

    @ApiModelProperty(notes = "Цель создания", name = "purpose_of_creation", example = "Поезда в метро")
    private String purpose_of_creation;

    @ApiModelProperty(notes = "Статус карты", name = "status", example = "ACTIVE")
    private String status;

    @ApiModelProperty(notes = "Сумма, на которую ограничиваются расходы", name = "limit", example = "95000")
    private Long limit;

    @ApiModelProperty(notes = "Срок, на который ограничиваются расходы (в днях)", name = "term", example = "20")
    private Long term;

    @ApiModelProperty(notes = "Остаток, который можно потратить", name = "remains", example = "12740")
    private Long remains;

    @ApiModelProperty(notes = "Автообновление по истичению :limit", name = "autoUpdate", example = "true")
    private Boolean autoUpdate;

    @ApiModelProperty(notes = "Валята (по дефолту RUB)", name = "currency", example = "RUB")
    private String currency;

}
