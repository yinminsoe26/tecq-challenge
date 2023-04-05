package com.tecq.dept.hr.employeeservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tecq.dept.hr.employeeservice.api.entity.EmployeeSalaryInfo;

import java.util.List;


public class Employees {
    @JsonProperty("results")
    List<EmployeeSalaryInfo> results ;
}
