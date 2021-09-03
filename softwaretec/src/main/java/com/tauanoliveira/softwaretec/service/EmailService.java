package com.tauanoliveira.softwaretec.service;

import javax.mail.internet.MimeMessage;

import com.tauanoliveira.softwaretec.domain.Funcionario;
import com.tauanoliveira.softwaretec.domain.OrdemServico;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    
    void sendOrderConfirmationEmail(OrdemServico entity);
    void sendOrderComcluidoEmail(OrdemServico entity);
    void sendEmail(SimpleMailMessage msg);

    void sendOrderConfirmationHtmlEmail(OrdemServico entity);
    void sendOrderComcluidoHtmlEmail(OrdemServico entity);
    void sendHtmlEmail(MimeMessage msg);

    void sendNewPasswordEmail(Funcionario funcionario, String newPassword);
}
