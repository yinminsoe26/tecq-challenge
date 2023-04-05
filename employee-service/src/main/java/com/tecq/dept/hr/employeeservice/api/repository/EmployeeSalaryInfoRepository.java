package com.tecq.dept.hr.employeeservice.api.repository;

import com.tecq.dept.hr.employeeservice.api.entity.EmployeeSalaryInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeSalaryInfoRepository extends JpaRepository<EmployeeSalaryInfo, String > {
    Optional<EmployeeSalaryInfo> findByIdAndLogin(String id, String login);
    List<EmployeeSalaryInfo> findByLogin(String login);

    Page<EmployeeSalaryInfo> findBySalaryBetween(Double minSalary, Double maxSalary, Pageable pageable);
}
