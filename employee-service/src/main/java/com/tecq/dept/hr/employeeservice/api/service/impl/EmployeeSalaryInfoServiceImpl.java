package com.tecq.dept.hr.employeeservice.api.service.impl;

import com.tecq.dept.hr.employeeservice.api.dto.EmployeeCriteria;
import com.tecq.dept.hr.employeeservice.api.dto.EmployeePage;
import com.tecq.dept.hr.employeeservice.api.entity.EmployeeSalaryInfo;
import com.tecq.dept.hr.employeeservice.api.repository.EmployeeCriteriaRepository;
import com.tecq.dept.hr.employeeservice.api.repository.EmployeeSalaryInfoRepository;
import com.tecq.dept.hr.employeeservice.api.service.EmployeeSalaryInfoService;
import com.tecq.dept.hr.employeeservice.api.util.ApiConstants;
import com.tecq.dept.hr.employeeservice.api.util.CSVFileHelper;
import com.tecq.dept.hr.employeeservice.scb.dto.SCBMessage;
import com.tecq.dept.hr.employeeservice.scb.exception.SCBClientException;
import com.tecq.dept.hr.employeeservice.scb.exception.SCBRecordNotFoundException;
import com.tecq.dept.hr.employeeservice.scb.exception.SCBUnknownException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Slf4j
@Transactional
public class EmployeeSalaryInfoServiceImpl implements EmployeeSalaryInfoService {
    public static final String CSV_COLUMN1="id";
    public static final String CSV_COLUMN2="login";
    public static final String CSV_COLUMN3="name";
    public static final String CSV_COLUMN4="salary";

    private final EmployeeSalaryInfoRepository employeeSalaryInfoRepository;

    private final EmployeeCriteriaRepository employeeCriteriaRepository;

