package com.fronchak.dscatalog.domain.exceptions.handler;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fronchak.dscatalog.domain.exceptions.EntityNotFoundException;
import com.fronchak.dscatalog.domain.exceptions.ExceptionResponse;

@RestController
@ControllerAdvice
public class CustomizeResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e, WebRequest request) {
		ExceptionResponse response = new ExceptionResponse();
		response.setTimestamp(Instant.now());
		response.setStatus(HttpStatus.NOT_FOUND.value());
		response.setError("Entity not found");
		response.setMessage(e.getMessage());
		response.setPath(request.getDescription(false));
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
}
