package com.cooksystems.group2project.service.impl;

import java.util.Comparator;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.cooksystems.group2project.dtos.CredentialsDto;
import com.cooksystems.group2project.dtos.ProfileDto;
import com.cooksystems.group2project.dtos.TweetResponseDto;
import com.cooksystems.group2project.dtos.UserRequestDto;
import com.cooksystems.group2project.dtos.UserResponseDto;
import com.cooksystems.group2project.entities.Profile;
import com.cooksystems.group2project.entities.Tweet;
import com.cooksystems.group2project.entities.User;
import com.cooksystems.group2project.exceptions.BadRequestException;
import com.cooksystems.group2project.exceptions.NotAuthorizedException;
import com.cooksystems.group2project.exceptions.NotFoundException;
import com.cooksystems.group2project.mapper.ProfileMapper;
import com.cooksystems.group2project.mapper.TweetMapper;
import com.cooksystems.group2project.mapper.UserMapper;
import com.cooksystems.group2project.repositories.UserRepository;
import com.cooksystems.group2project.services.UserService;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final ProfileMapper profileMapper;
	private final TweetMapper tweetMapper;

    @Override
public boolean checkUsernameExists(String username) {
    User user = userRepository.findByCredentialsUsername(username);
    if (user != null) {
        return true;
    } else {
        return false;
    }
}

    @Override
