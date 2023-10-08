package com.mycompany.employee.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class EmployeeRequest {

    @NotBlank
    @NotNull
    private String firstName;

    @NotBlank
    @NotNull
    private String lastName;

    @NotBlank
    @NotNull
    @Email
    private String email;

    @NotBlank
    @NotNull
    private String phNo;

    private String addressLine1;

    private String state;

    private String district;

    @NotBlank
    @NotNull
    private String country;

    private Timestamp joinedOn;

    private Timestamp addedOn;

    private Timestamp updatedOn;

    @NotBlank
    @NotNull
    @Size(min = 3)
    private String username;
    
    @NotBlank
    @NotNull
    @Size(min = 8)
    private String password;

    @NotNull
    @NotEmpty
    private List<String> rolesName;
}
