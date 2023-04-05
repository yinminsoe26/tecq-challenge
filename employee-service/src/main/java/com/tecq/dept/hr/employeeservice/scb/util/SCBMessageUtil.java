package com.tecq.dept.hr.employeeservice.scb.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class SCBMessageUtil {

    @Autowired
    private MessageSource messageSource;

    private Locale defaultLocale=Locale.getDefault();

    private final String DEFAULT_MESSAGE="NO MESSAGE ID" ;

    public SCBMessageUtil(){
    }
    public SCBMessageUtil(MessageSource messageSource){
        this.messageSource=messageSource;
    }
    public String getMessage(Locale locale, String code, String... args){
        if (locale == null) {
            locale = this.defaultLocale;
        }
         return messageSource.getMessage(code, args, DEFAULT_MESSAGE, locale);
    }
    public String getMessage(String code, String... args){
        return messageSource.getMessage(code, args, DEFAULT_MESSAGE, this.defaultLocale);
    }

    
}
