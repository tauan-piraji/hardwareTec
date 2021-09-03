package com.tauanoliveira.softwaretec.service;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.tauanoliveira.softwaretec.domain.Funcionario;
import com.tauanoliveira.softwaretec.domain.OrdemServico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


public abstract class AbstractEmailService implements EmailService {
    
    @Value("${default.sender}")
    private String sender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    //MockEmailService
    @Override
    public void sendOrderConfirmationEmail(OrdemServico entity) {
        SimpleMailMessage sm = prepareSimpleMailMessageFromOrdemServico(entity);
        sendEmail(sm);
    }

    @Override
    public void sendOrderComcluidoEmail(OrdemServico entity) {
        SimpleMailMessage sm = prepareSimpleMailMessageFromOrdemConcluida(entity);
        sendEmail(sm);
    }

    protected SimpleMailMessage prepareSimpleMailMessageFromOrdemServico(OrdemServico entity) {
        SimpleMailMessage sm = new SimpleMailMessage();
        sm.setTo(entity.getCliente().getEmail());
        sm.setFrom(sender);
        sm.setSubject("Favor confirmar pedido, Código: " + entity.getId());
        sm.setSentDate(new Date(System.currentTimeMillis()));
        sm.setText(entity.toString());
        return sm;
    } 

    protected SimpleMailMessage prepareSimpleMailMessageFromOrdemConcluida(OrdemServico entity) {
        SimpleMailMessage sm = new SimpleMailMessage();
        sm.setTo(entity.getCliente().getEmail());
        sm.setFrom(sender);
        sm.setSubject("Favor confirmar pedido, Código: " + entity.getId());
        sm.setSentDate(new Date(System.currentTimeMillis()));
        sm.setText("Ordem de servico concluida, Favor buscar o aparelho na filial" + "\n" 
                        + "Ligue ou mande mensagem para *****-**** para mais duvidas");
        return sm;
    } 
    
    //SmtpEmailService
    @Override
    public void sendOrderConfirmationHtmlEmail(OrdemServico entity) {
        try{
            MimeMessage mm = prepareMimeMessageFromOrdemServico(entity);
            sendHtmlEmail(mm);
        }catch(MessagingException e) {
            sendOrderConfirmationEmail(entity);
        }
    }

    @Override
    public void sendOrderComcluidoHtmlEmail(OrdemServico entity) {
        try{
            MimeMessage mm = prepareMimeMessageFromOrdemConclida(entity);
            sendHtmlEmail(mm);
        }catch(MessagingException e) {
            sendOrderComcluidoEmail(entity);
        }
    }
    
    protected String htmlFromTemplateConfirmaOS(OrdemServico OS) {
        String URL = "http://localhost:8080/ordemServicos/";
        Context context = new Context();
        context.setVariable("ordemServico", OS);
        context.setVariable("url", URL + OS.getId()  + "/itemAprovada/");
        context.setVariable("url2", URL + OS.getId()  + "/itemAprovada/");
        System.out.println(templateEngine.process("email/confirmaOS", context));
        return templateEngine.process("email/confirmaOS", context);
    }
    
    protected MimeMessage prepareMimeMessageFromOrdemServico(OrdemServico OS) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
        mmh.setTo(OS.getCliente().getEmail());
        mmh.setFrom(sender);
        mmh.setSubject("Favor confirmar pedido, Código: " + OS.getId());
        mmh.setSentDate(new Date(System.currentTimeMillis()));
        mmh.setText(htmlFromTemplateConfirmaOS(OS), true);
        return mimeMessage;
    } 

    protected String htmlFromTemplateConcluidaOS(OrdemServico OS) {
        Context context = new Context();
        context.setVariable("ordemServico", OS);
        System.out.println(templateEngine.process("email/confirmaOS", context));
        return templateEngine.process("email/confirmaOS", context);
    }
    
    protected MimeMessage prepareMimeMessageFromOrdemConclida(OrdemServico OS) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
        mmh.setTo(OS.getCliente().getEmail());
        mmh.setFrom(sender);
        mmh.setSubject("Favor confirmar pedido, Código: " + OS.getId());
        mmh.setSentDate(new Date(System.currentTimeMillis()));
        mmh.setText(htmlFromTemplateConcluidaOS(OS), true);
        return mimeMessage;
    } 

    //New Password
    @Override
    public void sendNewPasswordEmail(Funcionario funcionario, String newPassword) {
        SimpleMailMessage sm = prepareNewPasswordEmail(funcionario, newPassword);
        sendEmail(sm);
    }

    protected SimpleMailMessage prepareNewPasswordEmail(Funcionario funcionario, String newPassword) {
        SimpleMailMessage sm = new SimpleMailMessage();
        sm.setTo(funcionario.getEmail());//email destinatario
        sm.setFrom(sender);//Email remetente
        sm.setSubject("Solicitação de nova senha");//assunto do email
        sm.setSentDate(new Date(System.currentTimeMillis()));//data do email
        sm.setText("nova senha: " + newPassword);//Conteudo do email
        return sm;
    }
}