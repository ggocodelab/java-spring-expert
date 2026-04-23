package com.ggocodelab.dscatalogv1.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ggocodelab.dscatalogv1.dtos.CategoryDTO;
import com.ggocodelab.dscatalogv1.dtos.ProductDTO;
import com.ggocodelab.dscatalogv1.entities.Category;
import com.ggocodelab.dscatalogv1.entities.Product;
import com.ggocodelab.dscatalogv1.exceptions.DatabaseException;
import com.ggocodelab.dscatalogv1.exceptions.ResourceNotFoundException;
import com.ggocodelab.dscatalogv1.reporitories.CategoryRepository;
import com.ggocodelab.dscatalogv1.reporitories.ProductRepository;


@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable){		
		Page<Product> list = repository.findAll(pageable);		
		return list.map(x -> new
				ProductDTO(x));		
	}
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ProductDTO(entity, entity.getCategories());
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}	

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		Product entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
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
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setMoment(dto.getMoment());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		for(CategoryDTO catDTO : dto.getCategories()) {
			Category category = categoryRepository.getReferenceById(catDTO.getId());
			entity.getCategories().add(category);
		}
	}
}
