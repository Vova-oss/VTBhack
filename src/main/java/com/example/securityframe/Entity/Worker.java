package com.example.securityframe.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Worker {

    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private Long department_id;

}
