package com.example.securityframe.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
public class Transaction {

    private Long id;
    private Long card_id;
    private String category;
    private Date date;
    private Long value;

}
