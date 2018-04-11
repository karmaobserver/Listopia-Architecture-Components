package com.aleksej.makaji.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aleksej.makaji.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	@Query("SELECT user FROM User user WHERE user.email = :email") 
	 public User findUserByEmail(@Param("email") String email);
	

}
