package com.cooksystems.group2project.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksystems.group2project.dtos.HashtagDto;
import com.cooksystems.group2project.dtos.TweetResponseDto;
import com.cooksystems.group2project.services.HashtagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class HashtagController {

	private final HashtagService hashtagService;

	@GetMapping
	public List<HashtagDto> getAllHashtags() {
		return hashtagService.getAllHashtags();

	}

//	unsure about typing here- should it be a tweet response or hash response?
	@GetMapping(value = "/{label}")
	public List<TweetResponseDto> getTweetsByHashtag(@PathVariable String label) {
		return hashtagService.getTweetsByHashtag(label);
	
	}
	/*
	 * GET tags
	 * 
	 * Retrieves all hashtags tracked by the database.
	 * 
	 * Response ['Hashtag']
	 * 
	 * GET tags/{label}
	 * 
	 * Retrieves all (non-deleted) tweets tagged with the given hashtag label. The
	 * tweets should appear in reverse-chronological order. If no hashtag with the
	 * given label exists, an error should be sent in lieu of a response.
	 * 
	 * A tweet is considered "tagged" by a hashtag if the tweet has content and the
	 * hashtag's label appears in that content following a #
	 * 
	 * Response ['Tweet']
	 * 
	 */

}
