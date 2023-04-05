package com.tecq.dept.hr.employeeservice.api.controller;

import com.tecq.dept.hr.employeeservice.api.dto.EmployeeCriteria;
import com.tecq.dept.hr.employeeservice.api.dto.EmployeePage;
import com.tecq.dept.hr.employeeservice.api.entity.EmployeeSalaryInfo;
import com.tecq.dept.hr.employeeservice.api.service.EmployeeSalaryInfoService;
import com.tecq.dept.hr.employeeservice.api.util.CSVFileHelper;
import com.tecq.dept.hr.employeeservice.scb.controller.SCBController;
import com.tecq.dept.hr.employeeservice.scb.controller.SCBCrudController;
import com.tecq.dept.hr.employeeservice.scb.dto.SCBMessage;
import com.tecq.dept.hr.employeeservice.scb.exception.SCBClientException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.tecq.dept.hr.employeeservice.api.dto.EmployeeCriteria.EMLOYEE_SORT_COLUMNS;

@RestController
@Slf4j
@CrossOrigin
public class EmployeeController extends SCBController implements SCBCrudController<EmployeeSalaryInfo, String> {
    private final EmployeeSalaryInfoService employeeSalaryInfoService;

    public EmployeeController(EmployeeSalaryInfoService employeeSalaryInfoService){
        this.employeeSalaryInfoService=employeeSalaryInfoService;
    }
    //User Story 1
    @PostMapping("/users/upload")
    public String uploadEmployeeSalaryCSVFile(@RequestParam("file") @NotNull MultipartFile csvFile) throws IOException {
        String fileExtension = CSVFileHelper.getExtensionOfFileName(csvFile.getOriginalFilename());
        if("csv".equals(fileExtension)){
            List<EmployeeSalaryInfo> employeeSalaryInfos= employeeSalaryInfoService.validateAllCsvRecords(csvFile);
            if(employeeSalaryInfos !=null && employeeSalaryInfos.size() > 0)
                employeeSalaryInfoService.saveAllCsvRecords(employeeSalaryInfos);
            return getMessage(new SCBMessage("MAPI10001I", ""+employeeSalaryInfos.size()));
        }else{
            throw  new SCBClientException(new SCBMessage("MAPI10009E", csvFile.getOriginalFilename(), "UPLOAD"));
        }
    }

    //User Story 2
    @GetMapping("/users")
    public List<EmployeeSalaryInfo> getUsers(
            @RequestParam(value = "minSalary", required = true) Double minSalary,
            @RequestParam(value = "maxSalary", required = true) Double maxSalary,
            @RequestParam(value = "offset", required = true) Integer offset,
            @RequestParam(value = "limit", defaultValue = "30") Integer limit,
            @RequestParam(value = "sort", required = true) String sortColumn)
    {///users?minSalary=0&maxSalary=4000&offset=0&limit=30&sort=+name
       //Declaration && Initialization
        EmployeePage employeePage = new EmployeePage();
        employeePage.setOffset(offset);
        employeePage.setLimit(limit);

        //validation
        if(StringUtils.hasLength(sortColumn)){
             if(sortColumn.contains("-")){
                sortColumn= sortColumn.replace("-","").trim();
                employeePage.setSortDirection( Sort.Direction.DESC);
                employeePage.setSortBy(sortColumn);
            }else{
                 sortColumn= sortColumn.replace("+","").trim();
                 employeePage.setSortDirection( Sort.Direction.ASC);
                 employeePage.setSortBy(sortColumn);
             }
            if(sortColumn.contains(",") || !EMLOYEE_SORT_COLUMNS.contains(sortColumn)){
                throw  new SCBClientException(new SCBMessage("MAPI10020E", sortColumn));
            }
        }
        if(minSalary==null){
            minSalary=0.0;
        }
        if(maxSalary==null){
            maxSalary=0.0;
        }

        EmployeeCriteria employeeCriteria = EmployeeCriteria.builder().minSalary(minSalary).maxSalary(maxSalary).build();

        if(minSalary >= 0 && maxSalary > 0 && minSalary <= maxSalary){

            int page=0;
            log.info(employeePage.toString());
            //used criteria logic
             return employeeSalaryInfoService.findByAllFilters(employeeCriteria, employeePage);

          /* comment out spring data jpa logic
          Pageable pageable = PageRequest.of(page, limit, sortObj);
          return employeeSalaryInfoService.findBySalaryBetween(minSalary,maxSalary,pageable);*/
        }else{
            if(minSalary == 0 && maxSalary == 0){
                return employeeSalaryInfoService.findByAllFilters(employeeCriteria, employeePage);
            }
            throw  new SCBClientException(new SCBMessage("MAPI10021E", String.valueOf(minSalary), String.valueOf(maxSalary), "READ"));
        }
    }


    //User Story 3 CRUD
    @Override
    @PostMapping(value = "/users/{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeSalaryInfo create(@PathVariable("id") String id, @RequestBody EmployeeSalaryInfo employeeSalaryInfo) {
        employeeSalaryInfo.setId(id);
        return employeeSalaryInfoService.save(employeeSalaryInfo);
    }

    @Override
    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeSalaryInfo read(@PathVariable("id") String id) {
        return employeeSalaryInfoService.findById(id);
    }

    @Override
    @PatchMapping(value ="/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeSalaryInfo update(@PathVariable("id") String id, @RequestBody EmployeeSalaryInfo employeeSalaryInfo) {
        employeeSalaryInfo.setId(id);
        return employeeSalaryInfoService.update(employeeSalaryInfo);
    }

    @Override
    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable("id")String id) {
        employeeSalaryInfoService.deleteById(id);
    }
}
