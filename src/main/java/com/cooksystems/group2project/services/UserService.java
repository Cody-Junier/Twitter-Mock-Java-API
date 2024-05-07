package com.cooksystems.group2project.services;

import com.cooksystems.group2project.dtos.CredentialsDto;
import com.cooksystems.group2project.dtos.TweetResponseDto;
import com.cooksystems.group2project.dtos.UserRequestDto;
import com.cooksystems.group2project.dtos.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserService {


    List<UserResponseDto> getAllUsers();

//    prev called getOneUser
    UserResponseDto getUserByUsername(String username);
    UserResponseDto updateUserProfile(String username, UserRequestDto userRequestDto);
    UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

    boolean checkUsernameExists(String username);
    boolean checkUsernameAvailable(String username);
    UserResponseDto createUser(UserRequestDto userRequestDto);
    void followUser(String username, CredentialsDto credentialsDto);
    void unFollowUser(String username, CredentialsDto credentialsDto);
    List <TweetResponseDto> getTweets(String username);
    List <TweetResponseDto> getFeed(String username);

    List<TweetResponseDto> getMentions(String username);
    List<UserResponseDto> getFollowers(String username);
    List<UserResponseDto> getFollowing(String username);

}
