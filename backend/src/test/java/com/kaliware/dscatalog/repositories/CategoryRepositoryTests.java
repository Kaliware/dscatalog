package com.kaliware.dscatalog.repositories;

import com.kaliware.dscatalog.entities.Category;
import com.kaliware.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class CategoryRepositoryTests{

  @Autowired
  private CategoryRepository repository;

  long existingId;
  long nonExistingId;
  long countTotalCategorys;

  @BeforeEach
  void setUp() throws Exception{
    existingId = 1L;
    nonExistingId = 4L;
    countTotalCategorys = 3;
  }

  @Test
  public void deleteShouldDeleteObjectWhenIdExist(){
    repository.deleteById(existingId);
    Optional<Category> result = repository.findById(existingId);
    Assertions.assertFalse(result.isPresent());
  }


  @Test
  public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist(){
    Assertions.assertThrows(EmptyResultDataAccessException.class,() -> {
      repository.deleteById(nonExistingId);
    });
  }

  @Test
  public void saveShouldPersistWithAutoincrementWhenIdIsNull(){

    Category product = Factory.createCategory();
    product.setId(null);

    product = repository.save(product);

    Assertions.assertNotNull(product.getId());
    Assertions.assertEquals(countTotalCategorys + 1, product.getId());
  }

  @Test
  public void findByIdShouldReturnNonEmptyOptionalWhenIdExists(){
    Optional<Category> result = repository.findById(existingId);
    Assertions.assertTrue(result.isPresent());
  }

  @Test
  public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists(){
    Optional<Category> result = repository.findById(nonExistingId);
    Assertions.assertTrue(result.isEmpty());
  }
}
