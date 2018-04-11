package com.aleksej.makaji.dto;

import java.util.List;

import com.aleksej.makaji.model.User;

public class UserWithFriendsDto {
	
	private String id;
	private String name;
	private String email;
	private String imageUrl;
	private List<User> friends;
	
	public void setUserAndFriends(User user, List<User> friends) {
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.imageUrl = user.getImageUrl();
		this.friends = friends;
	}
	
	public List<User> getFriends() {
		return friends;
	}
	public void setFriends(List<User> friends) {
		this.friends = friends;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	

	
	

}
