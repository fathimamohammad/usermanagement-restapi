package com.pccw.springboot.userservices;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.pccw.springboot.model.User;

@Component
public class UserResourceAssembler implements ResourceAssembler<User, Resource<User>> {

	  @Override
	  public Resource<User> toResource(User user) {

	    return new Resource<>(user,
	      linkTo(methodOn(UserController.class).getUser(user.getObjectId())).withSelfRel(),
	      linkTo(methodOn(UserController.class).getUsers()).withRel("activeusers"),
	      linkTo(methodOn(UserController.class).getAllUsers()).withRel("allusers"));
	  }
	}


