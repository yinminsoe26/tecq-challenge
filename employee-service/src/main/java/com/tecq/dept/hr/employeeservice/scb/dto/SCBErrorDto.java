package com.tecq.dept.hr.employeeservice.scb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonPropertyOrder(value = {"date", "httpStatus", "httpError", "messages","message", "path"})
public class SCBErrorDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonProperty(value="timestamp")
    private LocalDateTime date=LocalDateTime.now();

    @JsonProperty(value = "status")
    private String httpStatus;
    @JsonProperty(value = "error")
    private String httpError;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "messages")
    private List<String> messages;

    @JsonProperty(value = "message")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    private String path;

}
