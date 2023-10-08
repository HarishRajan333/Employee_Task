package com.mycompany.employee.Service;

import com.mycompany.employee.Request.EmployeeRequest;
import com.mycompany.employee.Request.UpdateEmployeeRequest;
import com.mycompany.employee.Response.ApiResponse;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {

    public ApiResponse addEmployee(EmployeeRequest employeeRequ);

    public ApiResponse getEmployee();

    public ApiResponse getEmployeeById(int id);

    public ApiResponse updateEmployee(UpdateEmployeeRequest updateEmployeeReq, int id);

    public ApiResponse deleteEmployee(int id);

    public ApiResponse uploadDocume(MultipartFile file, int id) throws Exception;

    public ApiResponse registerAsAdmin(EmployeeRequest empReq);

}
