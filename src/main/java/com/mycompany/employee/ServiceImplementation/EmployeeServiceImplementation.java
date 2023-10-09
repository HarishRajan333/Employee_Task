package com.mycompany.employee.ServiceImplementation;

import com.mycompany.employee.GlobelException.UserNotFoundException;
import com.mycompany.employee.Mapper.Mapper;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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

    @Autowired
    Mapper mapper;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public ApiResponse addEmployee(EmployeeRequest employeeRequ) {

        if (userCredRepo.existsByUsername(employeeRequ.getUsername())) {
            return ApiResponse.builder()
                    .statusCode("607")
                    .errors(Map.of("message", "username already taken"))
                    .build();
        }

        UserCredientails userCred = new UserCredientails();
        BeanUtils.copyProperties(employeeRequ, userCred);
        String passwordEncoded = encoder.encode(employeeRequ.getPassword());
        userCred.setPassword(passwordEncoded);

        userCred = userCredRepo.save(userCred);

        if (Objects.isNull(userCred)) {
            return ApiResponse.builder()
                    .statusCode("605")
                    .errors(Map.of("message", "failed to add employee"))
                    .build();
        }

        Employee employee = new Employee();
        employee.setUserCredientails(userCred);
        employee.setRoles(mapRoles(employeeRequ.getRolesName()));
        BeanUtils.copyProperties(employeeRequ, employee);

        employee = employeeRepo.save(employee);

        if (Objects.isNull(employee)) {
            return ApiResponse.builder()
                    .statusCode("605")
                    .errors(Map.of("message", "failed to add employee"))
                    .build();
        }

        return ApiResponse.builder()
                .statusCode("600")
                .value(employee)
                .build();

    }

    public List<Roles> mapRoles(List<String> roles) {

        return roles.stream()
                .map(roleName -> {
                    Roles role = rolesRepo.findByRoleName(roleName);
                    return Objects.isNull(role) ? rolesRepo.save(Roles.builder()
                            .roleName(roleName)
                            .build()) : role;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponse getEmployee() {
        List<Employee> employess = employeeRepo.findAll();

        if (employess.isEmpty()) {
            throw new UserNotFoundException();
        }

        return ApiResponse.builder()
                .statusCode("600")
                .value(employess)
                .build();
    }

    @Override
    public ApiResponse getEmployeeById(int id) {
        Employee employee = employeeRepo.findById(id).get();

        if (Objects.isNull(employee)) {
            throw new UserNotFoundException();
        }

        return ApiResponse.builder()
                .statusCode("600")
                .value(employee)
                .build();
    }

    @Override
    public ApiResponse updateEmployee(UpdateEmployeeRequest updateEmployeeReq, int id) {
        Optional<Employee> empOpt = employeeRepo.findById(id);
        if (empOpt.isEmpty() || Objects.isNull(empOpt)) {
            throw new UserNotFoundException();
        }

        Employee employee = empOpt.get();
        mapper.copyNonNullPropertys(updateEmployeeReq, employee);

        employee = employeeRepo.save(employee);

        if (Objects.isNull(employee)) {
            return ApiResponse.builder()
                    .statusCode("621")
                    .errors(Map.of("message", "Failed to update for Id = " + id))
                    .build();
        }

        return ApiResponse.builder()
                .statusCode("600")
                .value(employee)
                .build();
    }

    @Override
    public ApiResponse deleteEmployee(int id) {

        Optional<Employee> empOpt = employeeRepo.findById(id);
        if (empOpt.isEmpty() || Objects.isNull(empOpt)) {
            throw new UserNotFoundException();
        }

        Employee emp = empOpt.get();
        emp.getRoles().clear();
        employeeRepo.save(emp);
        employeeRepo.delete(empOpt.get());
        return ApiResponse.builder()
                .statusCode("602")
                .value("Employee Id " + id + " Deleted.")
                .build();
    }

    @Override
    public ApiResponse uploadDocume(MultipartFile file, int id) throws Exception {
        Optional<Employee> empOpt = employeeRepo.findById(id);
        if (empOpt.isEmpty() || Objects.isNull(empOpt)) {
            throw new UserNotFoundException();
        }
        Employee emp = empOpt.get();

        Documents documents = Documents.builder()
                .documentName(file.getOriginalFilename())
                .file(file.getBytes())
                .build();

        documents = documentRepo.save(documents);

        if (emp.getDocuments() != null) {
            emp.getDocuments().add(documents);
        } else {
            emp.setDocuments(Arrays.asList(documents));
        }

        emp = employeeRepo.save(emp);

        return ApiResponse.builder()
                .statusCode("600")
                .value(emp)
                .build();
    }

    @Override
    public ApiResponse registerAsAdmin(EmployeeRequest employeeRequ) {
        if (userCredRepo.existsByUsername(employeeRequ.getUsername())) {
            return ApiResponse.builder()
                    .statusCode("607")
                    .errors(Map.of("message", "username already taken"))
                    .build();
        }

        UserCredientails userCred = new UserCredientails();

        BeanUtils.copyProperties(employeeRequ, userCred);
        String passwordEncoded = encoder.encode(employeeRequ.getPassword());
        userCred.setPassword(passwordEncoded);

        userCred = userCredRepo.save(userCred);
        if (Objects.isNull(userCred)) {
            return ApiResponse.builder()
                    .statusCode("605")
                    .errors(Map.of("message", "failed to add employee"))
                    .build();
        }

        Employee employee = Employee.builder()
                .userCredientails(userCred)
                .roles(mapRoles(Arrays.asList("ADMIN")))
                .build();
        BeanUtils.copyProperties(employeeRequ, employee);

        employee = employeeRepo.save(employee);

        if (Objects.isNull(employee)) {
            return ApiResponse.builder()
                    .statusCode("605")
                    .errors(Map.of("message", "failed to add employee"))
                    .build();
        }

        return ApiResponse.builder()
                .statusCode("600")
                .value(employee)
                .build();
    }

}
