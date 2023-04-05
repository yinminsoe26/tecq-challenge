package com.tecq.dept.hr.employeeservice.scb.service;

import java.util.List;
import java.util.Optional;

public interface SCBService<T, ID> {
    public static final String READ = "READ";
    public static final String CREATE = "CREATE";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";

    List<T> findAll();

    T findById(ID id);

    T save(T d);

    T update(T d);

    void deleteById(ID id);

    boolean existsById(ID id);


    T getResource(Optional<T> tOptional, String... args);

    List<T> getResources(List<T> tList, String... args);

}
