package com.pccw.springboot.userservices;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pccw.springboot.exception.DuplicateEmailException;
import com.pccw.springboot.exception.UserNotFoundException;
import com.pccw.springboot.model.User;
import com.pccw.springboot.persistence.UserRepository;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/usermanagement")
public class UserController {
	private UserRepository userRepository;
	private final UserResourceAssembler assembler;
	private MailService notificationService;

	public UserController(UserRepository userRepository, UserResourceAssembler assembler,
			MailService notificationService) {
		super();
		this.userRepository = userRepository;
		this.assembler = assembler;
		this.notificationService = notificationService;
	}

	

	@PostMapping("/users")
	ResponseEntity<?> newEmployee(@RequestBody User newUser) throws URISyntaxException {
		Resource<User> resource;
		Optional<User> userOpt = userRepository.findAll().stream()
				.filter(a -> a.getEmailId().equalsIgnoreCase(newUser.getEmailId())).findAny();
		if (userOpt.isPresent()) {
			User existingRecord = userOpt.get();
			if (existingRecord.isActiveUser()) {
				throw new DuplicateEmailException(newUser.getEmailId());
			} else {
				existingRecord.setActiveUser(true);
				resource = assembler.toResource(userRepository.save(existingRecord));
			}
		} else {
			resource = assembler.toResource(userRepository.save(newUser));
		}
		try {
			notificationService.sendEmail(newUser);
		} catch (MailException mailException) {
			System.out.println(mailException);
		}
		return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);

	}

	@PutMapping("/users/delete")
	public ResponseEntity<?> deleteMultipleUsers(@RequestBody Object ids) {
		if (ids != null) {
			LinkedHashMap idmap = (LinkedHashMap) ids;
			if (idmap.containsKey("ids")) {
				ArrayList<Integer> idlist = (ArrayList) idmap.get("ids");
				List<User> users = userRepository.findAll();
				idlist.forEach(id -> deactivateUser(users, id));
			}
		}
		return ResponseEntity.noContent().build();
	}

	/*
	 * Softdeleting multiple users, used PUT for this purpose
	 */
	@PutMapping("/users/{id}")
	ResponseEntity<?> replaceUser(@RequestBody User newUser, @PathVariable Long id) throws URISyntaxException {
		User updatedUser = userRepository.findById(new Long(id)).map(user -> {
			user.setEmailId(newUser.getEmailId());
			user.setFirstName(newUser.getFirstName());
			user.setLastName(newUser.getLastName());
			user.setActiveUser(newUser.isActiveUser());
			return userRepository.save(user);
		}).orElseGet(() -> {
			newUser.setObjectId(id);
			return userRepository.save(newUser);
		});

		Resource<User> resource = assembler.toResource(updatedUser);

		return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
	}

	/*
	 * Soft-deleting 1 user
	 */
	@DeleteMapping("/users/{id}")
	ResponseEntity<?> deleteUser(@PathVariable Long id) {

		User updatedUser = userRepository.findById(id).map(user -> {
			user.setActiveUser(false);
			return userRepository.save(user);
		}).orElseThrow(() -> new UserNotFoundException(id));
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/users")
	public Resources<Resource<User>> getUsers() {
		List<Resource<User>> users = userRepository.findAll().stream().filter(user -> user.isActiveUser())
				.map(assembler::toResource).collect(Collectors.toList());

		return new Resources<>(users, linkTo(methodOn(UserController.class).getUsers()).withSelfRel());
	}

	@GetMapping("/users/all")
	public Resources<Resource<User>> getAllUsers() {
		List<Resource<User>> users = userRepository.findAll().stream().map(assembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(users, linkTo(methodOn(UserController.class).getUsers()).withSelfRel());
	}

	@GetMapping("/user/{id}")
	public Resource<User> getUser(@PathVariable Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		return assembler.toResource(user);
	}

	@GetMapping("/user/{email}")
	public User getUserByEmail(@PathVariable String email) {
		return userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
	}

	private boolean isDuplicateEmail(String email) {
		return userRepository.findAll().stream().anyMatch(a -> a.getEmailId().equalsIgnoreCase(email));

	}

	private void deactivateUser(List<User> users, Integer id) {
		users.forEach(user -> deactivateAction(user, new Long(id)));
	}

	private void deactivateAction(User user, Long id) {
		if (user.getObjectId().compareTo(id) == 0) {
			user.setActiveUser(false);
			userRepository.save(user);
		}
	}
}
