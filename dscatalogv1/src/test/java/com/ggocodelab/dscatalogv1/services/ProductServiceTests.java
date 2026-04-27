package com.ggocodelab.dscatalogv1.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.ggocodelab.dscatalogv1.exceptions.DatabaseException;
import com.ggocodelab.dscatalogv1.exceptions.ResourceNotFoundException;
import com.ggocodelab.dscatalogv1.reporitories.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	
	@BeforeEach
	void setUp() {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 3L;
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		when(repository.existsById(existingId)).thenReturn(true);
		
		Assertions.assertDoesNotThrow(() -> {
	        service.delete(existingId);
	    });
		 Mockito.verify(repository).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowExceptionWhenIdDoesNotExist() {

	    when(repository.existsById(nonExistingId)).thenReturn(false);

	    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
	        service.delete(nonExistingId);
	    });

	    verify(repository, never()).deleteById(any());
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		
		when(repository.existsById(dependentId)).thenReturn(true);
		doThrow(DataIntegrityViolationException.class)
		       .when(repository).deleteById(dependentId);

		Assertions.assertThrows(DatabaseException.class, () -> {
		    service.delete(dependentId);
		});
	}	
}
