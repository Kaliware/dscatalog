package com.kaliware.dscatalog.dto;

import com.kaliware.dscatalog.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO implements Serializable{
  private static final long serialVersionUID = 1L;

  @NonNull
  private Long id;

  @NonNull
  private String name;

  public CategoryDTO(Category entity){
    this.id = entity.getId();
    this.name = entity.getName();
  }
}
