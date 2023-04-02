package com.epam.esm.web.filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.epam.esm.persistance.dao.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import security.JwtTestConfig;
import security.WithCustomJwtToken;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JwtTestConfig.class})
class OAuthUserDetailsCreateFilterTest {

  @Mock
  UserRepository userRepository;

  @InjectMocks
  OAuthUserDetailsCreateFilter filter;

  @Test
  @WithCustomJwtToken
  void doFilterShouldNotCreateUserWhenUserByIdExist() throws Exception{
    MockHttpServletRequest req = new MockHttpServletRequest();
    MockHttpServletResponse res = new MockHttpServletResponse();
    MockFilterChain chain = new MockFilterChain();

    when(userRepository.existsById(any())).thenReturn(true);

    filter.doFilter(req, res, chain);

    verify(userRepository, times(1)).existsById(any());
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  @WithCustomJwtToken
  void doFilterShouldSaveUserWhenUserByIdNotExist() throws Exception{
    MockHttpServletRequest req = new MockHttpServletRequest();
    MockHttpServletResponse res = new MockHttpServletResponse();
    MockFilterChain chain = new MockFilterChain();

    when(userRepository.existsById(any())).thenReturn(false);

    filter.doFilter(req, res, chain);

    verify(userRepository, times(1)).existsById(any());
    verify(userRepository, times(1)).save(any());
    verifyNoMoreInteractions(userRepository);
  }
}