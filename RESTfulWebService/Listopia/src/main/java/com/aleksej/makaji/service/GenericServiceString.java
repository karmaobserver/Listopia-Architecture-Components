package com.aleksej.makaji.service;

import java.util.List;

public interface GenericServiceString<T> {

	List<T> getAll();
	
	void deleteById(String id);

	T save(T t);

	T findById(String id);
	
}
