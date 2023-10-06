package com.example.demo.domain;


import lombok.Data;

import java.util.List;

@Data
public class UserServiceResponse {
  private List<User> users;
}
