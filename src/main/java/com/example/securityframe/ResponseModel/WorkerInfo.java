package com.example.securityframe.ResponseModel;

import lombok.Data;

@Data
public class WorkerInfo {
    private String name;
    private String surname;
    private String patronymic;
    private String departmentType;
    private Long account;
}
