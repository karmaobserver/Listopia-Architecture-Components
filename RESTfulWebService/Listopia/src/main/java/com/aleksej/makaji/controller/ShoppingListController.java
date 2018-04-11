package com.aleksej.makaji.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aleksej.makaji.model.ShoppingList;
import com.aleksej.makaji.model.User;
import com.aleksej.makaji.service.ShoppingListService;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;


@RestController
@RequestMapping("/api")
public class ShoppingListController {
	
	@Autowired
	ShoppingListService shoppingListService;
	
	@RequestMapping(value = "/shoppingLists", method = RequestMethod.GET)
	public ResponseEntity<List<ShoppingList>> getShoppingLists() {
		List<ShoppingList> shoppingLists = (List<ShoppingList>) shoppingListService.getAll();
		return new ResponseEntity<List<ShoppingList>>(shoppingLists, HttpStatus.OK);
	}
	
	
	
}
