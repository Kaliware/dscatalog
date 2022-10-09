package com.kaliware.dscatalog.services;

import com.kaliware.dscatalog.entities.Category;
import com.kaliware.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService{

  @Autowired
  CategoryRepository repository;

  public List<Category> findAll(){
    return repository.findAll();
  }
}
