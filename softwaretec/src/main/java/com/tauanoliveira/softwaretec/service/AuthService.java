package com.tauanoliveira.softwaretec.service;

import java.util.Random;

import com.tauanoliveira.softwaretec.domain.Funcionario;
import com.tauanoliveira.softwaretec.repository.FuncionarioRepository;
import com.tauanoliveira.softwaretec.service.exception.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

    private Random rand = new Random();

    public void sendPassword(String email) {
        Funcionario funcionario = funcionarioRepository.findByEmail(email);//procura email de Funcionario no repository/banco e coloca em uma var
        if(funcionario == null) {
            throw new ObjectNotFoundException("E-mail não encontrado");
        }

        String newPassword = newPassword();//gera senha aleatoria
        funcionario.setSenha(bCryptPasswordEncoder.encode(newPassword));//set com a nova senha do Funcionario incriptografada com bcrypt
        funcionarioRepository.save(funcionario);//salva Funcionario com a nova senha

        emailService.sendNewPasswordEmail(funcionario, newPassword);//service de email, manda email pro Funcionario
    }

    private String newPassword() {
        char[] vet = new char[10];
        for (int i=0;i<10;i++){
            vet[i] = randomChar();
        }
        return new String(vet);
    }

    private char randomChar() {//gera senha aleatoria de 10 caracteres
        int opt = rand.nextInt(3);
        if(opt == 0) {
            return (char) (rand.nextInt(10) + 48);//gera numero de 0 até 9, olhar unicode-table.com
        }else if(opt == 1) {//gera letra maiuscula 
            return (char) (rand.nextInt(26) + 65);
        }else {//letra minuscula
            return (char) (rand.nextInt(26) + 97);
        }
    }

}
