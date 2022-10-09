package com.kaliware.dscatalog.controllers;

import com.kaliware.dscatalog.dto.CategoryDTO;
import com.kaliware.dscatalog.entities.Category;
import com.kaliware.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController{

  @Autowired
  CategoryService service;

  @GetMapping
  public ResponseEntity<List<CategoryDTO>> findAll(){
    return ResponseEntity.ok().body(service.findAll());
  }
}
