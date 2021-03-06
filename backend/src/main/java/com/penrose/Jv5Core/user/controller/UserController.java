package com.penrose.Jv5Core.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.penrose.Jv5Core.Utill.JSONUtil;
import com.penrose.Jv5Core.email.service.EmailService;
import com.penrose.Jv5Core.dto.UserResponse;
import com.penrose.Jv5Core.model.User;
import com.penrose.Jv5Core.user.service.UserService;

@RestController
@RequestMapping(path="/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService; 
	
	@Autowired 
	EmailService emailService;

	@GetMapping(value="", produces="application/json")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<User> getAllUsers() {		
		List<User> allUsers = userService.getAllUsers(); 
		LOGGER.debug("*** Returning userResources={}", allUsers);
		return allUsers;
	}
	
	@PostMapping(value="/registerUser", produces="application/json")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public User registerUser(@RequestBody User user) throws Exception {
		LOGGER.info("Registering User: {}", JSONUtil.convertToJson(user));
		User registeredUser = userService.registerUser(user);
		emailService.sendEmailToRegisteredUser(registeredUser);
		return registeredUser;
	}
	
	@PostMapping(value="/loginUser", produces="application/json")
	@ResponseBody
	public ResponseEntity<User> loginUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
		User isValidUser = userService.verifyUserCredentials(user);
		//userService.loginUser(user);
		if(isValidUser == null) {
			//response.SC_UNAUTHORIZED;
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		request.getSession().setAttribute("user", user.getAlias());
		return new ResponseEntity<>(isValidUser, HttpStatus.OK);
	}
	
	@PostMapping(value="/logoutUser", produces="application/json")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public void logoutUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("user");
		request.getSession().invalidate();
	}
	
	@GetMapping(value="/getUserProfile/{userId}", produces="application/json")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public UserResponse getUserProfile(@PathVariable("userId") Long userId, HttpServletRequest request, HttpServletResponse response) {
		UserResponse userResponse = userService.getUserProfile(userId);
		return userResponse;
	}
	
	@GetMapping(value="/verify", produces="application/json")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<User> verifyUser(@RequestParam String email, @RequestParam String password, HttpServletRequest request, HttpServletResponse response) {
		User tempUser = new User();
		tempUser.setEmail(email);
		tempUser.setPassword(password);
		User isValidUser = userService.verifyUserCredentials(tempUser);
		//userService.loginUser(user);
		if(isValidUser == null) {
			//response.SC_UNAUTHORIZED;
			LOGGER.debug("***User was not verified");
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		LOGGER.debug("***User was verified");
		return new ResponseEntity<>(isValidUser, HttpStatus.OK);
	}
}
