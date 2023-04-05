package com.tecq.dept.hr.employeeservice.scb.controller;


import com.tecq.dept.hr.employeeservice.scb.dto.SCBMessage;
import com.tecq.dept.hr.employeeservice.scb.util.SCBMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public abstract class SCBController {
    @Autowired
    private SCBMessageUtil scbMessageUtil;

    public List<String> getMessages(List<SCBMessage> scbMessageList) {
        List<String> messages= new ArrayList<>();
        scbMessageList.forEach((e)->
        {
            String message= scbMessageUtil.getMessage(e.getMessageId(), e.getArguments());
            messages.add(message);
            log.info(e.getMessageId() +" args "+ Arrays.toString(e.getArguments()));
        });
        return messages;
    }

    public String getMessage(SCBMessage scbMessage) {
        return scbMessageUtil.getMessage(scbMessage.getMessageId(), scbMessage.getArguments());
    }
}
