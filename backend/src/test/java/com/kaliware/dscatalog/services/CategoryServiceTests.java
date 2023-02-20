package com.kaliware.dscatalog.services;

import com.kaliware.dscatalog.dto.CategoryDTO;
import com.kaliware.dscatalog.entities.Category;
import com.kaliware.dscatalog.repositories.CategoryRepository;
import com.kaliware.dscatalog.services.exceptions.DatabaseException;
import com.kaliware.dscatalog.services.exceptions.ResourceNotFoundException;
import com.kaliware.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests{

  @InjectMocks
  private CategoryService service;

  @Mock
  private CategoryRepository repository;

  @Mock
  private CategoryRepository categoryRepository;

  private long existingId;
  private long nonExistingId;
  private long dependentId;
  private PageImpl<Category> page;
  private Category category;
  private CategoryDTO categoryDTO;


  @BeforeEach
  void setUp() throws Exception{
    existingId = 1L;
    nonExistingId = 1000L;
    dependentId = 4L;
    category = Factory.createCategory();
    categoryDTO = Factory.createCategoryDTO();
    page = new PageImpl<>(List.of(category));

    Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

    Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(category);

    Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(category));
    Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

    Mockito.when(repository.getReferenceById(existingId)).thenReturn(category);
    Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

    Mockito.doNothing().when(repository).deleteById(existingId);
    Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
    Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

  }

  @Test
  public void updateShouldReturnCategoryDTOWhenIdExists(){
    CategoryDTO result = service.update(existingId, categoryDTO);

    Assertions.assertNotNull(result);
    Mockito.verify(repository, Mockito.times(1)).getReferenceById(existingId);
  }

  @Test
  public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      service.update(nonExistingId, categoryDTO);
    });
  }

  @Test
  public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      service.findById(nonExistingId);
    });
    Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
  }

  @Test
  public void findByIdShouldReturnCategoryDTOWhenIdExists(){
    CategoryDTO result = service.findById(existingId);

    Assertions.assertNotNull(result);
    Mockito.verify(repository, Mockito.times(1)).findById(existingId);
  }

  @Test
  public void findAllPagedShouldReturnPage(){
    Pageable pageable = PageRequest.of(0, 10);
    Page<CategoryDTO> result = service.findAllPaged(pageable);

    Assertions.assertNotNull(result);
    Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
  }

  @Test
  public void deleteShouldThrowDatabaseExceptionWhenDependentId(){
    Assertions.assertThrows(DatabaseException.class,() -> {
      service.delete(dependentId);
    });
    Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
  }

  @Test
  public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
    Assertions.assertThrows(ResourceNotFoundException.class,() -> {
      service.delete(nonExistingId);
    });
    Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
  }

  @Test
  public void deleteShouldDoNothingWhenIdExists(){
    Assertions.assertDoesNotThrow(() -> {
      service.delete(existingId);
    });
    Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
  }
}
