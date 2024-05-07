package com.cooksystems.group2project.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Data
public class TweetRequestDto {
	
	private String content;

	public CredentialsDto credentials;
}
