package com.tecq.dept.hr.employeeservice.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecq.dept.hr.employeeservice.api.dto.EmployeeCriteria;
import com.tecq.dept.hr.employeeservice.api.dto.EmployeePage;
import com.tecq.dept.hr.employeeservice.api.entity.EmployeeSalaryInfo;
import com.tecq.dept.hr.employeeservice.api.service.EmployeeSalaryInfoService;
import com.tecq.dept.hr.employeeservice.api.util.ApiConstants;
import com.tecq.dept.hr.employeeservice.api.util.CSVFileHelper;
import com.tecq.dept.hr.employeeservice.scb.config.SCBConfiguration;
import com.tecq.dept.hr.employeeservice.scb.dto.SCBMessage;
import com.tecq.dept.hr.employeeservice.scb.util.SCBMessageUtil;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = EmployeeController.class)
class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private Iterable<CSVRecord> csvRecords;
    @MockBean
    private SCBMessageUtil scbMessageUtil;
    @MockBean
    private EmployeeSalaryInfoService employeeSalaryInfoService;

    private static final String CLASSPATH_URL_PREFIX="classpath:";
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private ObjectMapper objectMapper;



    List<EmployeeSalaryInfo> employeeSalaryInfos=new ArrayList<>();

    EmployeeSalaryInfo employeeSalaryInfo1 = null;
    EmployeeSalaryInfo employeeSalaryInfo2= null;
    EmployeeSalaryInfo employeeSalaryInfo3= null;
    @BeforeEach
    void setUp() {
        employeeSalaryInfo1 = EmployeeSalaryInfo.builder()
                .id("e0001").login("hpotter").name("Harry Potter").salary(1234.00)
                .build();
        employeeSalaryInfo2 = EmployeeSalaryInfo.builder()
                .id("e0002").login("rwesley").name("Ron Weasley").salary(19234.50)
                .build();
        employeeSalaryInfo3 = EmployeeSalaryInfo.builder()
                .id("e0003").login("ssnape").name("Severus Snape").salary(4000.00)
                .build();

        employeeSalaryInfos.add(employeeSalaryInfo1);
        employeeSalaryInfos.add(employeeSalaryInfo2);
        employeeSalaryInfos.add(employeeSalaryInfo3);

        when(scbMessageUtil.getMessage(any(String.class), any(String[].class))).thenReturn("Error Encountered");
    }

    @Test
    void upload_csv_success() throws Exception {
        //preparing MultipartFile to pass
        String path = "data/1_SuccessfulWithComment.csv";
        Resource resource = resourceLoader.getResource(CLASSPATH_URL_PREFIX + path);
        byte[] fileContent = resource.getContentAsByteArray();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",resource.getFilename(),
                MediaType.MULTIPART_FORM_DATA.toString(), resource.getContentAsByteArray());

        given(employeeSalaryInfoService.validateAllCsvRecords(mockMultipartFile))
                .willReturn(employeeSalaryInfos);

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/users/upload")
                        .file(mockMultipartFile);
        this.mockMvc.perform(builder).andExpect(ok)
                .andDo(print());
    }
    @Test
    void upload_no_csv_fail() throws Exception {
        //preparing MultipartFile to pass
        String path = "data/WrongFileExtension.txt";
        Resource resource = resourceLoader.getResource(CLASSPATH_URL_PREFIX + path);
        byte[] fileContent = resource.getContentAsByteArray();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",resource.getFilename(),
                MediaType.MULTIPART_FORM_DATA.toString(), resource.getContentAsByteArray());

        given(employeeSalaryInfoService.validateAllCsvRecords(mockMultipartFile))
                .willReturn(employeeSalaryInfos);

        ResultMatcher badRequest = MockMvcResultMatchers.status().isBadRequest();
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/users/upload")
                        .file(mockMultipartFile);
        this.mockMvc.perform(builder).andExpect(badRequest)
                .andDo(print());
    }
    @Test
    void getUsers_success() throws Exception {
        ///users?minSalary=0&maxSalary=4000&offset=0&limit=30&sort=+name
        given(employeeSalaryInfoService.findByAllFilters(any(EmployeeCriteria.class), any(EmployeePage.class))).willReturn(employeeSalaryInfos);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("minSalary", "0");
        requestParams.add("maxSalary", "4000");
        requestParams.add("offset", "0");
        requestParams.add("limit", "30");
        requestParams.add("sort", "id");
        mockMvc.perform(get("/users").params(requestParams))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    void getUsers_missing_minSalary() throws Exception {
        ///users?minSalary=0&maxSalary=4000&offset=0&limit=30&sort=+name
        given(employeeSalaryInfoService.findByAllFilters(any(EmployeeCriteria.class), any(EmployeePage.class))).willReturn(employeeSalaryInfos);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
     //   requestParams.add("minSalary", "0");
        requestParams.add("maxSalary", "4000");
        requestParams.add("offset", "0");
        requestParams.add("limit", "30");
        requestParams.add("sort", "id");
        mockMvc.perform(get("/users").params(requestParams))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void getUsers_missing_maxSalary() throws Exception {
        ///users?minSalary=0&maxSalary=4000&offset=0&limit=30&sort=+name
        given(employeeSalaryInfoService.findByAllFilters(any(EmployeeCriteria.class), any(EmployeePage.class))).willReturn(employeeSalaryInfos);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("minSalary", "0");
        //requestParams.add("maxSalary", "4000");
        requestParams.add("offset", "0");
        requestParams.add("limit", "30");
        requestParams.add("sort", "id");
        mockMvc.perform(get("/users").params(requestParams))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void getUsers_missing_offset() throws Exception {
        ///users?minSalary=0&maxSalary=4000&offset=0&limit=30&sort=+name
        given(employeeSalaryInfoService.findByAllFilters(any(EmployeeCriteria.class), any(EmployeePage.class))).willReturn(employeeSalaryInfos);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("minSalary", "0");
        requestParams.add("maxSalary", "4000");
       // requestParams.add("offset", "0");
        requestParams.add("limit", "30");
        requestParams.add("sort", "id");
        mockMvc.perform(get("/users").params(requestParams))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void getUsers_missing_limit() throws Exception {
        ///users?minSalary=0&maxSalary=4000&offset=0&limit=30&sort=+name
        given(employeeSalaryInfoService.findByAllFilters(any(EmployeeCriteria.class), any(EmployeePage.class))).willReturn(employeeSalaryInfos);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("minSalary", "0");
        requestParams.add("maxSalary", "4000");
        requestParams.add("offset", "0");
        //fixed default value in request param
        //requestParams.add("limit", "30");
        requestParams.add("sort", "id");
        mockMvc.perform(get("/users").params(requestParams))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getUsers_missing_sort() throws Exception {
        ///users?minSalary=0&maxSalary=4000&offset=0&limit=30&sort=+name
        given(employeeSalaryInfoService.findByAllFilters(any(EmployeeCriteria.class), any(EmployeePage.class))).willReturn(employeeSalaryInfos);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("minSalary", "0");
        requestParams.add("maxSalary", "4000");
        requestParams.add("offset", "0");
        //fixed default value in request param
        requestParams.add("limit", "30");
       // requestParams.add("sort", "id");
        mockMvc.perform(get("/users").params(requestParams))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void getUsers_sort_wrong_column() throws Exception {
        ///users?minSalary=0&maxSalary=4000&offset=0&limit=30&sort=+name
        given(employeeSalaryInfoService.findByAllFilters(any(EmployeeCriteria.class), any(EmployeePage.class))).willReturn(employeeSalaryInfos);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("minSalary", "0");
        requestParams.add("maxSalary", "4000");
        requestParams.add("offset", "0");
        //fixed default value in request param
        requestParams.add("limit", "30");
        requestParams.add("sort", "xx");
        mockMvc.perform(get("/users").params(requestParams))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void getUsers_salary_wrong_format() throws Exception {
        ///users?minSalary=0&maxSalary=4000&offset=0&limit=30&sort=+name
        given(employeeSalaryInfoService.findByAllFilters(any(EmployeeCriteria.class), any(EmployeePage.class))).willReturn(employeeSalaryInfos);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("minSalary", "S$0");
        requestParams.add("maxSalary", "S$4000");
        requestParams.add("offset", "0");
        //fixed default value in request param
        requestParams.add("limit", "30");
        requestParams.add("sort", "+id");
        mockMvc.perform(get("/users").params(requestParams))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void create() throws Exception {
        //preparing test data
        String employeeId = "e0004";
        EmployeeSalaryInfo newEmployee = EmployeeSalaryInfo.builder()
                .login("rhagrid").name("Rubeus Hagrid").salary(3999.999)
                .build();
        //mock method
        given(employeeSalaryInfoService.save(any(EmployeeSalaryInfo.class))).willReturn(newEmployee);
        // when -  action or the behaviour
        ResultActions response = mockMvc.perform(post("/users/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee)));
        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(newEmployee.getId())))
                .andExpect(jsonPath("$.login", is(newEmployee.getLogin())))
                .andExpect(jsonPath("$.name", is(newEmployee.getName())))
                .andExpect(jsonPath("$.salary", is(newEmployee.getSalary())));
    }

    @Test
    void read() throws Exception {
        //preparing test data
        String employeeId = "e0001";
        //mock method
        given(employeeSalaryInfoService.findById(any(String.class))).willReturn(employeeSalaryInfo1);
        // when -  action or the behaviour
        ResultActions response = mockMvc.perform(get("/users/{id}", employeeId));
        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(employeeSalaryInfo1.getId())))
                .andExpect(jsonPath("$.login", is(employeeSalaryInfo1.getLogin())))
                .andExpect(jsonPath("$.name", is(employeeSalaryInfo1.getName())))
                .andExpect(jsonPath("$.salary", is(employeeSalaryInfo1.getSalary())));

    }

    @Test
    void update() throws Exception {
        //preparing test data
        String employeeId = "e0001";
        EmployeeSalaryInfo updatedEmployee= EmployeeSalaryInfo.builder()
                .id(employeeId).login("hpotter").name("Harry Potter").salary(1234.00)
                .build();
        //mock method
        given(employeeSalaryInfoService.update(any(EmployeeSalaryInfo.class))).willReturn(updatedEmployee);
        // when -  action or the behaviour
        ResultActions response = mockMvc.perform(patch("/users/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(updatedEmployee.getId())))
                .andExpect(jsonPath("$.login", is(updatedEmployee.getLogin())))
                .andExpect(jsonPath("$.name", is(updatedEmployee.getName())))
                .andExpect(jsonPath("$.salary", is(updatedEmployee.getSalary())));
    }

    @Test
    void deleteTest() throws Exception {
        //preparing test data
        String employeeId = "e0001";

        //mock method
        doNothing().when(employeeSalaryInfoService).deleteById(employeeId);
        // when -  action or the behaviour
        // when -  action or the behaviour
        ResultActions response = mockMvc.perform(delete("/users/{id}", employeeId));
        // then - verify the output
        response.andExpect(status().isOk());
        // then - verify the output
        verify(employeeSalaryInfoService, times(1)).deleteById(employeeId);
    }
}