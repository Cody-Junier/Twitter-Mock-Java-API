package com.cooksystems.group2project.dtos;

import java.sql.Timestamp;

import com.cooksystems.group2project.entities.Tweet;
import com.cooksystems.group2project.entities.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetResponseDto {

	private Long id;

	private UserResponseDto author;

	private Timestamp posted;

	private String content;

	private TweetResponseDto inReplyTo;

	private TweetResponseDto repostOf;
}
