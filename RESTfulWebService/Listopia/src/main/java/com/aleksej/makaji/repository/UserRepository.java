package com.aleksej.makaji.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aleksej.makaji.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
