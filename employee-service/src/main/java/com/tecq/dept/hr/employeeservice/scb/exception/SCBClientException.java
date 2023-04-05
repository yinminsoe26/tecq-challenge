package com.tecq.dept.hr.employeeservice.scb.exception;


import com.tecq.dept.hr.employeeservice.scb.dto.SCBMessage;

public class SCBClientException extends SCBException {
    public SCBClientException() {
        super();
    }

    public SCBClientException(SCBMessage scbMessage) {
        super(scbMessage);
    }

    public SCBClientException(SCBMessage scbMessage, Throwable cause) {
        super(scbMessage,  cause);
    }


    public SCBClientException(Throwable cause) {
        super(cause);
    }

}
