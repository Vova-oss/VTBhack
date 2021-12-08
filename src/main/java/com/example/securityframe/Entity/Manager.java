package com.example.securityframe.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.List;

@Data
@NoArgsConstructor
public class Manager {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String patronymic;
    private String post;
}
