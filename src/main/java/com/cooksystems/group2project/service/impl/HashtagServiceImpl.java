package com.cooksystems.group2project.service.impl;

import com.cooksystems.group2project.exceptions.BadRequestException;
import org.springframework.stereotype.Service;
import com.cooksystems.group2project.dtos.HashtagDto;
import com.cooksystems.group2project.dtos.TweetResponseDto;
import com.cooksystems.group2project.entities.Hashtag;
import com.cooksystems.group2project.entities.Tweet;
import com.cooksystems.group2project.exceptions.NotFoundException;
import com.cooksystems.group2project.mapper.HashtagMapper;
import com.cooksystems.group2project.mapper.TweetMapper;
import com.cooksystems.group2project.repositories.HashtagRepository;
import com.cooksystems.group2project.services.HashtagService;

import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

	private final HashtagRepository hashtagRepository;

	private final HashtagMapper hashtagMapper;

	private final TweetMapper tweetMapper;

	@Override
	public boolean checkHashtagExists(String label) {
		boolean checker = false;
		// Remove the hash symbol from the start of the label if it's there
//		String normalizedLabel = label.startsWith("#") ? label.substring(1) : label;
		if(label != null) {
			List<HashtagDto> hashTagList = getAllHashtags();
			for( HashtagDto indHashtag : hashTagList){
				String indHashTagLabel = indHashtag.getLabel();
				if(indHashTagLabel.equals(label)){
					checker = true;
					return checker;
				}
			}
		} else {
			throw new BadRequestException("no actual label provided to check if hashtag exists");
		}
		return checker;
	}

	@Override
	public List<HashtagDto> getAllHashtags() {
		// TODO Auto-generated method stub
		return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
	}

	@Override
	public List<TweetResponseDto> getTweetsByHashtag(String label) {
//		need to make a list of hashtags with given label
//		if it's in the ** service, you're golden
//		inject tweetmapper
		Optional<Hashtag> h = hashtagRepository.findByLabel("#" + label);
		if (h.isEmpty()) {
			throw new NotFoundException("Not found");
		}
//		usable stream to filterout 
		List<Tweet> tweets = h.get().getTweets().stream().filter(tweet -> !tweet.isDeleted())
				.sorted(Comparator.comparing(Tweet::getPosted)).collect(Collectors.toList());
		return tweetMapper.entitiesToDtos(tweets);
	}

}
