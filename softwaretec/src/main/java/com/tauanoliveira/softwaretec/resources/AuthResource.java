package com.tauanoliveira.softwaretec.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.tauanoliveira.softwaretec.dto.EmailDTO;
import com.tauanoliveira.softwaretec.security.JWTUtil;
import com.tauanoliveira.softwaretec.security.UserSS;
import com.tauanoliveira.softwaretec.service.AuthService;
import com.tauanoliveira.softwaretec.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

    @Autowired
    private JWTUtil jwtUtil; 

    @Autowired
    private AuthService authService; //gerador de senha

    @RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
    public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
        UserSS user = UserService.authenticated();//busca usuario logado
        String token = jwtUtil.generateToken(user.getUsername());//gerar token com mesmo usuario
        response.addHeader("Authorization", "Bearer " + token);//devolve o novo token
        response.addHeader("access-control-expose-headers", "Authorization");//authorizacao de token personalizado
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO entityDTO) {
        authService.sendPassword(entityDTO.getEmail());
        return ResponseEntity.noContent().build();
    }
}
