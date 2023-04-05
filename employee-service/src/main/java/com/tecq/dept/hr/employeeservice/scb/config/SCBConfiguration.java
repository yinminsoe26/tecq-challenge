package com.tecq.dept.hr.employeeservice.scb.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableTransactionManagement
public class SCBConfiguration {

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource= new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages", "classpath:label-messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        return messageSource;
    }

}
