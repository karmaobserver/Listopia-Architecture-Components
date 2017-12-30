package com.aleksej.makaji.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aleksej.makaji.model.ShoppingList;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
	
	/*@Query("SELECT language FROM Language language WHERE language.name = :languageNameLooking") 
	 public Language findLanguageByName(@Param("languageNameLooking") String languageNameLooking);
*/
}
