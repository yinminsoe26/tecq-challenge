package com.tecq.dept.hr.employeeservice.api.service;

import com.tecq.dept.hr.employeeservice.api.dto.EmployeeCriteria;
import com.tecq.dept.hr.employeeservice.api.dto.EmployeePage;
import com.tecq.dept.hr.employeeservice.api.entity.EmployeeSalaryInfo;
import com.tecq.dept.hr.employeeservice.scb.service.SCBService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface EmployeeSalaryInfoService extends SCBService<EmployeeSalaryInfo,String> {
    EmployeeSalaryInfo findByIdAndLogin(String id, String login);
    List<EmployeeSalaryInfo> findByLogin(String login);

    boolean saveAllCsvRecords(List<EmployeeSalaryInfo> employeeSalaryInfos);

    List<EmployeeSalaryInfo>  validateAllCsvRecords(MultipartFile csvFile);

    List<EmployeeSalaryInfo> findBySalaryBetween(Double minSalary, Double maxSalary, Pageable pageable);

    public List<EmployeeSalaryInfo> findByAllFilters(EmployeeCriteria employeeCriteria, EmployeePage employeePage);
}
