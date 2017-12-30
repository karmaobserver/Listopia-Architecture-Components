package com.aleksej.makaji.service;

import java.util.List;

public interface GenericService<T> {

	List<T> getAll();
	
	void deleteById(long id);

	T save(T t);

	T findById(long id);
	
}
