package com.tecq.dept.hr.employeeservice.scb.dto;


public class SCBMessage {
    private String messageId;
    private String[] arguments;
    public SCBMessage() {
    }

    public SCBMessage(String messageId, String... arguments) {
        this.messageId = messageId;
        this.arguments = arguments;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String... arguments) {
        this.arguments = arguments;
    }
}
