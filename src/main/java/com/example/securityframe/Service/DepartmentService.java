package com.example.securityframe.Service;

import com.example.securityframe.AuxiliaryClasses.StaticMethods;
import com.example.securityframe.DAO.DepartmentDAO;
import com.example.securityframe.DAO.WorkerDAO;
import com.example.securityframe.Entity.*;
import com.example.securityframe.ResponseModel.DepartmentsWorkersCards.CardDTO;
import com.example.securityframe.ResponseModel.DepartmentsWorkersCards.DepartmentDTO;
import com.example.securityframe.ResponseModel.DepartmentsWorkersCards.WorkerDTO;
import com.example.securityframe.Security.SService.JWTokenService;
import liquibase.pro.packaged.W;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.example.securityframe.AuxiliaryClasses.StaticMethods.parsingJson;
import static com.example.securityframe.Security.SecurityConstants.HEADER_JWT_STRING;
import static com.example.securityframe.Security.SecurityConstants.TOKEN_PREFIX;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentDAO departmentDAO;

    @Autowired
    private JWTokenService jwTokenService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private WorkerService workerService;
    @Autowired
    private CardService cardService;


    public Long findIdOfIndividualByAccountId(Long account_id) {
        Department department = departmentDAO.findByAccountId(account_id);
        return department.getId();
    }

    public Department findByAccountId(Long account_id){
        return departmentDAO.findByAccountId(account_id);
    }

    public List<Department> findAllByAccount_id(HttpServletRequest request, HttpServletResponse response) {
        Account account = accountService.findByJwt(request);
        return departmentDAO.findAllByAccount_id(account.getId());
    }

    public boolean existenceOfName(String name, HttpServletRequest request, HttpServletResponse response){
        Account account = accountService.findByJwt(request);
        return departmentDAO.existenceOfName(name, account.getId());
    }

    public boolean existenceOfName(String name, Long account_id){
        return departmentDAO.existenceOfName(name, account_id);
    }

    public void addDepartment(String body, HttpServletRequest request, HttpServletResponse response) {
        String name = StaticMethods.parsingJson(body, "name", request, response);
        if(name == null)
            return;

        Account account = accountService.findByJwt(request);
        if(existenceOfName(name, account.getId())){
            StaticMethods.createResponse(request, response, 400, "This name already exists");
            return;
        }

        Department department = new Department();
        department.setName(name);
        department.setAccount_id(account.getId());

        departmentDAO.save(department);
        department = departmentDAO.findByName(name, account.getId());

        try {
            JSONObject jsonObject = new JSONObject(body);
            JSONArray array = jsonObject.getJSONArray("workers");
            for (int i = 0; i<array.length(); i++){
                String id = array.getJSONObject(i).getString("id");
                workerService.replaceDepartmentId(id, department.getId());
            }
        } catch (JSONException e) {
            StaticMethods.createResponse(request, response, HttpServletResponse.SC_BAD_REQUEST, "Incorrect JSON");
        }

    }

    public List<DepartmentDTO> getDepartmentsWorkersCards(HttpServletRequest request, HttpServletResponse response) {

        List<DepartmentDTO> departmentDTOS = new ArrayList<>();

        List<Department> departments = findAllByAccount_id(request, response);
        for (Department department: departments){

            List<WorkerDTO> workerDTOS = new ArrayList<>();
            long amountOfCards = 0;

            List<Worker> workers = workerService.findAllByDepartmentId(department.getId());
            for(Worker worker: workers){
                List<Card> cards = cardService.findAllByWorkerId(worker.getId());

                amountOfCards+=cards.size();
                WorkerDTO workerDTO = WorkerDTO.createWorkerDTO(worker, cards);
                workerDTOS.add(workerDTO);
            }

            DepartmentDTO departmentDTO = DepartmentDTO.createDepartmentDTO(department, workerDTOS, amountOfCards);
            departmentDTOS.add(departmentDTO);
        }

        return departmentDTOS;
    }
}
