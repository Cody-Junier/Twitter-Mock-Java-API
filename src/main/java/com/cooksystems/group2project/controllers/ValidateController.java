package com.cooksystems.group2project.controllers;

import com.cooksystems.group2project.dtos.HashtagDto;
import com.cooksystems.group2project.entities.Hashtag;
import com.cooksystems.group2project.services.HashtagService;
import com.cooksystems.group2project.services.UserService;
import com.cooksystems.group2project.services.ValidateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
public class ValidateController {
	// Service dependencies would be autowired here

	private final UserService userService;
	private final HashtagService hashtagService;
	private final ValidateService validateService;


	@GetMapping("/tag/exists/{label}")
	public boolean checkHashtagExists(@PathVariable String label) {

		return hashtagService.checkHashtagExists(label);

	}

	@GetMapping("/username/exists/{username}")
	public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
		boolean exists = userService.checkUsernameExists(username);
		return ResponseEntity.ok(exists);
	}

	@GetMapping("/username/available/{username}")
	public ResponseEntity<Boolean> checkUsernameAvailable(@PathVariable String username) {
		boolean available = userService.checkUsernameAvailable(username);
		return ResponseEntity.ok(available);

	}

}
