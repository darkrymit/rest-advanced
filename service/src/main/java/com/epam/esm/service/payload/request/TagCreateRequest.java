package com.epam.esm.service.payload.request;

import com.epam.esm.validation.tag.Name;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagCreateRequest {
  @Name
  private String name;
}
