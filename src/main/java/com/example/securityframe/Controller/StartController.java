package com.example.securityframe.Controller;

import com.example.securityframe.AuxiliaryClasses.StaticMethods;
import com.example.securityframe.Entity.Card;
import com.example.securityframe.Entity.Department;
import com.example.securityframe.Entity.Worker;
import com.example.securityframe.ResponseModel.DepartmentsWorkersCards.DepartmentDTO;
import com.example.securityframe.ResponseModel.WidgetCurrentAccount;
import com.example.securityframe.Service.CardService;
import com.example.securityframe.Service.DepartmentService;
import com.example.securityframe.Service.ManagerService;
import com.example.securityframe.Service.WorkerService;
import io.swagger.annotations.*;
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
    CardService cardService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @ApiOperation(value = "Получение данных для виджета 'Текущий счёт'")
    @GetMapping("/currentAccount")
    public WidgetCurrentAccount currentAccount(HttpServletRequest request, HttpServletResponse response){
        return managerService.getInfoAboutWidgetCurrentAccount(request, response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Created")
    })
    @ApiOperation(value = "Добавление нового сотрудника")
    @PostMapping("/addWorker")
    public void addWorker(
            @ApiParam(
                    name = "Worker",
                    value = "id - не нужно\nname, surname, patronymic - ФИО\ndepartment_id - :id отдела, к которому " +
                            "принадлежит сотрудник.",
                    example = "{\nname: \"Владимир\",\nsurname: \"Полетаев\",\npatronymic: \"Викторович\", \n" +
                            "department_id: \"1\"\n}",
                    required = true
            )
            @RequestBody Worker worker,
            HttpServletRequest request,
            HttpServletResponse response){
        workerService.addWorker(worker, request, response);
        StaticMethods.createResponse(request, response, 201, "Created");
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Incorrect JSON\n" +
                    "This name already exists\n")
    })
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
        StaticMethods.createResponse(request, response, 201, "Created");
    }

    @ApiOperation(value = "Изменение отдела у сотрудника")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Incorrect JSON")
    })
    @PutMapping("/updateDepartmentOfWorker")
    public void updateDepartmentOfWorker(
            @ApiParam(
                    name = "IdWorker and IdDepartment",
                    value = "id - :id сотрудника, которого хотем перевести в другой отдел\ndepartment_id - :id отдела" +
                            "куда будет переведён сотрудник",
                    example = "{\n" +"    \"id\":\"1\",\n" +"    \"department_id\":\"4\"\n" +"}",
                    required = true
            )
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response
            ){

        workerService.updateDepartmentOfWorker(body,request, response);
        StaticMethods.createResponse(request, response, 201, "Created");
    }

    @ApiOperation(value = "Добавление карты")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Incorrect JSON")
    })
    @PostMapping("/addCard")
    public void addCard(
            @ApiParam(
                    name = "Department",
                    value = "id_worker - :id сотрудника, на которого хотим оформить карту\ntype - тип карты\n" +
                            "purpose_of_creation - цель создания карты",
                    example = "{\n" +"    \"id_worker\":\"1\",\n" +"    \"type\":\"Транспорт\"\n"+
                            "    \"purpose_of_creation\":\"Поездка в метро\"\n" +"}",
                    required = true
            )
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response){
        cardService.addCard(body, request, response);
        StaticMethods.createResponse(request, response, 201, "Created");
    }

    @ApiOperation(value = "Получение отделов, сотрудников и карт")
    @GetMapping("/getDepartmentsWorkersCards")
    public List<DepartmentDTO> getDepartmentsWorkersCards(HttpServletRequest request, HttpServletResponse response){
        return departmentService.getDepartmentsWorkersCards(request, response);
    }

//    @GetMapping("/fiveGeneralInformation")
//    public void fiveGeneralInformation(HttpServletRequest request, HttpServletResponse response){
//        managerService.fiveGeneralInformation(request);
//    }

}
