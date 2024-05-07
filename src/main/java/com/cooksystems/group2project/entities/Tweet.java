package com.cooksystems.group2project.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import org.springframework.data.annotation.CreatedDate;
import com.fasterxml.jackson.annotation.JsonProperty;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Tweet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User author;
//renamed from postedTimestamp
//	@Column(nullable = false)
	@CreationTimestamp
	private Timestamp posted;

	private boolean deleted = false;

	private String content;

//access the likes from the Tweet side,set that references the users who have liked the tweet.
	@ManyToMany(mappedBy = "likedTweets")
	private List<User> likedByUsers;

	@ManyToOne
	private Tweet inReplyTo;

	@OneToMany(mappedBy = "inReplyTo")
	private List<Tweet> replies = new ArrayList<>();

	@ManyToOne
	@JoinTable(name ="reposts_of_id")
	private Tweet repostOf;

	@OneToMany(mappedBy = "repostOf")
	private List<Tweet> reposts = new ArrayList<>();

	// Hashtags (Many-to-Many with Hashtag)
	@ManyToMany
	@JoinTable(name = "tweet_hashtags", joinColumns = @JoinColumn(name = "tweet_id"), inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
	private List<Hashtag> hashtags = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "user_mentions", joinColumns = @JoinColumn(name = "tweet_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> mentionedUsers = new ArrayList<>();

//	public void setMentions(List<User> mentions) {
//		this.mentions = mentions;
//	}

}


////
////access the likes from the Tweet side,set that references the users who have liked the tweet.
//@ManyToMany(mappedBy = "likedTweets")
//private List<User> likedByUsers;
////bidirectional access. JPA understands that the likedTweets collection in
//// the User entity maps to the user_likes table,
//// navigate from a Tweet to its liking Users without an explicit join table on the Tweet side.
//
//@ManyToOne
//	consider replyId for name-
// matches the inReplyTo field in the tweet table
//	@JoinColumn(name = "inReplyToId") // matches the inReplyTo field in the tweet table
//	consider replyId for name-
// matches the inReplyTo field in the tweet table




//	@ManyToMany
//	@JoinTable(name = "tweet_hashtags", joinColumns = @JoinColumn(name = "tweet_id"), inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
//	private List<Hashtag> hashtags = new ArrayList<>();
//
//// In JPA, we manage this relationship by defining a collection of entities on each side of the relationship
//// and using the @ManyToMany and @JoinTable annotations to configure the association.
//
////   Here, private Set<Hashtag> hashtags represents the collection of Hashtag entities associated with a Tweet entity.

//	@OneToMany(mappedBy = "repostOf")
//	private List<Tweet> reposts = new ArrayList<>();

//    JPA will create and manage the foreign key relationships based on these annotations. When you save a Tweet entity with a non-null replyTo or repostOf,
//    JPA will insert the corresponding tweet's ID into the inReplyTo or repostOf column.