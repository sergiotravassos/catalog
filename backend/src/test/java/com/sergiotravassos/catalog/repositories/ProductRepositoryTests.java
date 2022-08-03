package com.sergiotravassos.catalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.sergiotravassos.catalog.entities.Product;
import com.sergiotravassos.catalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository productRepository;

	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
	}

	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		product = productRepository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts +1, product.getId());
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		productRepository.deleteById(existingId);
		Optional<Product> result = productRepository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			productRepository.deleteById(nonExistingId);
		});
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {
		Optional<Product> result = productRepository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists() {
		Optional<Product> result = productRepository.findById(nonExistingId);
		Assertions.assertTrue(result.isEmpty());
	}
}
