package com.tauanoliveira.softwaretec.config;

import java.text.ParseException;

import com.tauanoliveira.softwaretec.service.DBService;
import com.tauanoliveira.softwaretec.service.EmailService;
import com.tauanoliveira.softwaretec.service.MockEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {
 
    @Autowired
    private DBService dbService;
    
    @Bean
    public Boolean instantiateDatabase() throws ParseException{//boolean em vez de void para obrigar algum retorno
        dbService.instantiateTestDatabase();
        return true;
    }

    @Bean
    public EmailService emailService() {
        return new MockEmailService();
    }
}