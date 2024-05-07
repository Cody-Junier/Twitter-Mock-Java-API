package com.cooksystems.group2project.controllers;

import java.util.List;

import com.cooksystems.group2project.exceptions.BadRequestException;
import com.cooksystems.group2project.exceptions.NotAuthorizedException;
import com.cooksystems.group2project.exceptions.NotFoundException;
import com.cooksystems.group2project.services.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksystems.group2project.dtos.CredentialsDto;
import com.cooksystems.group2project.dtos.TweetResponseDto;
import com.cooksystems.group2project.dtos.UserRequestDto;
import com.cooksystems.group2project.dtos.UserResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public List<UserResponseDto> getAllUsers() {
		return userService.getAllUsers();
	}

//	prev called getOneUser
	@GetMapping("/@{username}")
	public UserResponseDto getUserByUsername(@PathVariable String username) {
		return userService.getUserByUsername(username);
	}

	// Autowired services and mappers

//	@GetMapping
//	public List<UserResponseDto> getAllActiveUsers() {
//		return null;
//
//	}

	@GetMapping("/@{username}/mentions")
	public List<TweetResponseDto> getMentions(@PathVariable String username) {
		return userService.getMentions(username);
	}

	@PostMapping
	public ResponseEntity<?> createUser(@RequestBody UserRequestDto userRequestDto) {
		try {
			UserResponseDto createdUser = userService.createUser(userRequestDto);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);  // Return 201 Created with user data
		} catch (BadRequestException e) {
			return ResponseEntity.badRequest().body(e.getMessage());  // Return 400 Bad Request with error message
		}  catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request.");
		}
	}

	@PatchMapping("/@{username}")
	public UserResponseDto updateUserProfile(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
		return userService.updateUserProfile(username, userRequestDto);
	}


	@DeleteMapping("/@{username}")
	public UserResponseDto deleteUser(@PathVariable String username , @RequestBody CredentialsDto credentialsDto) {
		return userService.deleteUser(username, credentialsDto);
	}

	@PostMapping("/@{username}/follow")
	public void followUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		userService.followUser(username, credentialsDto);
	}
	@GetMapping("/@{username}/feed")
	public List<TweetResponseDto> getFeed(@PathVariable String username) {
		return userService.getFeed(username);
	}

	@PostMapping("/@{username}/unfollow")
	public void unFollowUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		userService.unFollowUser(username, credentialsDto);
	}

	@GetMapping("/@{username}/tweets")
	public List<TweetResponseDto> getTweets(@PathVariable String username){
		return userService.getTweets(username);
	}
	@GetMapping("/@{username}/followers")
	public List<UserResponseDto> getFollowers(@PathVariable String username){
		return userService.getFollowers(username);
	}

	@GetMapping("/@{username}/following")
	public List<UserResponseDto> getFollowing(@PathVariable String username){
		return userService.getFollowing(username);
	}

}