public boolean checkUsernameAvailable(String username) {
    User user = userRepository.findByCredentialsUsername(username);
    if (user == null) {
        return true;
    } else {
        return user.isDeleted();
    }
}

    @Override
    public void followUser(String username, CredentialsDto credentialsDto){
        User userToFollow = userRepository.findByCredentialsUsername(username);
        String credentialsDtoUsernameToBeFollower = credentialsDto.getUsername();
        User userToBeFollower = userRepository.findByCredentialsUsername(credentialsDtoUsernameToBeFollower);
        if(!checkUsernameExists(credentialsDtoUsernameToBeFollower)) {
            throw new NotFoundException("sorry cannot delete this user as the username entered does not exist in the DB");
        }
        for( User indFollower : userToFollow.getFollowers()){
            if(indFollower.equals(userToBeFollower)){
                throw new BadRequestException("You already follow this user.");
            }
        }
        userToBeFollower.getFollowing().add(userToFollow);
        userToFollow.getFollowers().add(userToBeFollower);
        userMapper.entityToDto(userRepository.saveAndFlush(userToBeFollower));
//        save flush owning side only the mapped by
        userMapper.entityToDto(userRepository.saveAndFlush(userToFollow));

	}

    @Override
    public void unFollowUser(String username, CredentialsDto credentialsDto){
        User userToUnFollow = userRepository.findByCredentialsUsername(username);
        String credentialsDtoUsernameToBeUnFollower = credentialsDto.getUsername();
        User userToBeUnFollower = userRepository.findByCredentialsUsername(credentialsDtoUsernameToBeUnFollower);
        if(!checkUsernameExists(credentialsDtoUsernameToBeUnFollower)) {
            throw new NotFoundException("sorry cannot delete this user as the username entered does not exist in the DB");
        }

        // Check if the userToBeUnFollower is following userToUnFollow
        if(!userToBeUnFollower.getFollowing().contains(userToUnFollow)) {
            throw new BadRequestException("You are not following this user.");
        }

        userToBeUnFollower.getFollowing().remove(userToUnFollow);
        userToUnFollow.getFollowers().remove(userToBeUnFollower);
        userMapper.entityToDto(userRepository.saveAndFlush(userToBeUnFollower));
//        save flush owning side only the mapped by
        userMapper.entityToDto(userRepository.saveAndFlush(userToUnFollow));

    }

    public List<TweetResponseDto> getFeed(String username) {
        User user = getUserFromDatabase(username);
        List<Tweet> tweets = user.getTweets();
        List<User> following = user.getFollowing();
        for( User indFollower: following){
            tweets.addAll(indFollower.getTweets());
        }
        tweets.sort(Comparator.comparing(Tweet::getPosted).reversed());
        return tweetMapper.entitiesToDtos(tweets);
    }

	@Override
	public List<TweetResponseDto> getTweets(String username) {
		User userWeWant = getUserFromDatabase(username);
		List<Tweet> userTweets = userWeWant.getTweets();
//        have to check the date each is posted and then compare them and reverse the order sort it this way
		userTweets.sort(Comparator.comparing(Tweet::getPosted).reversed());
		return tweetMapper.entitiesToDtos(userTweets);
	}

    @Override
    public List<UserResponseDto> getFollowers(String username){
        User userWeWant = getUserFromDatabase(username);
        List<User> nonDeletedUsers = new ArrayList<>();
        List<User> followers = userWeWant.getFollowers();
        // Check if user exists and is not deleted
        if (userWeWant == null || userWeWant.isDeleted()) {
            throw new NotFoundException("User with username: " + username + " does not exist or is deleted.");
        }
        for( User indFollower: followers){
            Boolean isDeleted = indFollower.isDeleted();
            if(!isDeleted){
                nonDeletedUsers.add(indFollower);
            }
        }
        return userMapper.entitiesToDtos(nonDeletedUsers);
    }

    @Override
    public List<UserResponseDto> getFollowing(String username){
        User userWeWant = userRepository.findByCredentialsUsername(username);
        List<User> nonDeletedUsers = new ArrayList<>();
        List<User> following = userWeWant.getFollowing();
        // Check if user exists and is not deleted
        if (userWeWant == null || userWeWant.isDeleted()) {
            throw new NotFoundException("User with username: " + username + " does not exist or is deleted.");
        }
        for( User indFollowing: following){
            Boolean isDeleted = indFollowing.isDeleted();
            if(!isDeleted){
                nonDeletedUsers.add(indFollowing);
            }
        }
        return userMapper.entitiesToDtos(nonDeletedUsers);
    }

    @Override
    public List<TweetResponseDto> getMentions(String username){
        User userWeWant = getUserFromDatabase(username);
        // Check if user exists and is not deleted
        if (userWeWant == null || userWeWant.isDeleted()) {
            throw new NotFoundException("User with username: " + username + " does not exist or is deleted.");
        }
        // Directly use the mentionedUsers collection to get the tweets where the user is mentioned
        List<Tweet> mentionedTweets = userWeWant.getMentionedTweets().stream()
                .filter(tweet -> !tweet.isDeleted())
                .sorted(Comparator.comparing(Tweet::getPosted).reversed())
                .collect(Collectors.toList());
        return tweetMapper.entitiesToDtos(mentionedTweets);
    }


    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto){
        if (userRequestDto == null || userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null) {
            throw new BadRequestException("Missing user data, credentials, or profile information.");
        }



        ProfileDto profileDto = userRequestDto.getProfile();
        CredentialsDto credentialsDto = userRequestDto.getCredentials();
        String username = credentialsDto.getUsername();
        String password = credentialsDto.getPassword();

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new BadRequestException("Username and password must not be empty.");
        }

        // Attempt to find the user regardless of their deletion status.
        User existingUser = userRepository.findByCredentialsUsername(username);

        // If user exists and is deleted, check password and reactivate if correct.
        if (existingUser != null && existingUser.isDeleted()) {
            if (existingUser.getCredentials().getPassword().equals(password)) {
                existingUser.setDeleted(false);
                // Update profile information from the DTO.
                updateProfile(existingUser, profileDto);
                userRepository.saveAndFlush(existingUser);
//                System.out.println("User was deleted but in the DB and is now being reactivated");
                return userMapper.entityToDto(existingUser);
            } else {
                throw new NotAuthorizedException("Password is incorrect for reactivation.");
            }
        }

        // If user exists and is not deleted, throw an exception as username is taken.
        if (existingUser != null && !existingUser.isDeleted()) {
            throw new BadRequestException("Username already taken");
        }

        // Create a new user as no existing user was found.
        User newUser = userMapper.requestDtoToEntity(userRequestDto);
        newUser.setDeleted(false);
        userRepository.saveAndFlush(newUser);
        return userMapper.entityToDto(newUser);
        }

    private void updateProfile(User user, ProfileDto profileDto) {
        if (profileDto.getEmail() != null) user.getProfile().setEmail(profileDto.getEmail());
        if (profileDto.getFirstName() != null) user.getProfile().setFirstName(profileDto.getFirstName());
        if (profileDto.getLastName() != null) user.getProfile().setLastName(profileDto.getLastName());
        if (profileDto.getPhone() != null) user.getProfile().setPhone(profileDto.getPhone());
    }

	@Override
	public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) {
		User potentialDeletedUser = getUserFromDatabase(username);
		String credentialsDtoUsername = credentialsDto.getUsername();
		if (!username.equals(credentialsDtoUsername)) {
			throw new NotAuthorizedException(
					"sorry the username you entered does not match anything in the database so you can not delete this user");
		}
		if (potentialDeletedUser == null) {
			throw new NotFoundException(
					"sorry cannot delete this user as the username entered does not exist in the DB");
		}
		potentialDeletedUser.setDeleted(true);
		return userMapper.entityToDto(userRepository.saveAndFlush(potentialDeletedUser));

	}

	@Override
	public UserResponseDto updateUserProfile(String username, UserRequestDto userRequestDto) {
		User potentialUpdatedUser = getUserFromDatabase(username);
		ProfileDto ourProfileDto = userRequestDto.getProfile();
		Profile ourProfile = profileMapper.dtoToEntity(ourProfileDto);
		String potentialUpdatedUserPassword = potentialUpdatedUser.getCredentials().getPassword();
		String userRequestDtoPassword = userRequestDto.getCredentials().getPassword();
		if (!userRequestDtoPassword.equals(potentialUpdatedUserPassword)) {
			throw new NotAuthorizedException(
					"sorry you have entered an incorrect password so you can not update your user profile");
		}
		String profileDtoFirstName = ourProfileDto.getFirstName();
		String profileDtoLastName = ourProfileDto.getLastName();
		String profileDtoEmail = ourProfileDto.getEmail();
		String profileDtoPhone = ourProfileDto.getPhone();
//        only email is required for profile info but if they dont send the rest of the info in their request for when we
//        update the user profile we will just use the info that is already in the potentialUpdatedUser(ie the current User) when setting it
		if (profileDtoFirstName == null) {
			ourProfile.setFirstName(potentialUpdatedUser.getProfile().getFirstName());
		}
		if (profileDtoLastName == null) {
			ourProfile.setLastName(potentialUpdatedUser.getProfile().getLastName());
		}
		if (profileDtoEmail == null) {
			ourProfile.setEmail(potentialUpdatedUser.getProfile().getEmail());
		}
		if (profileDtoPhone == null) {
			ourProfile.setPhone(potentialUpdatedUser.getProfile().getPhone());
		}
		potentialUpdatedUser.setProfile(ourProfile);
		User potentialUpdatedUserReturning = userRepository.saveAndFlush(potentialUpdatedUser);
		UserResponseDto finalUserResponseDto = userMapper.entityToDto(potentialUpdatedUserReturning);
		return finalUserResponseDto;
	}

	@Override
	public List<UserResponseDto> getAllUsers() {
		return userMapper.entitiesToDtos(userRepository.findAllByDeletedFalse());
	}

	@Override
	public UserResponseDto getUserByUsername(String username) {
		return userMapper.entityToDto(getUserFromDatabase(username));
	}

	
