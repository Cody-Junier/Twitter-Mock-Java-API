package com.cooksystems.group2project.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cooksystems.group2project.dtos.ContextDto;
import com.cooksystems.group2project.dtos.CredentialsDto;
import com.cooksystems.group2project.dtos.HashtagDto;
import com.cooksystems.group2project.dtos.TweetRequestDto;
import com.cooksystems.group2project.dtos.TweetResponseDto;
import com.cooksystems.group2project.dtos.UserResponseDto;
import com.cooksystems.group2project.entities.Hashtag;
import com.cooksystems.group2project.entities.Tweet;
import com.cooksystems.group2project.entities.User;
import com.cooksystems.group2project.exceptions.BadRequestException;
import com.cooksystems.group2project.exceptions.NotAuthorizedException;
import com.cooksystems.group2project.exceptions.NotFoundException;
import com.cooksystems.group2project.mapper.HashtagMapper;
import com.cooksystems.group2project.mapper.TweetMapper;
import com.cooksystems.group2project.mapper.UserMapper;
import com.cooksystems.group2project.repositories.HashtagRepository;
import com.cooksystems.group2project.repositories.TweetRepository;
import com.cooksystems.group2project.repositories.UserRepository;
import com.cooksystems.group2project.services.TweetService;
//import com.cooksystems.group2project.services.UserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

	private final TweetRepository tweetRepository;

	private final TweetMapper tweetMapper;

	private final HashtagMapper hashtagMapper;

	private final UserMapper userMapper;

	private final UserRepository userRepository;

	private final HashtagRepository hashtagRepository;

//	Come back to this after talking to simrath about userservice
//	private final UserService userService;

	private List<Tweet> getAllNonDeletedTweets(List<Tweet> tweets) {
		List<Tweet> nonDeleted = new ArrayList<Tweet>();
		for (Tweet tweet : tweets) {
			if (!tweet.isDeleted()) {
				nonDeleted.add(tweet);
			}
		}
		return nonDeleted;
	};

//	helper function- will be used in posting requests
	private User getUser(String username) {
		Optional<User> user = Optional.of(userRepository.findByCredentialsUsername(username));
		if (user.isEmpty() || user.get().isDeleted() || user == null) {
			throw new NotFoundException(username + " is not a user, or deleted their account.");
		}

		return user.get();
	}

