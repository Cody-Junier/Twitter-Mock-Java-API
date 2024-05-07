package com.cooksystems.group2project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Data
public class Credentials {
//    username; should be unique value

	@Column(unique = true, nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String password;

}
