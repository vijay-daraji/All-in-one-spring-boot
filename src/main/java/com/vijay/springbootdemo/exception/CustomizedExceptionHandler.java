package com.vijay.springbootdemo.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vijay.springbootdemo.util.ErrorResponse;

@ControllerAdvice
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(UserAlreadyExistException.class)
	public final ResponseEntity<Object> userAlreadyExistExceptionHandler(UserAlreadyExistException ex, WebRequest request){
		ErrorResponse error = new ErrorResponse(ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public final ResponseEntity<Object> userNotFoundExceptionHandler(UserNotFoundException ex, WebRequest request){
		ErrorResponse error = new ErrorResponse(ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	    ErrorResponse error = new ErrorResponse("Validation Failed", ex.getBindingResult().toString());
	    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	  }
	
}
