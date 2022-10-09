package com.kaliware.dscatalog.controllers;

import com.kaliware.dscatalog.dto.CategoryDTO;
import com.kaliware.dscatalog.entities.Category;
import com.kaliware.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

  @GetMapping(value = "/{id}")
  public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
    return ResponseEntity.ok().body(service.findById(id));
  }

  @PostMapping
  public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
    dto = service.insert(dto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("{id}")
            .buildAndExpand(dto.getId())
            .toUri();
    return ResponseEntity.created(uri).body(dto);
  }
}
