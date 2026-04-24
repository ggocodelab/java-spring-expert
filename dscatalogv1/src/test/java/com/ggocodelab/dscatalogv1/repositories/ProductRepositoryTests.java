package com.ggocodelab.dscatalogv1.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.ggocodelab.dscatalogv1.entities.Product;
import com.ggocodelab.dscatalogv1.reporitories.ProductRepository;
import com.ggocodelab.dscatalogv1.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long countTotalProducts;
	private long nonExistingId;
	
	@BeforeEach
	void setUp() throws Exception {
	    existingId = 1L;
	    nonExistingId = 1000L;
	    countTotalProducts = 25;
	    
	}
	
	@Test
	public void deleteShouldDeletObjectWhenIdExists() {
				
		repository.deleteById(existingId);
		
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void saveShouldPersistWhithAutoincrementWhenIdIsNull() {
		
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());	
	}
	
	@Test
	public void findyIdShouldReturnNonEmptyOptionalWhenIdExists(){
		
		Optional<Product> result = repository.findById(existingId);
		
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findyIdShouldReturnEmptyOptionalWhenIdDoesNotExists(){
		
		Optional<Product> result = repository.findById(nonExistingId);
		
		Assertions.assertTrue(result.isEmpty());
	}	
}
