package com.cooksystems.group2project.services;

import java.util.List;

import com.cooksystems.group2project.dtos.ContextDto;
import com.cooksystems.group2project.dtos.CredentialsDto;
import com.cooksystems.group2project.dtos.HashtagDto;
import com.cooksystems.group2project.dtos.TweetRequestDto;
import com.cooksystems.group2project.dtos.TweetResponseDto;
import com.cooksystems.group2project.dtos.UserResponseDto;
import com.cooksystems.group2project.entities.Tweet;

public interface TweetService {
	
	Tweet getTweet(Long tweetId);

	List<TweetResponseDto> findAfterChain(Tweet tweet);

	List<TweetResponseDto> findBeforeChain(Tweet targetTweet);

	List<TweetResponseDto> getAllTweets();

	TweetResponseDto getTweetById(Long id);

	TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto);

	List<HashtagDto> getAllHashtagsByTweet(Long id);

	List<UserResponseDto> getUsersByLikedTweet(Long id);

	void likeTweet(Long id, CredentialsDto credentialsDto);

	List<TweetResponseDto> getRepliesByTweet(Long id);

	List<TweetResponseDto> getRepostsByTweet(Long id);

	List<UserResponseDto> getUsersMentionedInTweet(Long id);

	TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

	TweetResponseDto replyToTweet(Long id, TweetRequestDto tweetRequestDto);

	TweetResponseDto getRepostsByTweet(Long id, CredentialsDto credentialsDto);

	ContextDto getTweetContext(Long id);


}
