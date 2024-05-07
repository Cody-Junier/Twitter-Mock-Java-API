package com.cooksystems.group2project.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksystems.group2project.dtos.UserRequestDto;
import com.cooksystems.group2project.dtos.UserResponseDto;
import com.cooksystems.group2project.entities.User;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class })

public interface UserMapper {

//Since the username is inside the Credentials embeddable class,
// you'll need to specify how MapStruct can access it.
//	The @Mapping annotation tells MapStruct that when  converting from User entity to a UserResponseDto, it should take the username field from the
//	credentials embedded object within the User entity and map it to the username field in the UserResponseDto.
	@Mapping(source = "credentials.username", target = "username")
	UserResponseDto entityToDto(User user);

	List<UserResponseDto> entitiesToDtos(List<User> users);

	User requestDtoToEntity(UserRequestDto userRequestDto);

//	added additional method
	User dtoToEntity(UserResponseDto userResponseDto);

}
