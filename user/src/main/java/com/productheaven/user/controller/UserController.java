package com.productheaven.user.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.productheaven.user.api.schema.request.UserCreateRequestDTO;
import com.productheaven.user.api.schema.response.UserDTO;
import com.productheaven.user.api.schema.response.UserResponseDTO;
import com.productheaven.user.api.schema.response.UsersResponseDTO;
import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.service.RequestValidationService;
import com.productheaven.user.service.UserService;
import com.productheaven.user.service.exception.InvalidRequestException;
import com.productheaven.user.service.exception.NoUsersFoundException;
import com.productheaven.user.service.exception.UserAlreadyExistsException;
import com.productheaven.user.service.exception.UserNotFoundException;

/**
 * 
 * HTTP RestController for the user operations
 * 
 * @author mkaman
 * 
 * */

@RestController

public class UserController {
	
	private UserService userService;
	
	private ModelMapper modelMapper;
	
	private RequestValidationService validationService;
	
	@Autowired
	public UserController(UserService userService,ModelMapper modelMapper,RequestValidationService validationService) {
		this.userService = userService;
		this.modelMapper = modelMapper;
		this.validationService= validationService;
	}
	
	@GetMapping("/user")
	public ResponseEntity<UsersResponseDTO>  getUsers() throws NoUsersFoundException {
		UsersResponseDTO responseDTO = new UsersResponseDTO();
		List<User> allUsers = userService.getAllUsers();
		responseDTO.setUsers(
				allUsers
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList()));
		return new ResponseEntity<>(responseDTO,HttpStatus.OK);
	}	
	
	@GetMapping("/user/{id}")
	public ResponseEntity<UserResponseDTO> getUserByUserId(@PathVariable("id") String id) throws UserNotFoundException, InvalidRequestException {
		validationService.validateUserId(id);
		User user = userService.getUser(id);
		UserDTO userDto = modelMapper.map(user, UserDTO.class);
		return new ResponseEntity<>(new UserResponseDTO(userDto),HttpStatus.OK);
	}
	
	@GetMapping("/user-by-username/{username}")
	public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable("username") String username) throws UserNotFoundException {
		User user = userService.getUserByUsername(username);
		UserDTO userDto = modelMapper.map(user, UserDTO.class);
		return new ResponseEntity<>(new UserResponseDTO(userDto),HttpStatus.OK);
	}
	
	@PostMapping("/user")
	public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateRequestDTO userRequest) throws UserAlreadyExistsException {
		User entity = convertToEntity(userRequest);
		entity = userService.registerNewUser(entity);
		UserDTO userDto = convertToDto(entity);
		UserResponseDTO responseDTO = new UserResponseDTO(userDto);
		return new ResponseEntity<>(responseDTO,HttpStatus.CREATED);
	}

	private UserDTO convertToDto(User user) {
		return modelMapper.map(user, UserDTO.class);
	}
	
	private User convertToEntity(UserCreateRequestDTO userCreateRequestDTO) {
		return modelMapper.map(userCreateRequestDTO, User.class);
	}
	
}
