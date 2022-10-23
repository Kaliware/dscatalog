package com.kaliware.dscatalog.dto;

import com.kaliware.dscatalog.entities.Category;
import com.kaliware.dscatalog.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO implements Serializable{
  private static final long serialVersionUID = 1L;

  @NonNull
  private Long id;

  @NonNull
  private String name;

  @NonNull
  private String description;

  @NonNull
  private Double price;

  @NonNull
  private String imgUrl;

  @NonNull
  private Instant date;

  private List<CategoryDTO> categories = new ArrayList<>();

  public ProductDTO(Product entity){
    this.id = entity.getId();
    this.name = entity.getName();
    this.description = entity.getDescription();
    this.price = entity.getPrice();
    this.imgUrl = entity.getImgUrl();
    this.date = entity.getDate();
  }

  public ProductDTO(Product entity, Set<Category> categories){
    this(entity);
    categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
  }
}
