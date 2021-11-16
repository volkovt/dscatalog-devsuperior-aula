package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
		
	@Transactional(readOnly=true)
	public List<CategoryDTO> findAll() {
		return repository.findAll().stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly=true)
	public CategoryDTO findById(Long id) {
		return repository.findById(id).map(x -> new CategoryDTO(x)).orElseThrow(() -> new ResourceNotFoundException("Entity Not Found"));
	}

	@Transactional
	public CategoryDTO save(CategoryDTO dto) {
		Category cat = new Category();
		cat.setName(dto.getName());
		return new CategoryDTO(repository.save(cat));
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
		Category entity = repository.getOne(id);
		
		entity.setName(dto.getName());
		return new CategoryDTO(repository.save(entity));
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	@Transactional(readOnly=true)
	public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
		return repository.findAll(pageRequest).map(x -> new CategoryDTO(x));
	}
	
	@Transactional
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
