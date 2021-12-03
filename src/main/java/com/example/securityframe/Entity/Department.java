package com.example.securityframe.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Department {

    private Long id;
    private String name;
    private Long account_id;

}
