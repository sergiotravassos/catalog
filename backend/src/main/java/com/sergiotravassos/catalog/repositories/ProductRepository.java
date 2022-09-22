package com.sergiotravassos.catalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sergiotravassos.catalog.entities.Category;
import com.sergiotravassos.catalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	@Query("SELECT DISTINCT obj FROM Product obj "
			+ "INNER JOIN obj.categories cats "
			+ "WHERE (:category IS NULL OR :category IN cats) "
			+ "AND (LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%')))")
	Page<Product> search(Category category, String name, Pageable pageable);

}
