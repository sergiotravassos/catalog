package com.sergiotravassos.catalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sergiotravassos.catalog.entities.Category;
import com.sergiotravassos.catalog.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<Category> findAll(){
		return categoryRepository.findAll();
	}
}
