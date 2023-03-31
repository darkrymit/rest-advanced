package com.epam.esm.web.config;

import com.epam.esm.web.dto.assembler.GiftCertificateModelAssembler;
import com.epam.esm.web.dto.assembler.OrderModelAssembler;
import com.epam.esm.web.dto.assembler.TagModelAssembler;
import com.epam.esm.web.dto.assembler.UserModelAssembler;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HateoasConfig {

  @Bean
  GiftCertificateModelAssembler giftCertificateModelAssembler(TagModelAssembler tagModelAssembler,ModelMapper modelMapper){
    return new GiftCertificateModelAssembler(tagModelAssembler,modelMapper);
  }

  @Bean
  OrderModelAssembler orderModelAssembler(ModelMapper modelMapper){
    return new OrderModelAssembler(modelMapper);
  }

  @Bean
  TagModelAssembler tagModelAssembler(ModelMapper modelMapper){
    return new TagModelAssembler(modelMapper);
  }

  @Bean
  UserModelAssembler userModelAssembler(ModelMapper modelMapper){
    return new UserModelAssembler(modelMapper);
  }
}
