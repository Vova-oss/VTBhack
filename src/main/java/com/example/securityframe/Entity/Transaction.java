package com.example.securityframe.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@NoArgsConstructor
public class Transaction {

    private Long id;
    private Long card_id;
    private Long account_id;
    private String category;
    private Date date;
    private Time time;
    private Long value;

}
