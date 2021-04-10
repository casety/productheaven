package com.productheaven.user.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.websocket.server.PathParam;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productheaven.user.api.schema.response.UserDTO;
import com.productheaven.user.api.schema.response.UsersResponseDTO;
import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.service.UserService;
import com.productheaven.user.service.exception.NoUsersFoundException;

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
	
	@Autowired
	public UserController(UserService userService,ModelMapper modelMapper) {
		this.userService = userService;
		this.modelMapper = modelMapper;
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
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<UsersResponseDTO>  getUserByUserId(@PathParam("userId") @NotBlank @Size(max = 36,min = 36,message = "userId gecersiz") String userId) throws NoUsersFoundException {
		UsersResponseDTO responseDTO = new UsersResponseDTO();
		List<User> allUsers = userService.getAllUsers();
		responseDTO.setUsers(
				allUsers
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList()));
		return new ResponseEntity<>(responseDTO,HttpStatus.OK);
	}

	private UserDTO convertToDto(User user) {
		return modelMapper.map(user, UserDTO.class);
	}
	
}
