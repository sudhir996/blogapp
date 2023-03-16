package com.myblog.blogapp.repository;

import com.myblog.blogapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);                                 //finding the record based on Email

    Optional<User> findByUsernameOrEmail(String username, String email);      //finding the record based on UsernameOrEmail

    Optional<User> findByUsername(String username);                           //finding the record based on Username

    Boolean existsByUsername(String username);                  //exist:true / false:not exist

    Boolean existsByEmail(String email);                        //exist:true / false:not exist
}
