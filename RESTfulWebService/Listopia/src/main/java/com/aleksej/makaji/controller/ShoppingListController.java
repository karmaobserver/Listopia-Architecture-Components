package com.aleksej.makaji.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aleksej.makaji.model.ShoppingList;
import com.aleksej.makaji.service.ShoppingListService;


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
