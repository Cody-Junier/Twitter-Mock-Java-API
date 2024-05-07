package com.cooksystems.group2project.services;

import java.util.List;

import com.cooksystems.group2project.dtos.HashtagDto;
import com.cooksystems.group2project.dtos.TweetResponseDto;

public interface HashtagService {

	List<HashtagDto> getAllHashtags();
//
//	List<TweetResponseDto> getTweetsByHashtag;

    boolean checkHashtagExists(String label);

	List<TweetResponseDto> getTweetsByHashtag(String label);


}
