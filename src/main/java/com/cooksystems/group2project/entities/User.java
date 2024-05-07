package com.cooksystems.group2project.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import com.cooksystems.group2project.entities.Credentials;
import com.cooksystems.group2project.entities.Profile;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "user_account") // Rename due to reserved keyword
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Credentials credentials;

	@Embedded
	private Profile profile;

	@CreationTimestamp
	private Timestamp joined;

	@OneToMany(mappedBy = "author")
	private List<Tweet> tweets;

	private boolean deleted = false;

	// Followers and Following (Many-to-Many Self-Referencing)
	@ManyToMany
	@JoinTable(name = "followers_following")
	private List<User> following = new ArrayList<>();

	@ManyToMany(mappedBy = "following")
	private List<User> followers = new ArrayList<>();

	// Likes (Many-to-Many with Tweet)
	@ManyToMany
	@JoinTable(name = "user_likes", 
			joinColumns = @JoinColumn (name = "user_id"),
			inverseJoinColumns= @JoinColumn (name = "tweet_id"))
	private List<Tweet> likedTweets = new ArrayList<>();
//    Even though there is no explicit likes field in the Tweet entity, this relationship is
//    defined in the User entity with a Set<Tweet> because the action of liking a tweet is user-driven.

	// Mentions (Many-to-Many with Tweet)
	@ManyToMany(mappedBy = "mentionedUsers")
	private List<Tweet> mentionedTweets = new ArrayList<>();
	// Getters and setters
}
