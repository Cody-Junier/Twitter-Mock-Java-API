package com.cooksystems.group2project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileDto {
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
}
