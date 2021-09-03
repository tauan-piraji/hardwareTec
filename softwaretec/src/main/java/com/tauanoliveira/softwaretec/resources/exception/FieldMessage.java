package com.tauanoliveira.softwaretec.resources.exception;

import java.io.Serializable;

public class FieldMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String fieldName;//passa o paramentro onde ocorreu o erro              EX: erro no parametro "email"
	private String message;//passa a mensagem de erro criada para esse parametro   EX: menssagem         "Campo obrigatorio"
	
	public FieldMessage(String tipo_cliente_obrigatorio) {
	}

	
	public FieldMessage(String fieldName, String message) {
		this.fieldName = fieldName;
		this.message = message;
	}
	
	public String getFildName() {
		return fieldName;
	}
	
	public void setFildName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}