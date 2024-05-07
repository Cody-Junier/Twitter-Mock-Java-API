package com.cooksystems.group2project.dtos;

import lombok.NoArgsConstructor;
import lombok.Data;

@NoArgsConstructor
@Data
public class UserRequestDto {
private ProfileDto profile;
private CredentialsDto credentials;
}
