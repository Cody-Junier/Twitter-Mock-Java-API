package com.cooksystems.group2project.dtos;

import java.sql.Timestamp;

import com.cooksystems.group2project.entities.Profile;

import jakarta.persistence.Embedded;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
public class UserResponseDto {

	private String username;

	@Embedded
	private ProfileDto profile;

	private Timestamp joined;
}
