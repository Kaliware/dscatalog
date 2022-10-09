package com.kaliware.dscatalog.services;

import com.kaliware.dscatalog.dto.CategoryDTO;
import com.kaliware.dscatalog.entities.Category;
import com.kaliware.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService{

  @Autowired
  CategoryRepository repository;

  @Transactional(readOnly = true)
  public List<CategoryDTO> findAll(){
    List<Category> list = repository.findAll();
    return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
  }
}
