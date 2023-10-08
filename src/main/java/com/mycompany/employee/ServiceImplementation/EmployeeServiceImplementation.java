package com.mycompany.employee.ServiceImplementation;

import com.mycompany.employee.Model.Documents;
import com.mycompany.employee.Model.Employee;
import com.mycompany.employee.Model.Roles;
import com.mycompany.employee.Model.UserCredientails;
import com.mycompany.employee.Repository.DocumentRepository;
import com.mycompany.employee.Repository.EmployeeRepository;
import com.mycompany.employee.Repository.RolesRepository;
import com.mycompany.employee.Repository.UserCredientailsRepository;
import com.mycompany.employee.Request.EmployeeRequest;
import com.mycompany.employee.Request.UpdateEmployeeRequest;
import com.mycompany.employee.Response.ApiResponse;
import com.mycompany.employee.Service.EmployeeService;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmployeeServiceImplementation implements EmployeeService {
    
    @Autowired
    EmployeeRepository employeeRepo;
    
    @Autowired
    RolesRepository rolesRepo;
    
    @Autowired
    UserCredientailsRepository userCredRepo;
    
    @Autowired
    DocumentRepository documentRepo;
    
    String path = "/home/harish/Downloads/";
    
    @Autowired
    PasswordEncoder encoder;
    
    @Override
    public ApiResponse addEmployee(EmployeeRequest employeeRequ) {
        Employee employee = new Employee();
        UserCredientails userCred = new UserCredientails();
        BeanUtils.copyProperties(employeeRequ, userCred);
        
        String passwordEncoded = encoder.encode(employeeRequ.getPassword());
        userCred.setPassword(passwordEncoded);
        UserCredientails checkAlreadyAvailable = userCredRepo.findByUsername(employeeRequ.getUsername());
        if (checkAlreadyAvailable != null) {
            return ApiResponse.builder().statusCode("607").errors(Map.of("message", "username already taken")).build();
        }
        
        userCred = userCredRepo.save(userCred);
        if (Objects.isNull(userCred)) {
            return ApiResponse.builder().statusCode("605").errors(Map.of("message", "failed to add employee")).build();
        }
        employee.setUserCredientails(userCred);
        
        employee.setRoles(mapRoles(employeeRequ.getRolesName()));
        BeanUtils.copyProperties(employeeRequ, employee);
        employee = employeeRepo.save(employee);
        if (Objects.isNull(employee)) {
            return ApiResponse.builder().statusCode("605").errors(Map.of("message", "failed to add employee")).build();
        }
        return ApiResponse.builder().statusCode("600").value(employee).build();
    }
    
    public List<Roles> mapRoles(List<String> roles) {
        List<Roles> roless = new ArrayList<>();
        
        for (String roleName : roles) {
            Roles role = rolesRepo.findByRoleName(roleName);
            if (Objects.isNull(role)) {
                role = rolesRepo.save(Roles.builder().roleName(roleName).build());
            }
            roless.add(role);
        }
        return roless;
    }
    
    @Override
    public ApiResponse getEmployee() {
        List<Employee> employess = employeeRepo.findAll();
        if (Objects.isNull(employess) || employess.size() == 0) {
            return ApiResponse.builder().statusCode("621").errors(Map.of("message", "No Employee in Database")).build();
        }
        return ApiResponse.builder().statusCode("600").value(employess).build();
    }
    
    @Override
    public ApiResponse getEmployeeById(int id) {
        Employee employee = employeeRepo.findById(id).get();
        if (Objects.isNull(employee)) {
            return ApiResponse.builder().statusCode("621").errors(Map.of("message", "No Employee in Database in Id = " + id)).build();
        }
        return ApiResponse.builder().statusCode("600").value(employee).build();
    }
    
    @Override
    public ApiResponse updateEmployee(UpdateEmployeeRequest updateEmployeeReq, int id) {
        Optional<Employee> empOpt = employeeRepo.findById(id);
        if (empOpt.isEmpty() || Objects.isNull(empOpt)) {
            return ApiResponse.builder().statusCode("621").errors(Map.of("message", "No Employee in Database in Id = " + id)).build();
        }
        Employee employee = empOpt.get();
        employee.setRoles(mapRoles(updateEmployeeReq.getRoles()));
        employee.setFirstName(updateEmployeeReq.getFirstName());
        employee.setLastName(updateEmployeeReq.getLastName());
        employee.setEmail(updateEmployeeReq.getEmail());
        employee.setPhNo(updateEmployeeReq.getPhNo());
        employee.setAddressLine1(updateEmployeeReq.getAddressLine1());
        employee.setState(updateEmployeeReq.getState());
        employee.setDistrict(updateEmployeeReq.getDistrict());
        employee.setCountry(updateEmployeeReq.getCountry());
        employee.setUpdatedOn(updateEmployeeReq.getUpdatedOn());
        
        employee = employeeRepo.save(employee);
        if (Objects.isNull(employee)) {
            return ApiResponse.builder().statusCode("621").errors(Map.of("message", "Failed to update for Id = " + id)).build();
        }
        return ApiResponse.builder().statusCode("600").value(employee).build();
    }
    
    @Override
    public ApiResponse deleteEmployee(int id) {
        
        Optional<Employee> empOpt = employeeRepo.findById(id);
        if (empOpt.isEmpty() || Objects.isNull(empOpt)) {
            return ApiResponse.builder().statusCode("621").errors(Map.of("message", "No Employee in Database in Id = " + id)).build();
        }
        Employee emp = empOpt.get();
        emp.getRoles().clear();
        employeeRepo.save(emp);
        employeeRepo.delete(empOpt.get());
        return ApiResponse.builder().statusCode("602").value("Employee Id " + id + " Deleted.").build();
    }
    
    @Override
    public ApiResponse uploadDocume(MultipartFile file, int id) throws Exception {
        Documents documents = new Documents();
        Optional<Employee> empOpt = employeeRepo.findById(id);
        if (empOpt.isEmpty() || Objects.isNull(empOpt)) {
            return ApiResponse.builder().statusCode("621").errors(Map.of("message", "No Employee in Database in Id = " + id)).build();
        }
        Employee emp = empOpt.get();
        documents.setDocumentName(file.getOriginalFilename());
        documents.setFile(file.getBytes());
        documents = documentRepo.save(documents);
        if (emp.getDocuments() != null) {
            emp.getDocuments().add(documents);
        } else {
            emp.setDocuments(Arrays.asList(documents));
        }
        emp = employeeRepo.save(emp);
        return ApiResponse.builder().statusCode("600").value(emp).build();
    }
    
    @Override
    public ApiResponse registerAsAdmin(EmployeeRequest employeeRequ) {
        
        Employee employee = new Employee();
        UserCredientails userCred = new UserCredientails();
        BeanUtils.copyProperties(employeeRequ, userCred);
        
        String passwordEncoded = encoder.encode(employeeRequ.getPassword());
        userCred.setPassword(passwordEncoded);
        
        UserCredientails checkAlreadyAvailable = userCredRepo.findByUsername(employeeRequ.getUsername());
        if (checkAlreadyAvailable != null) {
            return ApiResponse.builder().statusCode("607").errors(Map.of("message", "username already taken")).build();
        }
        
        userCred = userCredRepo.save(userCred);
        if (Objects.isNull(userCred)) {
            return ApiResponse.builder().statusCode("605").errors(Map.of("message", "failed to add employee")).build();
        }
        employee.setUserCredientails(userCred);
        
        employee.setRoles(mapRoles(Arrays.asList("ADMIN")));
        BeanUtils.copyProperties(employeeRequ, employee);
        employee = employeeRepo.save(employee);
        if (Objects.isNull(employee)) {
            return ApiResponse.builder().statusCode("605").errors(Map.of("message", "failed to add employee")).build();
        }
        return ApiResponse.builder().statusCode("600").value(employee).build();
        
    }
    
}
