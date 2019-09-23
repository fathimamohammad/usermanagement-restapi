package com.pccw.springboot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long objectId;
	private String firstName;
	private String lastName;

	private String emailId;

	private boolean activeUser;

	public User() {
	}

	

	public User(Long objectId, String firstName, String lastName, String emailId, boolean activeUser) {
		super();
		this.objectId = objectId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.activeUser = activeUser;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getEmailId() {
		return emailId;
	}

	public boolean isActiveUser() {
		return activeUser;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setActiveUser(boolean activeUser) {
		this.activeUser = activeUser;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
