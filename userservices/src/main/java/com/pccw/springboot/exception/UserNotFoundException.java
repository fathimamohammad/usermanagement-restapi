package com.pccw.springboot.exception;

public class UserNotFoundException extends RuntimeException{
	public UserNotFoundException(Long id) {
		    super("Could not find employee " + id);
		  }
	public UserNotFoundException(String email) {
	    super("Could not find employee " + email);
	  }
}
