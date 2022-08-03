package com.sergiotravassos.catalog.tests;

import java.time.Instant;

import com.sergiotravassos.catalog.dto.ProductDTO;
import com.sergiotravassos.catalog.entities.Category;
import com.sergiotravassos.catalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png",
				Instant.parse("2020-07-14T10:00:00Z"));
		product.getCategories().add(new Category(2L, "Electronics"));
		return product;
	}
	
	public static ProductDTO createProductDto() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}
}
