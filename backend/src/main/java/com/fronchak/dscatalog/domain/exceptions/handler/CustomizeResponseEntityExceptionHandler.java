package com.fronchak.dscatalog.domain.exceptions.handler;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fronchak.dscatalog.domain.exceptions.ResourceNotFoundException;
import com.fronchak.dscatalog.domain.exceptions.DatabaseException;
import com.fronchak.dscatalog.domain.exceptions.ExceptionResponse;

@RestController
@ControllerAdvice
public class CustomizeResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		ExceptionResponse response = makeResponse(e, request, status, "Resource not found");
		return ResponseEntity.status(status).body(response);
	}
	
	private ExceptionResponse makeResponse(Exception e, WebRequest request, HttpStatus status, String error) {
		ExceptionResponse response = new ExceptionResponse();
		response.setTimestamp(Instant.now());
		response.setStatus(status.value());
		response.setError(error);
		response.setMessage(e.getMessage());
		response.setPath(request.getDescription(false));
		return response;
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<ExceptionResponse> handleDatabaseException(DatabaseException e, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ExceptionResponse response = makeResponse(e, request, status, "Database error");
		return ResponseEntity.status(status).body(response);
	}
}
