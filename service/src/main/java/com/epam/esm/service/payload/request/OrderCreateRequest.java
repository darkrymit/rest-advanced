package com.epam.esm.service.payload.request;


import com.epam.esm.validation.giftcertificate.Name;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Value;

@Value
public class OrderCreateRequest {

  @NotNull
  List<@Name String> giftCertificates;

}