    public EmployeeSalaryInfoServiceImpl(EmployeeSalaryInfoRepository employeeSalaryInfoRepository,
                                         EmployeeCriteriaRepository employeeCriteriaRepository ){
        this.employeeSalaryInfoRepository=employeeSalaryInfoRepository;
        this.employeeCriteriaRepository=employeeCriteriaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeSalaryInfo> findAll() {
        return getResources(employeeSalaryInfoRepository.findAll(),"ALL",READ);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeSalaryInfo findById(String id) {
        return getResource(employeeSalaryInfoRepository.findById(id), ""+id, READ);
    }

    @Override
    public EmployeeSalaryInfo save(EmployeeSalaryInfo employeeSalaryInfo) {
        String id = employeeSalaryInfo.getId();
        if(!existsById(id)){
            return  employeeSalaryInfoRepository.save(employeeSalaryInfo);
        }else{
            throw  new SCBClientException(new SCBMessage("MSCB10002E", ""+id, CREATE));
        }
    }

    @Override
    public EmployeeSalaryInfo update(EmployeeSalaryInfo employeeSalaryInfo) {
        String id = employeeSalaryInfo.getId();
        if(existsById(id)){
            return employeeSalaryInfoRepository.save(employeeSalaryInfo);
        }else{
            throw  new SCBRecordNotFoundException(new SCBMessage("MSCB10001E", ""+id, UPDATE));
        }
    }

    @Override
    public void deleteById(String id) {
        if(existsById(id)){
            employeeSalaryInfoRepository.deleteById(id);
        }else{
            throw  new SCBRecordNotFoundException(new SCBMessage("MSCB10001E", ""+id, DELETE));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        if(!StringUtils.hasLength(id)){
            throw  new SCBClientException(new SCBMessage("MSCB10001E", ""+id, READ));
        }
        return employeeSalaryInfoRepository.existsById(id);
    }

    @Override
    public EmployeeSalaryInfo getResource(Optional<EmployeeSalaryInfo> employeeSalaryInfo, String... args) {
        return employeeSalaryInfo.orElseThrow(()->new SCBRecordNotFoundException(new SCBMessage("MSCB10001E", args)));
    }

    @Override
    public List<EmployeeSalaryInfo> getResources(List<EmployeeSalaryInfo> employeeSalaryInfos, String... args) {
        if(employeeSalaryInfos.isEmpty()){
            throw  new SCBRecordNotFoundException(new SCBMessage("MSCB10001E", args));
        }
        return employeeSalaryInfos;
    }

    @Override
    public EmployeeSalaryInfo findByIdAndLogin(String id, String login) {
        return getResource(employeeSalaryInfoRepository.findByIdAndLogin(id, login),id+"-"+login, READ);
    }

    @Override
    public List<EmployeeSalaryInfo> findByLogin(String login) {
        return getResources(employeeSalaryInfoRepository.findByLogin(login),login,READ);
    }


    // Relative to User Story 1
    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor=Exception.class)
    public boolean saveAllCsvRecords(List<EmployeeSalaryInfo> employeeSalaryInfoList) {
        Map<String, List<String>> loginIds=new HashMap<>();
        List<String> logins = null;
        for(EmployeeSalaryInfo employeeSalaryInfo: employeeSalaryInfoList){
            // If an entry in the CSV contains an employee ID that already exists in the database, the existing entry in the database is updated.
            // If it does not exist, then a new entry is created. It is possible to replace a login in the database as a result.
            employeeSalaryInfoRepository.save(employeeSalaryInfo);
            log.info(employeeSalaryInfo.getId()+","+employeeSalaryInfo.getLogin()+","+employeeSalaryInfo.getName()+","+employeeSalaryInfo.getSalary());
        }
        return true;
    }

    // Relative to User Story 1
    @Override
    public List<EmployeeSalaryInfo>  validateAllCsvRecords(MultipartFile csvFile){
        Iterable<CSVRecord> csvRecords = CSVFileHelper.getAllCsvRecords(csvFile, ApiConstants.EMPLOYEE_SALARY_INFO_CSV_HEADERS);

        //Declaration
        List<EmployeeSalaryInfo> employeeSalaryInfoList = new ArrayList<EmployeeSalaryInfo>();
        List<String> uniqueIdAndLoginList= new ArrayList<String>();
        Map<String, List<String>> loginIds=new HashMap<>();
        List<String> logins = null;
        int lineNo=0;
        try{
            for (CSVRecord csvRecord : csvRecords) {
                lineNo++;
                if(csvRecord != null &&
                        csvRecord.toString().toLowerCase().contains(CSV_COLUMN1)
                        && csvRecord.toString().toLowerCase().contains(CSV_COLUMN2) ){
                    continue;
                }
                if(csvRecord.size() < 4 || csvRecord.size() > 4){
                    throw  new SCBClientException(new SCBMessage("MAPI10007E", ""+lineNo, CREATE));
                }
                //getting column values from each csv record
                String id= csvRecord.get(CSV_COLUMN1);
                id=id!=null?id.trim():null;

                //Any row starting with “#” is considered a comment and ignored.
                if(StringUtils.hasLength(id) && id.charAt(0) == '#'){
                    continue;
                }

                String login = csvRecord.get(CSV_COLUMN2);
                login=login!=null?login.trim():null;
                String name = csvRecord.get(CSV_COLUMN3);
                name=name!=null?name.trim():null;
                String salary = csvRecord.get(CSV_COLUMN4);
                salary=salary!=null?salary.trim():null;
                String uniqueIdAndLogin=id+"_"+login;
                Double salaryDec = null;
                String errorMessage="";

                //validate All 4 columns must be filled.
                if(!StringUtils.hasLength(id)) errorMessage= CSV_COLUMN1;
                if(!StringUtils.hasLength(login)) errorMessage="".equals(errorMessage)?CSV_COLUMN2:","+ CSV_COLUMN2;
                if(!StringUtils.hasLength(name))  errorMessage="".equals(errorMessage)?CSV_COLUMN3:","+ CSV_COLUMN3;
                if(!StringUtils.hasLength(salary)) {
                    errorMessage="".equals(errorMessage)?CSV_COLUMN4:","+ CSV_COLUMN4;
                }else{
                    try{
                        salaryDec=Double.valueOf(salary);
                        if(salaryDec < 0){
                            throw new NumberFormatException();
                        }
                    }
                    catch (NumberFormatException n){
                        throw new SCBClientException(new SCBMessage("MAPI10008E", salary, ""+lineNo, CREATE));
                    }
                }
                if(StringUtils.hasLength(errorMessage)) {
                    throw new SCBClientException(new SCBMessage("MAPI10004E", ""+lineNo, errorMessage, CREATE));
                }

                // id and login must be unique. They cannot be repeated in another row.
                if(uniqueIdAndLoginList.size() > 0 && uniqueIdAndLoginList.contains(uniqueIdAndLogin)){
                    throw new SCBClientException(new SCBMessage("MAPI10005E", id, login,""+lineNo, CREATE));
                }
                uniqueIdAndLoginList.add(uniqueIdAndLogin);

                // However, this must not conflict with an existing login mapped to a different ID.
                // For a complex use case of swapping logins between 2 IDs.
                // You can accomplish this with 3 uploads: change first ID’s login to temporary one, change 2nd ID’s login
                //to first ID’s, change the first ID to 2nd ID’s previous login.
                // 1, temp1
                // 2, temp1
                // 1, prev2nd1
                List<EmployeeSalaryInfo> employeeSalaryInfosByLogin=  employeeSalaryInfoRepository.findByLogin(login);
                String diffIds=null;
                for(EmployeeSalaryInfo employeeSalaryInfo: employeeSalaryInfosByLogin){
                    if(!employeeSalaryInfo.getId().equals(id)){
                        diffIds=diffIds==null?employeeSalaryInfo.getId():","+employeeSalaryInfo.getId();
                    }
                }
                if(StringUtils.hasLength(diffIds)){
                    throw new SCBClientException(new SCBMessage("MAPI10006E", login, diffIds,""+lineNo, CREATE));
                }

                EmployeeSalaryInfo employeeSalaryInfo = new EmployeeSalaryInfo(id,login,name, salaryDec);
                employeeSalaryInfoList.add(employeeSalaryInfo);
            }

            if(employeeSalaryInfoList.size() == 0){
                throw  new SCBClientException(new SCBMessage("MSCB10003E", "", CREATE));
            }
        }catch (SCBClientException e){
            throw e;
        } catch (Exception e){
            throw  new SCBUnknownException(new SCBMessage("MSCB10004E", e.getMessage(), CREATE));
        }
        return employeeSalaryInfoList;
    }

    @Override
    public List<EmployeeSalaryInfo> findBySalaryBetween(Double minSalary, Double maxSalary, Pageable pageable) {
       return employeeSalaryInfoRepository.findBySalaryBetween(minSalary, maxSalary, pageable).getContent();
    }


    //Relative to User Story 2
    @Override
    public List<EmployeeSalaryInfo>  findByAllFilters(EmployeeCriteria employeeCriteria, EmployeePage employeePage) {
        return employeeCriteriaRepository.findByAllFilters(employeeCriteria, employeePage);
    }


}
