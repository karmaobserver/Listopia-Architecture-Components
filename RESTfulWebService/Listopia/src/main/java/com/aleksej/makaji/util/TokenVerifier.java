package com.aleksej.makaji.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class TokenVerifier {
	
	//public static final String CLIENT_ID = "479114592644-91qbbkho91juascc95fiasf1a0bj8acr.apps.googleusercontent.com";
	public static final String CLIENT_ID = "479114592644-jilkbsr5flkcumgn0uvcnslsduli2t8o.apps.googleusercontent.com";
	
	// Set up the HTTP transport and JSON factory
	HttpTransport transport = new NetHttpTransport();
	JsonFactory jsonFactory = new JacksonFactory();
	
	public GoogleIdToken verifyToken(String idTokenString) throws GeneralSecurityException, IOException {
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
				// Specify the CLIENT_ID of the app that accesses the backend:
			    .setAudience(Collections.singletonList(CLIENT_ID))
			    //.setIssuer("https://accounts.google.com")
			    // Or, if multiple clients access the backend:
			    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
			    .build();
		
		GoogleIdToken idToken = verifier.verify(idTokenString);
		return idToken;
	}
	

}
