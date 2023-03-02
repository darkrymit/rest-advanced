package com.epam.esm.web.filter;

import com.epam.esm.persistance.dao.UserRepository;
import com.epam.esm.persistance.entity.User;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
@Slf4j
public class OAuthUserDetailsCreateFilter extends OncePerRequestFilter {

  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof JwtAuthenticationToken)) {
      filterChain.doFilter(request, response);
    }
    log.trace("OAuthUserDetailsCreateFilter start analyze");
    JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
    UUID uuid = UUID.fromString(jwtAuthenticationToken.getName());

    if (!userRepository.existsById(uuid)) {
      User user = new User();
      user.setId(uuid);
      userRepository.save(user);
      log.info("OAuthUserDetailsCreateFilter add user with id {}",uuid);
    }

    filterChain.doFilter(request, response);
  }
}
