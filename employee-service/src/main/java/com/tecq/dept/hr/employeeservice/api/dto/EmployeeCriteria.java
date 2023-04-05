package com.tecq.dept.hr.employeeservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeCriteria {
    public static final List<String> EMLOYEE_SORT_COLUMNS= Arrays.asList(new String[] {"id", "name", "login", "salary"});
    private Double minSalary;
    private Double maxSalary;


}
