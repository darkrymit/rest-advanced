package com.epam.esm.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserOAuthDetails {

  private String email;

  private String firstName;

  private String lastName;
}