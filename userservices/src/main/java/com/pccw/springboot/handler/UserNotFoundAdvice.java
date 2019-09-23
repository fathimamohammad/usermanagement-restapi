package com.pccw.springboot.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import com.pccw.springboot.exception.DuplicateEmailException;
import com.pccw.springboot.exception.UserNotFoundException;

@ControllerAdvice
public class UserNotFoundAdvice {
	@ResponseBody
	  @ExceptionHandler(UserNotFoundException.class)
	  @ResponseStatus(HttpStatus.NOT_FOUND)
	  String UserNotFoundHandler(UserNotFoundException ex) {
	    return ex.getMessage();
	  }
	
	@ResponseBody
	  @ExceptionHandler(DuplicateEmailException.class)
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  String DuplicateEmailFoundHandler(DuplicateEmailException ex) {
	    return ex.getMessage();
	  }
}
