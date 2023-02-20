package com.kaliware.dscatalog.dto;

import com.kaliware.dscatalog.entities.User;

import java.util.HashSet;
import java.util.Set;

public class UserInsertDTO extends UserDTO{
  private static final long serialVersionUID = 1L;

  private String password;

  public UserInsertDTO(){
    super();
  }

  public String getPassword(){
    return password;
  }

  public void setPassword(String password){
    this.password = password;
  }
}
