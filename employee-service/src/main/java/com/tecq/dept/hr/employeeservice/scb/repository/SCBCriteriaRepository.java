package com.tecq.dept.hr.employeeservice.scb.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;

public class SCBCriteriaRepository<T, ID> {
    protected EntityManager entityManager;
    protected CriteriaBuilder criteriaBuilder;


    public SCBCriteriaRepository(EntityManager entityManager){
        this.entityManager=entityManager;
        this.criteriaBuilder=entityManager.getCriteriaBuilder();
    }
}
