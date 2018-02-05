package com.aleksej.makaji.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.aleksej.makaji.model.User;
import com.aleksej.makaji.repository.UserRepository;

@Service
@Transactional
public class UserService implements GenericServiceString<User>{

	@Autowired
	private UserRepository userRepository;
	
	public List<User> getAll() {
		return userRepository.findAll();
	}

	@Override
	public User save(User t) {
		return userRepository.save(t);
	}

	@Override
	public User findById(String id) {
		return userRepository.findOne(id);
	}

	@Override
	public void deleteById(String id) {
		userRepository.delete(id);	
	}
}
