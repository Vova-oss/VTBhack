package com.example.securityframe.Controller;

import com.example.securityframe.AuxiliaryClasses.StaticMethods;
import com.example.securityframe.Entity.Department;
import com.example.securityframe.Entity.Worker;
import com.example.securityframe.ResponseModel.WidgetCurrentAccount;
import com.example.securityframe.Service.DepartmentService;
import com.example.securityframe.Service.ManagerService;
import com.example.securityframe.Service.WorkerService;
import io.swagger.annotations.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Api(tags = "Start")
@RequestMapping("/start")
public class StartController {

    @Autowired
    ManagerService managerService;
    @Autowired
    WorkerService workerService;
    @Autowired
    DepartmentService departmentService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @ApiOperation(value = "Получение данных для виджета 'Текущий счёт'")
    @GetMapping("/currentAccount")
    public WidgetCurrentAccount currentAccount(HttpServletRequest request, HttpServletResponse response){
        return managerService.getInfoAboutWidgetCurrentAccount(request, response);
    }

    @ApiOperation(value = "Добавление нового сотрудника")
    @PostMapping("/addWorker")
    public void addWorker(
            @ApiParam(
                    name = "Worker",
                    value = "id - не нужно\nname, surname, patronymic - ФИО\ndepartment_id - :id отдела, к которому " +
                            "принадлежит сотрудник. Если отдел не был выбран, то ставить '-1'.",
                    example = "{\nname: \"Владимир\",\nsurname: \"Полетаев\",\npatronymic: \"Викторович\", \n" +
                            "department_id: \"-1\"\n}",
                    required = true
            )
            @RequestBody Worker worker,
            HttpServletRequest request,
            HttpServletResponse response){
        workerService.addWorker(worker, request, response);
        StaticMethods.createResponse(request, response, 200, "Worker added");
    }

    @ApiOperation(value = "Получение всех отделов")
    @GetMapping("/getAllDepartments")
    public List<Department> getAllDepartments(HttpServletRequest request, HttpServletResponse response){
        return departmentService.findAllByAccount_id(request, response);
    }

    @ApiOperation(value = "Получение всех сотрудников")
    @GetMapping("/getAllWorkers")
    public List<Worker> getAllWorkers(HttpServletRequest request, HttpServletResponse response){
        return workerService.findAllByAccount_id(request, response);
    }

    @ApiOperation(value = "Добавление отдела")
    @PostMapping("/addDepartment")
    public void addDepartment(
            @ApiParam(
                    name = "Department",
                    value = "name - название нового отдела\nworkers - сотрудники, которые будут переведены в это отделение" +
                            "\nid - :id каждого сотрудника",
                    example = "{\n" +"    \"name\" : \"Отдел маркетинга\",\n" +"    \"workers\" : [\n" +"        {\n" +
                            "            \"id\" : 2\n" +"        },\n" +"        {\n" +"            \"id\" : 3\n" +
                            "        }\n" +"    ]\n" +"}",
                    required = true
            )
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response){

        departmentService.addDepartment(body, request, response);
        StaticMethods.createResponse(request, response, 200, "Department added");
    }

}
