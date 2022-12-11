package com.fronchak.dscatalog.domain.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationExceptionResponse extends ExceptionResponse {

	private static final long serialVersionUID = 1L;
	
	private List<FieldMessage> errors = new ArrayList<>();

	public List<FieldMessage> getErrors() {
		return errors;
	}
	
	public void addError(FieldMessage error) {
		errors.add(error);
	}
	
	public void addError(String fieldName, String message) {
		addError(new FieldMessage(fieldName, message));
	}
}
