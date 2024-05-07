package com.cooksystems.group2project.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksystems.group2project.dtos.TweetRequestDto;
import com.cooksystems.group2project.dtos.TweetResponseDto;
import com.cooksystems.group2project.entities.Tweet;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TweetMapper {

	TweetResponseDto entityToDto(Tweet tweet);

	List<TweetResponseDto> entitiesToDtos(List<Tweet> tweets);

	Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto);

	Tweet dtoToEntity(TweetResponseDto tweetResponseDto);



}