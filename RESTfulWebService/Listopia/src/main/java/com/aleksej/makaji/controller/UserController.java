package com.aleksej.makaji.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aleksej.makaji.model.User;
import com.aleksej.makaji.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	UserService userService;
	
	//public static final String CLIENT_ID = "479114592644-91qbbkho91juascc95fiasf1a0bj8acr.apps.googleusercontent.com";
	public static final String CLIENT_ID = "479114592644-jilkbsr5flkcumgn0uvcnslsduli2t8o.apps.googleusercontent.com";
		
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ResponseEntity<User> login(@RequestHeader(value="Authorization") String idTokenString) throws GeneralSecurityException, IOException {
		
		System.out.println("Login API has been hitted");
		System.out.println("idTokenString: " + idTokenString);
		
		// Set up the HTTP transport and JSON factory
		HttpTransport transport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
				// Specify the CLIENT_ID of the app that accesses the backend:
			    .setAudience(Collections.singletonList(CLIENT_ID))
			    //.setIssuer("https://accounts.google.com")
			    // Or, if multiple clients access the backend:
			    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
			    .build();
		
		GoogleIdToken idToken = verifier.verify(idTokenString);
		System.out.println("idToken after verification: " + idToken);
		if (idToken != null) {
		  Payload payload = idToken.getPayload();

		  // Print user identifier
		  String userId = payload.getSubject();
		  System.out.println("User ID: " + userId);

		  // Get profile information from payload
		  String email = payload.getEmail();
		  System.out.println("User Email: " + email);
		  boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
		  String name = (String) payload.get("name");
		  String pictureUrl = (String) payload.get("picture");
		 /* String locale = (String) payload.get("locale");
		  String familyName = (String) payload.get("family_name");
		  String givenName = (String) payload.get("given_name");*/
		  
		  User user = new User();
		  user.setId(userId);
		  user.setEmail(email);
		  user.setName(name);
		  user.setImageUrl(pictureUrl);
		  
		  User userDataBase = userService.findById(userId);
		  if (userDataBase == null)
			  userService.save(user);
		  
		  return new ResponseEntity<User>(user, HttpStatus.OK);

		} else {
		  System.out.println("Invalid ID token.");
		  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}