// this is just a helper method designed for getting the users from the DB 	   
    private User getUserFromDatabase(String username) {
        User user = userRepository.findByCredentialsUsername(username);
      if (user == null) {
            throw new NotFoundException("Please use the correct username, No user with username: " + username);
        }
        if (user.isDeleted()) {
            throw new NotFoundException("Sorry but a User with the username: " + username + " has actually been deleted.");
        }
        return user;
    }

}


//        WHEN CREATE user at first its empty slate no followers no likes no mentions yt those develop over
//        time so you dont have to check and set those here when cerating
//        After creation, the User might post tweets, follow other users, and like or be mentioned in tweets.
//These actions create entries in the respective join tables or related tables.


//    @Override
//    public List<TweetResponseDto> getMentions(String username){
//        User userWeWant = getUserFromDatabase(username);
//        List<Tweet> userMentions = userWeWant.getMentionedTweets();
////        have to check the date each is posted and then compare them and reverse the order sort it this way
//        userMentions.sort(Comparator.comparing(Tweet::getPosted).reversed());
//        return tweetMapper.entitiesToDtos(userMentions);
//    }
//    I think the above way is better not 100% sure 4-27
//    @Override
//    public List<TweetResponseDto> getMentions(String username){
//        User userWeWant = getUserFromDatabase(username);
//        List<Tweet> userMentions = userWeWant.getMentionedTweets();
//
//        // Check if user exists and is not deleted
//        if (userWeWant == null || userWeWant.isDeleted()) {
//            throw new NotFoundException("User with username: " + username + " does not exist or is deleted.");
//        }
//
////        String mentionTag = "@" + username;
//        List<Tweet> listToSort = new ArrayList<>();
//        for(Tweet indTweet : userMentions){
//            String content = indTweet.getContent();
//            if(!indTweet.isDeleted() && content != null && content.contains(username)){
//                listToSort.add(indTweet);
//            }
//        }
////        have to check the date each is posted and then compare them and reverse the order sort it this way
//        listToSort.sort(Comparator.comparing(Tweet::getPosted).reversed());
//        return tweetMapper.entitiesToDtos(listToSort);
//    }
