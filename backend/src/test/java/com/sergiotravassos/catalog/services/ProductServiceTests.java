package com.sergiotravassos.catalog.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sergiotravassos.catalog.dto.ProductDTO;
import com.sergiotravassos.catalog.entities.Category;
import com.sergiotravassos.catalog.entities.Product;
import com.sergiotravassos.catalog.repositories.CategoryRepository;
import com.sergiotravassos.catalog.repositories.ProductRepository;
import com.sergiotravassos.catalog.services.exceptions.DatabaseException;
import com.sergiotravassos.catalog.services.exceptions.ResourceNotFoundException;
import com.sergiotravassos.catalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CategoryRepository categoryRepository;

	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private Category category;
	private PageImpl<Product> page;
	private Product product;
	private ProductDTO dto;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;
		product = Factory.createProduct();
		category = Factory.createCategory();
		dto = Factory.createProductDto();
		page = new PageImpl<>(List.of(product));

		Mockito.when(productRepository.findAll((Pageable) any())).thenReturn(page);

		Mockito.when(productRepository.save(any())).thenReturn(product);

		Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(productRepository.find(any(), any(), any())).thenReturn(page);

		Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);
		Mockito.when(productRepository.getReferenceById(nonExistingId)).thenThrow(EntityExistsException.class);

		Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
		Mockito.when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityExistsException.class);

		Mockito.doNothing().when(productRepository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO result = productService.findById(existingId);
		Assertions.assertNotNull(result);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.findById(nonExistingId);
		});
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		ProductDTO result = productService.update(existingId, dto);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.update(nonExistingId, dto);
		});
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = productService.findAllPaged(0L, "", pageable);
		Assertions.assertNotNull(result);
	}

	@Test
	public void deleteShouldThrowsDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			productService.delete(dependentId);
		});

		Mockito.verify(productRepository, Mockito.times(1)).deleteById(dependentId);
	}

	@Test
	public void deleteShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.delete(nonExistingId);
		});

		Mockito.verify(productRepository, Mockito.times(1)).deleteById(nonExistingId);
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			productService.delete(existingId);
		});

		Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
	}

}
