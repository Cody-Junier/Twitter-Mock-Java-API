package com.cooksystems.group2project.repositories;

import com.cooksystems.group2project.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

	List<Tweet> findAllByInReplyTo(Tweet tweet);

	List<Tweet> findAllByRepostOf(Tweet tweet);

    //List<Tweet> findInReplyToTweetId(Long tweetId);

}
