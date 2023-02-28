package com.epam.esm.service;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {

  private UUID id;

  private String email;

  private String firstName;

  private String lastName;

  private Instant creationDate;
}
