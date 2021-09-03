package com.tauanoliveira.softwaretec.service.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.tauanoliveira.softwaretec.dto.ClienteNewDTO;//FALTA CRIAR
import com.tauanoliveira.softwaretec.domain.Cliente;
import com.tauanoliveira.softwaretec.domain.enums.TipoCliente;
import com.tauanoliveira.softwaretec.repository.ClienteRepository;
import com.tauanoliveira.softwaretec.resources.exception.FieldMessage;
import com.tauanoliveira.softwaretec.service.validation.utils.BR;

import org.springframework.beans.factory.annotation.Autowired;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void initialize(ClienteInsert ann) {
    }
    @Override
    public boolean isValid(ClienteNewDTO objDTO, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();
        // inclua os testes aqui, inserindo erros na lista

        if(objDTO.getTipoCliente().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDTO.getCpfOuCnpj())){
            list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
        }

        if(objDTO.getTipoCliente().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDTO.getCpfOuCnpj())){
            list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
        }

        Cliente aux = clienteRepository.findByEmail(objDTO.getEmail());
        if(aux != null){
            list.add(new FieldMessage("email", "E-mail já existente"));
        }

        Cliente X = clienteRepository.findByCpfOuCnpj(objDTO.getCpfOuCnpj());
        if(X != null){
            if(objDTO.getTipoCliente().equals(TipoCliente.PESSOAFISICA)) {
                list.add(new FieldMessage("cpfOuCnpj", "CPF já existente"));
            }else{
                list.add(new FieldMessage("cpfOuCnpj", "CNPJ já existente"));
            }
        }

        //
        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFildName()).addConstraintViolation();
        }
        return list.isEmpty();
    }
}