//	helper function- will be used in posting requests
	private User checkCredentials(String username, CredentialsDto credentials) {
		User user = getUser(username);
		if (!user.getCredentials().getUsername().equals(credentials.getUsername())
				|| !user.getCredentials().getPassword().equals(credentials.getPassword())) {
			throw new NotAuthorizedException("Please enter correct username and password");
		}
		return user;
	}

	private void processContent(Tweet tweet) {
		String content = tweet.getContent();
		Pattern hashtagPattern = Pattern.compile("(?<![\\w])#[\\S]*\\b");
		Matcher hashtagMatcher = hashtagPattern.matcher(content);
		while (hashtagMatcher.find()) {
			String label = hashtagMatcher.group(0);
			Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabelIgnoreCase(label);
			Hashtag hashtag;
			if (optionalHashtag.isEmpty()) {
				hashtag = new Hashtag();
				hashtag.setLabel(label);
			} else {
				hashtag = optionalHashtag.get();
			}
			hashtag.setLastUsed(Timestamp.valueOf(LocalDateTime.now()));
			hashtag.getTweets().add(tweet);
			hashtagRepository.saveAndFlush(hashtag);
			tweet.getHashtags().add(hashtag);
		}
		Pattern mentionPattern = Pattern.compile("(?<![\\w])@[\\S]*\\b");
		Matcher mentionMatcher = mentionPattern.matcher(content);
		while (mentionMatcher.find()) {
			String username = mentionMatcher.group(0).substring(1);
			Optional<User> user = Optional.of(userRepository.findByCredentialsUsername(username));
			if (!user.isEmpty()) {
				user.get().getMentionedTweets().add(tweet);
				tweet.getMentionedUsers().add(user.get());
				userRepository.saveAndFlush(user.get());
			}
		}

		tweetRepository.saveAndFlush(tweet);
	}

	public void setMentions(Long tweetId, List<User> mentions) {
		Tweet tweet = tweetRepository.findById(tweetId)
				.orElseThrow(() -> new EntityNotFoundException("Tweet not found"));
		tweet.setMentionedUsers(mentions);
		tweetRepository.save(tweet);
	}

	@Override
	public List<TweetResponseDto> findAfterChain(Tweet tweet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tweet getTweet(Long tweetId) {
		Optional<Tweet> targetTweet = tweetRepository.findById(tweetId);
		if (targetTweet == null || targetTweet.isEmpty()) {
			throw new NotFoundException("Tweet with ID: " + tweetId + " was not found");
		}
		if (targetTweet.get().isDeleted()) {
			throw new NotFoundException("Tweet with ID: " + tweetId + " is deleted");
		}
		return targetTweet.get();
	}

	@Override
	public List<TweetResponseDto> findBeforeChain(Tweet targetTweet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TweetResponseDto> getAllTweets() {

		List<Tweet> tweetsToSend = tweetRepository.findAll().stream().filter(tweet -> !tweet.isDeleted())
				.sorted(Comparator.comparing(Tweet::getPosted, Comparator.reverseOrder())).collect(Collectors.toList());
		return tweetMapper.entitiesToDtos(tweetsToSend);
	}

	@Override
	public TweetResponseDto getTweetById(Long id) {
		return tweetMapper.entityToDto(getTweet(id));
	}

	@Override
	public TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto) {
		Tweet tweet = getTweet(id);
		if (!tweet.getAuthor().getCredentials().getUsername().equals(credentialsDto.getUsername())
				|| !tweet.getAuthor().getCredentials().getPassword().equals(credentialsDto.getPassword())) {
			throw new NotAuthorizedException("You are not authorized to delete this tweet");
		}

		tweet.setDeleted(true);
		tweetRepository.save(tweet);

		return tweetMapper.entityToDto(tweet);
	}

	@Override
	public List<HashtagDto> getAllHashtagsByTweet(Long id) {
		Tweet tweet = getTweet(id);
		if(tweet == null) {
			throw new NotFoundException("That tweet does not exist");
		}

		List<HashtagDto> tags = hashtagMapper.entitiesToDtos(tweet.getHashtags());
		return tags;
	}

	@Override
	public List<UserResponseDto> getUsersByLikedTweet(Long id) {
		Tweet tweet = getTweet(id);

		List<User> activeUsers = tweet.getLikedByUsers().stream().filter(user -> !user.isDeleted())
				.collect(Collectors.toList());
		return userMapper.entitiesToDtos(activeUsers);
	}

	@Override
	public void likeTweet(Long id, CredentialsDto credentialsDto) {
		User user = checkCredentials(credentialsDto.getUsername(), credentialsDto);
		Tweet tweet = getTweet(id);
		List<Tweet> usersLikedTweets = user.getLikedTweets();
		List<User> tweetsLikedUsers = tweet.getLikedByUsers();
		if (!usersLikedTweets.contains(tweetsLikedUsers)) {
			usersLikedTweets.add(tweet);
			tweetsLikedUsers.add(user);
			tweetRepository.saveAndFlush(tweet);
			userRepository.saveAndFlush(user);
		}

	}

	@Override
	public List<TweetResponseDto> getRepliesByTweet(Long id) {
		Tweet tweet = getTweet(id);

		List<Tweet> activeReplies = tweet.getReplies().stream().filter(reply -> !reply.isDeleted())
				.collect(Collectors.toList());
		return tweetMapper.entitiesToDtos(activeReplies);
	}

	@Override
	public List<TweetResponseDto> getRepostsByTweet(Long id) {
		Tweet tweet = getTweet(id);

		List<Tweet> activeReposts = getAllNonDeletedTweets(tweet.getReposts());

//		System.out.println(activeReposts);

		return tweetMapper.entitiesToDtos(activeReposts);
//		return null;
	}

	@Override
	public List<UserResponseDto> getUsersMentionedInTweet(Long id) {
		// TODO Auto-generated method stub
		Tweet tweet = getTweet(id);
		List<User> mentionedUsers = tweet.getMentionedUsers().stream().filter(user -> !user.isDeleted())
				.collect(Collectors.toList());
		return userMapper.entitiesToDtos(mentionedUsers);
	}

	@Override
	public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
		if (tweetRequestDto.getContent() == null) {
			throw new BadRequestException("You must give content to your tweet");
		}
		if (tweetRequestDto.getCredentials() == null || tweetRequestDto.getCredentials().getPassword() == null
				|| tweetRequestDto.getCredentials().getUsername() == null) {
			throw new NotAuthorizedException("Incorrect Credentials");
		}
		if(userRepository.findByCredentialsUsername(tweetRequestDto.getCredentials().getUsername()) == null) {
			throw new BadRequestException("User does not exist.");
		}
		User user = checkCredentials(tweetRequestDto.getCredentials().getUsername(), tweetRequestDto.getCredentials());
		if(user == null) {
			throw new NotFoundException("Could not find a user with the given credentials");
		}
		Tweet tweet = tweetMapper.requestDtoToEntity(tweetRequestDto);
		tweet.setAuthor(user);
		processContent(tweet);
		return tweetMapper.entityToDto(tweet);
	}

	@Override
	public TweetResponseDto replyToTweet(Long id, TweetRequestDto tweetRequestDto) {
		if (tweetRequestDto.getContent() == null) {
			throw new BadRequestException("You must give content to your tweet");
		}
		if (tweetRequestDto.getCredentials() == null || tweetRequestDto.getCredentials().getPassword() == null
				|| tweetRequestDto.getCredentials().getUsername() == null) {
			throw new NotAuthorizedException("Incorrect Credentials");
		}
		User user = checkCredentials(tweetRequestDto.getCredentials().getUsername(), tweetRequestDto.getCredentials());
		Tweet inReplyTo = getTweet(id);

		Tweet replyTweet = tweetMapper.requestDtoToEntity(tweetRequestDto);
		replyTweet.setAuthor(user);
		replyTweet.setInReplyTo(inReplyTo);
//		helper method
		processContent(replyTweet);
		replyTweet = tweetRepository.saveAndFlush(replyTweet);
		inReplyTo.getReplies().add(replyTweet);
		tweetRepository.saveAndFlush(inReplyTo);

		return tweetMapper.entityToDto(replyTweet);
	}

	@Override
	public TweetResponseDto getRepostsByTweet(Long id, CredentialsDto credentialsDto) {
		if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
			throw new BadRequestException("You must provide your Username and Password");
		}
		User user = checkCredentials(credentialsDto.getUsername(), credentialsDto);
		Tweet originalTweet = getTweet(id);
		Tweet repostTweet = new Tweet();
		repostTweet.setAuthor(user);
		repostTweet.setRepostOf(originalTweet);
		repostTweet = tweetRepository.saveAndFlush(repostTweet);
		originalTweet.getReposts().add(repostTweet);
		tweetRepository.saveAndFlush(originalTweet);

		return tweetMapper.entityToDto(repostTweet);
	}

	@Override
	public ContextDto getTweetContext(Long id) {
		Tweet targetTweet = getTweet(id);
		List<Tweet> beforeTweets = tweetRepository.findAllByRepostOf(targetTweet);
		List<Tweet> nonDeletedBeforeTweets = beforeTweets.stream().filter(tweet -> !tweet.isDeleted()).collect(Collectors.toList());
		List<Tweet> afterTweets = tweetRepository.findAllByInReplyTo(targetTweet);
		List<Tweet> nonDeletedAfterTweets = afterTweets.stream().filter(tweet -> !tweet.isDeleted()).collect(Collectors.toList());
		ContextDto context = new ContextDto();
		context.setTarget(tweetMapper.entityToDto(targetTweet));
		context.setBefore(tweetMapper.entitiesToDtos(nonDeletedBeforeTweets));
		context.setAfter(tweetMapper.entitiesToDtos(nonDeletedAfterTweets));
		return context;
	}

}
