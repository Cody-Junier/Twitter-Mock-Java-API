package com.cooksystems.group2project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksystems.group2project.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByCredentialsUsername(String username);

    List<User> findAllByDeletedFalse();

//    User findByCredentialsUsernameAndDeletedFalse(String username);

    List<User> findAllByDeletedTrue(); // This will find all users where deleted is true
}

