package com.cooksystems.group2project.mapper;

import org.mapstruct.Mapper;

import com.cooksystems.group2project.dtos.CredentialsDto;
import com.cooksystems.group2project.entities.Credentials;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
	CredentialsDto entityToDto(Credentials credentials);

	Credentials dtoToEntity(CredentialsDto credentialsDto);
}