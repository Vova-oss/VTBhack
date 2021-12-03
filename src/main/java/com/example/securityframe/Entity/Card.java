package com.example.securityframe.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Card {

    private Long id;
    private Long worker_id;
    private String payment_system;
    private String card_number;
    private Long account;
    private String type;
    private String purpose_of_creation;
    private String status;
    private Long limit;
    private Long limit_beginning;
    private String duration;
    private String currency;

}
