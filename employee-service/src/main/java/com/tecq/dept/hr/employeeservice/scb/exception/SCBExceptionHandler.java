package com.tecq.dept.hr.employeeservice.scb.exception;


import com.tecq.dept.hr.employeeservice.scb.dto.SCBErrorDto;
import com.tecq.dept.hr.employeeservice.scb.dto.SCBMessage;
import com.tecq.dept.hr.employeeservice.scb.util.SCBMessageUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class SCBExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private SCBMessageUtil scbMessageUtil;

    @ExceptionHandler({SCBException.class})
    public ResponseEntity<Object> handleSystemException(SCBException ex, WebRequest request) throws Exception {
        //Declaration
        HttpHeaders headers = new HttpHeaders();
        HttpStatus httpStatus=null;
        SCBErrorDto body=new SCBErrorDto();
        HttpServletRequest httpServletRequest =  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();
        String path = ((ServletWebRequest) request).getRequest().getRequestURL().toString();

        if(ex instanceof SCBRecordNotFoundException){
            httpStatus=HttpStatus.NOT_FOUND;
            body.setHttpStatus(httpStatus.toString());
            body.setHttpError(httpStatus.getReasonPhrase());

        }else if(ex instanceof SCBClientException){
            httpStatus=HttpStatus.BAD_REQUEST;
            body.setHttpStatus(httpStatus.toString());
            body.setHttpError(httpStatus.getReasonPhrase());

        }else if(ex instanceof SCBUnknownException){
            httpStatus=HttpStatus.INTERNAL_SERVER_ERROR;
            body.setHttpStatus(httpStatus.toString());
            body.setHttpError(httpStatus.getReasonPhrase());

        }
        if(ex.getMessageInfoList().size() == 1){
            body.setMessage(this.getMessages(ex.getMessageInfoList()).get(0));
        }else  if(ex.getMessageInfoList().size() > 1){
            body.setMessages(this.getMessages(ex.getMessageInfoList()));
        }

        body.setPath(path);

        return  handleExceptionInternal(ex, body,  headers, httpStatus,  request);
    }

    public List<String> getMessages(List<SCBMessage> scbMessageList) {
       List<String> messages= new ArrayList<>();
        scbMessageList.forEach((e)-> messages.add( scbMessageUtil.getMessage(e.getMessageId(), e.getArguments())));
        return messages;
    }
}