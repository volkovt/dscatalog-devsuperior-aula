package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
		
	@Transactional(readOnly=true)
	public List<ProductDTO> findAll() {
		return repository.findAll().stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly=true)
	public ProductDTO findById(Long id) {
		return repository.findById(id).map(x -> new ProductDTO(x, x.getCategories())).orElseThrow(() -> new ResourceNotFoundException("Entity Not Found"));
	}

	@Transactional
	public ProductDTO save(ProductDTO dto) {
		Product cat = new Product();
		//cat.setName(dto.getName());
		return new ProductDTO(repository.save(cat));
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
		Product entity = repository.getOne(id);
		//entity.setName(dto.getName());
		return new ProductDTO(repository.save(entity));
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	@Transactional(readOnly=true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		return repository.findAll(pageRequest).map(x -> new ProductDTO(x));
	}
	
	@Transactional
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
