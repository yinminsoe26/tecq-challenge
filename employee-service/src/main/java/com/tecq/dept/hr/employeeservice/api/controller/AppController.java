package com.tecq.dept.hr.employeeservice.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class AppController {
    @GetMapping("/status")
    public String uploadEmployeeSalaryCSVFile(){
        return "Api started at "+new Date();
    }
}
