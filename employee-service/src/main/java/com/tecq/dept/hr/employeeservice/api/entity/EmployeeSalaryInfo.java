package com.tecq.dept.hr.employeeservice.api.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tecq.dept.hr.employeeservice.scb.dto.SCBDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonPropertyOrder({
       "id",
        "login",
        "name",
        "salary"
})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(indexes = {@Index(name = "unq_id_login",  columnList="id,login", unique = true)})
public class EmployeeSalaryInfo extends SCBDto {

    @Id
    @Column(name = "id", columnDefinition=" varchar(80) NOT NULL COMMENT 'employee ID'")
    private String id;

    @Column(name = "login", columnDefinition=" varchar(80) NOT NULL COMMENT 'employee login'")
    private String login;

    @Column(name = "name", columnDefinition=" varchar(100) NOT NULL COMMENT 'employee non-unique name'")
    private String name;

    @Column(name = "salary", columnDefinition=" DECIMAL(20, 2) DEFAULT 0.00 COMMENT 'employee salary'")
    private Double salary;
}
