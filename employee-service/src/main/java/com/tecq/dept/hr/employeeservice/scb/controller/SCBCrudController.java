package com.tecq.dept.hr.employeeservice.scb.controller;

public interface SCBCrudController<T, ID> {
    T create(ID id, T d);

    T read(ID id);

    T update(ID id, T d);

    void delete(ID id);

}
