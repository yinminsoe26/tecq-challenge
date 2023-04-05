package com.tecq.dept.hr.employeeservice.scb.exception;


import com.tecq.dept.hr.employeeservice.scb.dto.SCBMessage;

public class SCBRecordNotFoundException extends SCBException {
    public SCBRecordNotFoundException() {
        super();
    }

    public SCBRecordNotFoundException(SCBMessage scbMessage) {
        super(scbMessage);
    }

    public SCBRecordNotFoundException(SCBMessage scbMessage, Throwable cause) {
        super(scbMessage,  cause);
    }


    public SCBRecordNotFoundException(Throwable cause) {
        super(cause);
    }

}
