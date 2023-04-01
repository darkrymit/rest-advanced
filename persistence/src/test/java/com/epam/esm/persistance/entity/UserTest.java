package com.epam.esm.persistance.entity;

import static com.epam.esm.persistance.TestUtils.getEqualsVerifier;

import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void equalForHibernate() {
    getEqualsVerifier().forClass(User.class).verify();
  }

}