package com.cooksystems.group2project.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {

//    many to many relationship between tweet and hashtag

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String label;

//	java sql timestamp format: yyyy-mm-dd hh:mm:ss
	@CreationTimestamp
	private Timestamp firstUsed;


//	@Column(name = "last_used")
	@UpdateTimestamp
	private Timestamp lastUsed;

	// The many-to-many relationship with Tweet is mapped here
	@ManyToMany(mappedBy = "hashtags")
	private List<Tweet> tweets = new ArrayList<>();


}


//    In the Hashtag entity, private Set<Tweet> tweets represents the collection of Tweet entities
//    associated with a Hashtag entity. The mappedBy = "hashtags" attribute tells JPA
//    that this side of the relationship is mapped by the hashtags field on the Tweet side.
//    IMPORTANT: Thus, the Hashtag entity doesn't need to define the join table again;
//    it just needs to point to the field that does.
