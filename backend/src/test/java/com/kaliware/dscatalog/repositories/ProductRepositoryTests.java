package com.kaliware.dscatalog.repositories;

import com.kaliware.dscatalog.entities.Product;
import com.kaliware.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests{

  @Autowired
  private ProductRepository repository;

  long existingId;
  long nonExistingId;
  long countTotalProducts;

  @BeforeEach
  void setUp() throws Exception{
    existingId = 1L;
    nonExistingId = 1000L;
    countTotalProducts = 25;
  }

  @Test
  public void deleteShouldDeleteObjectWhenIdExist(){
    repository.deleteById(existingId);
    Optional<Product> result = repository.findById(existingId);
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

    Product product = Factory.createProduct();
    product.setId(null);

    product = repository.save(product);

    Assertions.assertNotNull(product.getId());
    Assertions.assertEquals(countTotalProducts + 1, product.getId());
  }

  @Test
  public void findByIdShouldReturnNonEmptyOptionalWhenIdExists(){
    Optional<Product> result = repository.findById(existingId);
    Assertions.assertTrue(result.isPresent());
  }

  @Test
  public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists(){
    Optional<Product> result = repository.findById(nonExistingId);
    Assertions.assertTrue(result.isEmpty());
  }
}
