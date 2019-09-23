package com.pccw.springboot.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateEmailException extends RuntimeException{
	
	public DuplicateEmailException(String email) {
	    super("This email address already exists: " + email);
	  }
}
