package com.productheaven.user.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productheaven.user.api.schema.response.UserResponseDTO;
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

	private UserResponseDTO convertToDto(User post) {
		return modelMapper.map(post, UserResponseDTO.class);
	}

	
}
