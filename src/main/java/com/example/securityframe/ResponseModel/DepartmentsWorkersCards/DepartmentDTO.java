package com.example.securityframe.ResponseModel.DepartmentsWorkersCards;

import com.example.securityframe.Entity.Department;
import com.example.securityframe.Entity.Worker;
import lombok.Data;

import java.util.List;

@Data
public class DepartmentDTO {

    private Long id;
    private String name;
    private Long amountOfCards;
    List<WorkerDTO> workers;


    public static DepartmentDTO createDepartmentDTO(Department department, List<WorkerDTO> workers, Long amountOfCards){
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(department.getId());
        departmentDTO.setName(department.getName());
        departmentDTO.setAmountOfCards(amountOfCards);
        departmentDTO.setWorkers(workers);
        return departmentDTO;
    }

}
