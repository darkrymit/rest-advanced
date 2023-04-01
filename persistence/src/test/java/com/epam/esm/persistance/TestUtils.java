package com.epam.esm.persistance;

import nl.jqno.equalsverifier.ConfiguredEqualsVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class TestUtils {
  public static ConfiguredEqualsVerifier getEqualsVerifier() {
    return EqualsVerifier.simple().suppress(Warning.STRICT_HASHCODE)
        .suppress(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY)
        .suppress(Warning.SURROGATE_KEY);
  }
}
