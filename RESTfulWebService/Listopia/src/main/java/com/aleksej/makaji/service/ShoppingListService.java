package com.aleksej.makaji.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aleksej.makaji.model.ShoppingList;
import com.aleksej.makaji.repository.ShoppingListRepository;

@Service
@Transactional
public class ShoppingListService implements GenericService<ShoppingList>{

	@Autowired
	private ShoppingListRepository shoppingListRepository;
	
	public List<ShoppingList> getAll() {
		return shoppingListRepository.findAll();
	}

	@Override
	public ShoppingList save(ShoppingList t) {
		return shoppingListRepository.save(t);
	}

	@Override
	public ShoppingList findById(long id) {
		return shoppingListRepository.findOne(id);
	}

	@Override
	public void deleteById(long id) {
		shoppingListRepository.delete(id);	
	}
	
	/*public Language findLanguageByName(String name) {
		Language language = new Language();
		language = languageRepository.findLanguageByName(name);
		return language;
	}*/

}
