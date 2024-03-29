package com.kaliware.dscatalog.services;

import com.kaliware.dscatalog.dto.CategoryDTO;
import com.kaliware.dscatalog.entities.Category;
import com.kaliware.dscatalog.repositories.CategoryRepository;
import com.kaliware.dscatalog.services.exceptions.DatabaseException;
import com.kaliware.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoryService{

  @Autowired
  CategoryRepository repository;

  @Transactional(readOnly = true)
  public Page<CategoryDTO> findAllPaged(Pageable pageable){
    Page<Category> list = repository.findAll(pageable);
    return list.map(CategoryDTO::new);
  }

  @Transactional(readOnly = true)
  public CategoryDTO findById(Long id){
    Optional<Category> obj = repository.findById(id);
    Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
    return new CategoryDTO(entity);
  }

  @Transactional
  public CategoryDTO insert(CategoryDTO dto){
    Category entity = new Category();
    entity.setName(dto.getName());
    entity = repository.save(entity);
    return new CategoryDTO(entity);
  }

  @Transactional
  public CategoryDTO update(Long id, CategoryDTO dto){
    try{
      Category entity = repository.getReferenceById(id);
      entity.setName(dto.getName());
      entity = repository.save(entity);
      return new CategoryDTO(entity);
    }catch(javax.persistence.EntityNotFoundException e){
      throw new ResourceNotFoundException("Id not found " + id);
    }
  }

  public void delete(Long id){
    try{
      repository.deleteById(id);
    }catch(EmptyResultDataAccessException e){
      throw new ResourceNotFoundException("Id not found " + id);
    }catch(DataIntegrityViolationException e){
      throw new DatabaseException("Integrity violation");
    }
  }
}
