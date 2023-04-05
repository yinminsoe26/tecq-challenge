package com.tecq.dept.hr.employeeservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeePage {
    private int offset;
    private int limit;
    private Sort.Direction sortDirection = Sort.Direction.ASC;

    private String sortBy="id";

    private int pageNo;
    private int pageSize = 10;
}
