package com.tecq.dept.hr.employeeservice.scb.exception;

import com.tecq.dept.hr.employeeservice.scb.dto.SCBMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SCBException extends RuntimeException{

    private List<SCBMessage> SCBMessageList = new ArrayList<>();

    private List<String> messages;

    public SCBException() {
        super();
    }

    public SCBException(SCBMessage scbMessage) {
        super();
        SCBMessageList.add(scbMessage);
    }


    public SCBException(SCBMessage scbMessage, Throwable cause) {
        super(cause);
        SCBMessageList.add(scbMessage);
    }


    public SCBException(Throwable cause) {
        super(cause);
    }

    public List<SCBMessage> getMessageInfoList(){
        return SCBMessageList;
    }

    public void addMessageInfo(SCBMessage SCBMessage){
        SCBMessageList.add(SCBMessage);
    }

}
