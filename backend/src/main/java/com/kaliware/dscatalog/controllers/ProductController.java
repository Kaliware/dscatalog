package com.kaliware.dscatalog.controllers;

import com.kaliware.dscatalog.dto.ProductDTO;
import com.kaliware.dscatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductController{

  @Autowired
  ProductService service;

  @GetMapping
  public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable){
    Page<ProductDTO> list = service.findAllPaged(pageable);
    return ResponseEntity.ok().body(list);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
    return ResponseEntity.ok().body(service.findById(id));
  }

  @PostMapping
  public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto){
    dto = service.insert(dto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("{id}")
            .buildAndExpand(dto.getId())
            .toUri();
    return ResponseEntity.created(uri).body(dto);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO dto){
    dto = service.update(id ,dto);
    return ResponseEntity.ok().body(dto);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
