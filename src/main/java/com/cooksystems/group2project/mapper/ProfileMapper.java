package com.cooksystems.group2project.mapper;

import org.mapstruct.Mapper;

import com.cooksystems.group2project.dtos.ProfileDto;
import com.cooksystems.group2project.entities.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

	ProfileDto entityToDto(Profile profile);

	Profile dtoToEntity(ProfileDto profileDto);

}
