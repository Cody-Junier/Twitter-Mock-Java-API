package com.cooksystems.group2project.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;

import com.cooksystems.group2project.dtos.HashtagDto;
import com.cooksystems.group2project.entities.Hashtag;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

	HashtagDto entityToDto(Hashtag hashtag);

	List<HashtagDto> entitiesToDtos(List<Hashtag> hashtags);

	Hashtag dtoToEntity(HashtagDto hashtagDto);

}