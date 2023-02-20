package com.kaliware.dscatalog.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaliware.dscatalog.dto.CategoryDTO;
import com.kaliware.dscatalog.services.CategoryService;
import com.kaliware.dscatalog.services.exceptions.DatabaseException;
import com.kaliware.dscatalog.services.exceptions.ResourceNotFoundException;
import com.kaliware.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTests{

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CategoryService service;

  @Autowired
  private ObjectMapper objectMapper;

  private Long existingId;
  private Long nonExistingId;
  private Long dependentId;
  private CategoryDTO productDTO;
  private PageImpl<CategoryDTO> page;

  @BeforeEach
  void setUp() throws Exception{
    existingId = 1L;
    nonExistingId = 2L;
    dependentId = 3L;
    productDTO = Factory.createCategoryDTO();
    page = new PageImpl<>(List.of(productDTO));

    Mockito.when(service.findAllPaged(any())).thenReturn(page);

    Mockito.when(service.findById(existingId)).thenReturn(productDTO);
    Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

    Mockito.when(service.update(eq(existingId), any())).thenReturn(productDTO);
    Mockito.when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

    Mockito.when(service.insert(any())).thenReturn(productDTO);

    Mockito.doNothing().when(service).delete(existingId);
    Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
    Mockito.doThrow(DatabaseException.class).when(service).delete(dependentId);

  }
  @Test
  public void insertShouldReturnCategoryDTOWhenIdExist() throws Exception{
    String jsonBody = objectMapper.writeValueAsString(productDTO);

    ResultActions result =
        mockMvc.perform(post("/categories")
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isCreated());
    result.andExpect(jsonPath("$.id").exists());
    result.andExpect(jsonPath("$.name").exists());
  }

  @Test
  public void deleteShouldReturnNoContentWhenIdExist() throws Exception{

    ResultActions result =
        mockMvc.perform(delete("/categories/{id}", existingId)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isNoContent());
  }

  @Test
  public void deleteShouldReturnBadRequestWhenDependentId() throws Exception{

    ResultActions result =
        mockMvc.perform(delete("/categories/{id}", dependentId)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isBadRequest());
  }

  @Test
  public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{

    ResultActions result =
        mockMvc.perform(delete("/categories/{id}", nonExistingId)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isNotFound());
  }

  @Test
  public void updateShouldReturnCategoryDTOWhenIdExist() throws Exception{
    String jsonBody = objectMapper.writeValueAsString(productDTO);

    ResultActions result =
        mockMvc.perform(put("/categories/{id}", existingId)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.id").exists());
    result.andExpect(jsonPath("$.name").exists());
  }

  @Test
  public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
    String jsonBody = objectMapper.writeValueAsString(productDTO);

    ResultActions result =
        mockMvc.perform(put("/categories/{id}", nonExistingId)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isNotFound());
    result.andExpect(jsonPath("$.timestamp").exists());
    result.andExpect(jsonPath("$.status").exists());
    result.andExpect(jsonPath("$.error").exists());
    result.andExpect(jsonPath("$.message").isEmpty());
    result.andExpect(jsonPath("$.path").exists());
  }

  @Test
  public void findAllShouldReturnPage() throws Exception{
    ResultActions result =
        mockMvc.perform(get("/categories")
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
  }

  @Test
  public void findByIdShouldReturnCategoryWhenIdExist() throws Exception{
    ResultActions result =
        mockMvc.perform(get("/categories/{id}", existingId)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.id").exists());
    result.andExpect(jsonPath("$.name").exists());
  }

  @Test
  public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
    ResultActions result =
        mockMvc.perform(get("/categories/{id}", nonExistingId)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isNotFound());
    result.andExpect(jsonPath("$.timestamp").exists());
    result.andExpect(jsonPath("$.status").exists());
    result.andExpect(jsonPath("$.error").exists());
    result.andExpect(jsonPath("$.message").isEmpty());
    result.andExpect(jsonPath("$.path").exists());
  }
}
