package com.tecq.dept.hr.employeeservice.scb.exception;


import com.tecq.dept.hr.employeeservice.scb.dto.SCBMessage;

public class SCBUnknownException extends SCBException {
    public SCBUnknownException() {
        super();
    }

    public SCBUnknownException(SCBMessage scbMessage) {
        super(scbMessage);
    }

    public SCBUnknownException(SCBMessage scbMessage, Throwable cause) {
        super(scbMessage,  cause);
    }


    public SCBUnknownException(Throwable cause) {
        super(cause);
    }

}
