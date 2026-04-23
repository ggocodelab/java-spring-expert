package com.ggocodelab.dscatalogv1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ggocodelab.dscatalogv1.dtos.CategoryDTO;
import com.ggocodelab.dscatalogv1.entities.Category;
import com.ggocodelab.dscatalogv1.exceptions.DatabaseException;
import com.ggocodelab.dscatalogv1.exceptions.ResourceNotFoundException;
import com.ggocodelab.dscatalogv1.reporitories.CategoryRepository;


@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> result = repository.findAll();
		return result.stream().map(x -> new CategoryDTO(x)).toList();
	}
		
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Invalid ID."));
		return new CategoryDTO(entity);
	}
	
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity. setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}
	
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		Category entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found."));
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Resource not found");
		}		
		try {
	        	repository.deleteById(id);   		
		}
	    	catch (DataIntegrityViolationException e) {
	    		throw new DatabaseException("Integrity violation.");
	   	}
	}	
}
