package com.tecq.dept.hr.employeeservice.api.repository;

import com.tecq.dept.hr.employeeservice.api.dto.EmployeeCriteria;
import com.tecq.dept.hr.employeeservice.api.dto.EmployeePage;
import com.tecq.dept.hr.employeeservice.api.entity.EmployeeSalaryInfo;
import com.tecq.dept.hr.employeeservice.scb.repository.SCBCriteriaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Repository
@Transactional
public class EmployeeCriteriaRepository extends SCBCriteriaRepository<EmployeeSalaryInfo, String> {

    public EmployeeCriteriaRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public List<EmployeeSalaryInfo> findByAllFilters(EmployeeCriteria employeeCriteria, EmployeePage employeePage) {
        CriteriaQuery<Long> countQuery = criteriaBuilder
                .createQuery(Long.class);
        Root<EmployeeSalaryInfo> countQueryRoot = countQuery.from(EmployeeSalaryInfo.class);

        Predicate countQueryPredicate = criteriaBuilder.between(countQueryRoot.get("salary"),
                employeeCriteria.getMinSalary(), employeeCriteria.getMaxSalary());

        countQuery.select(criteriaBuilder
                .count(countQueryRoot));
        if(employeeCriteria.getMinSalary() >= 0 && employeeCriteria.getMaxSalary() != 0)
        {
            countQuery.where(countQueryPredicate);
        }

        Long count = entityManager.createQuery(countQuery)
                .getSingleResult();
        //get List
        List<EmployeeSalaryInfo> employeeSalaryInfos = new ArrayList<>();
        CriteriaQuery<EmployeeSalaryInfo> select = null;
        if (count > 0) {
            CriteriaQuery<EmployeeSalaryInfo> criteriaQuery = criteriaBuilder
                    .createQuery(EmployeeSalaryInfo.class);
            Root<EmployeeSalaryInfo> from = criteriaQuery.from(EmployeeSalaryInfo.class);
            select = criteriaQuery.select(from);
            Predicate salRangePredicate = criteriaBuilder.between(from.get("salary"),
                    employeeCriteria.getMinSalary(), employeeCriteria.getMaxSalary());
            if(employeeCriteria.getMinSalary() >= 0 && employeeCriteria.getMaxSalary() != 0)
            {
                select.where(salRangePredicate);
            }
            if (Sort.Direction.ASC.equals(employeePage.getSortDirection())) {
                select.orderBy(criteriaBuilder.asc(from.get(employeePage.getSortBy())));
            } else if (Sort.Direction.DESC.equals(employeePage.getSortDirection())){
                select.orderBy(criteriaBuilder.desc(from.get(employeePage.getSortBy())));
            } else {
                select.orderBy(criteriaBuilder.asc(from.get("id")));
            }

            TypedQuery<EmployeeSalaryInfo> typedQuery = entityManager.createQuery(select);

        if (employeePage.getOffset() < count.intValue()) {
            typedQuery.setFirstResult(employeePage.getOffset()); // offset
            typedQuery.setMaxResults(employeePage.getLimit()); // limit
            employeeSalaryInfos = typedQuery.getResultList();
            System.out.println("Current page: " + typedQuery.getResultList());
        }
    }
       return employeeSalaryInfos;
    }



}
