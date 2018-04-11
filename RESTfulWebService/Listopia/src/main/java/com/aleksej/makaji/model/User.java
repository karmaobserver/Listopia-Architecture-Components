package com.aleksej.makaji.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "USERS")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "NAME")
	private String name;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "IMAGE_URL")
	private String imageUrl;
	
	/*@OneToMany(mappedBy="myFriends")
	private List<User> friends;

	@OneToMany(mappedBy="me")
	private List<User> friendTo;*/
	
	@JoinTable(name = "friendship", joinColumns = {
			 @JoinColumn(name = "friend_requester", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
			 @JoinColumn(name = "friend_accepter", referencedColumnName = "id", nullable = false)})
	@JsonIgnore
	@ManyToMany
	private List<User> friends;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "friends")
    private List<User> friendsTo;

	public User(String id, String name, String email, String imageUrl) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.imageUrl = imageUrl;
	}
	
	public User() {
		super();
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
	
	public List<User> getFriends() {
		return friends;
	}

	public void setFriends(List<User> friends) {
		this.friends = friends;
	}

	public List<User> getFriendsTo() {
		return friendsTo;
	}

	public void setFriendsTo(List<User> friendsTo) {
		this.friendsTo = friendsTo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
