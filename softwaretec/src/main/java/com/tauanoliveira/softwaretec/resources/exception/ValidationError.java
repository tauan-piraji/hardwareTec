package com.tauanoliveira.softwaretec.resources.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {//estend os campos status, msg e timeStamp de StandardError

	private static final long serialVersionUID = 1L;

	private List<FieldMessage> list = new ArrayList<>();//Lista de erros do FildMessage
	
	public ValidationError(Long timestamp, Integer status, String error, String message, String path) {
		super(timestamp, status, error, message, path);
	}

	public List<FieldMessage> getList() {
		return list;
	}

	public void addError(String fieldName, String message) {
		list.add(new FieldMessage(fieldName, message));
	}
	
}