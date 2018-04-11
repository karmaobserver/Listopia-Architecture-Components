package com.aleksej.makaji.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aleksej.makaji.dto.UserWithFriendsDto;
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
	
	public ResponseEntity<?> addUserToFriendsByEmail(String userId, String email) {
		System.out.println("Email is: " + email + " and userId is: " + userId);
		
		//Find user by email
		User userFriend = userRepository.findUserByEmail(email);
		if (userFriend == null) {
			System.out.println("User is not registrated in application with that email");
	    	return new ResponseEntity<String>("User is not registrated in application with that email", HttpStatus.NOT_FOUND);
	    }  
		System.out.println("FriendUser is " + userFriend.getName());
		
		User user = findById(userId);
		System.out.println("User is " + user.getName());
		
		//Check if user with email is already in friend list
		for (User friend: user.getFriends()) {
			if (friend.getId().equals(userFriend.getId())) {
				System.out.println("User with that email is already friend");
				return new ResponseEntity<String>("User with that email is already friend", HttpStatus.CONFLICT);
			}
		}
		
		//Add and save friend
		List<User> friendList = user.getFriends();
		friendList.add(userFriend);
		user.setFriends(friendList);
		userRepository.save(user);
		
		//Set dto
		UserWithFriendsDto userWithFriendsDto = new UserWithFriendsDto();
		userWithFriendsDto.setUserAndFriends(user, friendList);
		
		return new ResponseEntity<UserWithFriendsDto>(userWithFriendsDto, HttpStatus.OK);
	}
	
	public ResponseEntity<User> deleteAllFriends(String userId) {
		System.out.println("DeleteAllFriends #### userId is: " + userId);
		User user = userRepository.findOne(userId);
		user.getFriends().clear();
		
		try {
			userRepository.save(user);
        } catch(Exception e) {
        	e.printStackTrace();
        }
		
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	
}
