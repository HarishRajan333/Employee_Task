package com.mycompany.employee.Response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class ApiResponse {

    private String statusCode;
    private Object value;
    private Map<String, String> errors;
}
