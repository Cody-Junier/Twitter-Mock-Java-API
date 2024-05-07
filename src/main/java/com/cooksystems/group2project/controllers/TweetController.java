package com.cooksystems.group2project.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksystems.group2project.dtos.ContextDto;
import com.cooksystems.group2project.dtos.CredentialsDto;
import com.cooksystems.group2project.dtos.HashtagDto;
import com.cooksystems.group2project.dtos.TweetRequestDto;
import com.cooksystems.group2project.dtos.TweetResponseDto;
import com.cooksystems.group2project.dtos.UserResponseDto;
import com.cooksystems.group2project.services.TweetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetController {

	private final TweetService tweetService;

	@GetMapping
	public List<TweetResponseDto> getAllTweets() {
		return tweetService.getAllTweets();
	}

	@PostMapping
	public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createTweet(tweetRequestDto);
	}

	@GetMapping("/{id}")
	public TweetResponseDto getTweetById(@PathVariable Long id) {
		return tweetService.getTweetById(id);
	}

	@DeleteMapping("/{id}")
	public TweetResponseDto deleteTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.deleteTweetById(id, credentialsDto);
	}

	@PostMapping("/{id}/like")
	public ResponseEntity<Void> likeTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		tweetService.likeTweet(id, credentialsDto);
		return ResponseEntity.noContent().build();
	}
 
	@PostMapping("/{id}/reply")
	public TweetResponseDto replyToTweet(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.replyToTweet(id, tweetRequestDto);
	}

	@PostMapping("/{id}/repost")
	public TweetResponseDto repostTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.getRepostsByTweet(id, credentialsDto);
	}

	@GetMapping("/{id}/tags")
	public List<HashtagDto> getAllHashtagsByTweet(@PathVariable Long id) {
		return tweetService.getAllHashtagsByTweet(id);
	}

	@GetMapping("/{id}/likes")
	public List<UserResponseDto> getUsersByLikedTweet(@PathVariable Long id) {
		return tweetService.getUsersByLikedTweet(id);
	}

	@GetMapping("/{id}/replies")
	public ResponseEntity<List<TweetResponseDto>> getRepliesByTweet(@PathVariable Long id){
		List<TweetResponseDto> replies = tweetService.getRepliesByTweet(id);
		return ResponseEntity.ok(replies);
	}

	@GetMapping("/{id}/reposts")
	public ResponseEntity<List<TweetResponseDto>> getRepostsByTweet(@PathVariable Long id){
		List<TweetResponseDto> reposts = tweetService.getRepostsByTweet(id);
		return ResponseEntity.ok(reposts);
	}

	@GetMapping("/{id}/mentions")
	public List<UserResponseDto> getUsersMentionedInTweet(@PathVariable Long id){
		List<UserResponseDto> mentionedUsers = tweetService.getUsersMentionedInTweet(id);
		return mentionedUsers;
	}
	
	@GetMapping("/{id}/context")
	public ContextDto getTweetContext(@PathVariable Long id) {
		return tweetService.getTweetContext(id);
	}
}
