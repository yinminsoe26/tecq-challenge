package com.tecq.dept.hr.employeeservice.scb.mapper;

import java.util.List;

public interface SCBMapper <E,D> {

    D toDto(E t);
    E toEntity(D d);

    List<D> toDtoList(List<E> ts);
    List<E> toEntityList(List<D> d);
}
