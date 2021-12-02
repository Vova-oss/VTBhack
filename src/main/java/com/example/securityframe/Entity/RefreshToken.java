package com.example.securityframe.Entity;

import lombok.Data;

import java.sql.Date;

@Data
public class RefreshToken {

    private Long id;
    private Long manager_id;
    private String token;
    private Long expiryDate;

}
