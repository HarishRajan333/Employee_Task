package com.mycompany.employee.Controller;

import com.mycompany.employee.Request.EmployeeRequest;
import com.mycompany.employee.Request.UpdateEmployeeRequest;
import com.mycompany.employee.Response.ApiResponse;
import com.mycompany.employee.Service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    EmployeeService employeeSrvice;

    @PostMapping("/employees")
    public ApiResponse addEmployee(@Valid @RequestBody EmployeeRequest employeeRequ) {
        return employeeSrvice.addEmployee(employeeRequ);
    }

    @GetMapping("/employees")
    public ApiResponse getEmployee() {
        return employeeSrvice.getEmployee();
    }

    @GetMapping("/employees/{id}")
    public ApiResponse getEmployeeById(@PathVariable(name = "id") int id) {
        return employeeSrvice.getEmployeeById(id);
    }

    @PutMapping("/employees/{id}")
    public ApiResponse updateEmployee(@Valid @RequestBody UpdateEmployeeRequest updateEmployeeReq,@PathVariable(name = "id") int id) {
        return employeeSrvice.updateEmployee(updateEmployeeReq,id);
    }

    @DeleteMapping("/employees/{id}")
    public ApiResponse deleteEmployee(@PathVariable(name = "id") int id) {
        return employeeSrvice.deleteEmployee(id);
    }

    @PostMapping("/employees/{id}")
    public ApiResponse uploadDocument(@RequestParam("file") MultipartFile file ,@PathVariable(name = "id") int id) throws Exception {
        return employeeSrvice.uploadDocume(file,id);
    }
    
    @PostMapping("/register")
    public ApiResponse firstRegister(@RequestBody EmployeeRequest empReq){
        return employeeSrvice.registerAsAdmin(empReq);
    }

}
