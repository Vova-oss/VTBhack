package com.example.securityframe.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
public class Role {

    private Long id;
    private String role;

}
