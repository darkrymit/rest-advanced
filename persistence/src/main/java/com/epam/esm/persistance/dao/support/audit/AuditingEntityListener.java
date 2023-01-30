package com.epam.esm.persistance.dao.support.audit;

import com.epam.esm.persistance.dao.support.audit.annotation.CreatedBy;
import com.epam.esm.persistance.dao.support.audit.annotation.CreatedDate;
import com.epam.esm.persistance.dao.support.audit.annotation.LastModifiedBy;
import com.epam.esm.persistance.dao.support.audit.annotation.LastModifiedDate;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;

@Slf4j
public class AuditingEntityListener implements ApplicationContextAware, InitializingBean {

  private ApplicationContext context;

  private List<AuditingHandlerWrapper> auditingProviders;

  private static Optional<Field> getFirst(List<Field> fields,
      Class<? extends Annotation> annotation) {
    return fields.stream().filter(field -> field.isAnnotationPresent(annotation)).findFirst();
  }

  private static void writeField(Object target, Field field, Object value) {
    try {
      field.setAccessible(true);
      field.set(target, value);
    } catch (IllegalAccessException exception) {
      throw new RuntimeException(exception);
    }
  }

  private static List<Field> getFields(Object target) {
    return Arrays.stream(target.getClass().getDeclaredFields())
        .filter(field -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
  }

  @PrePersist
  public void touchForCreate(Object target) {
    Assert.notNull(target, "Entity must not be null!");

    List<Field> fields = getFields(target);

    Optional<Field> createdDateFieldOptional = getFirst(fields, CreatedDate.class);

    createdDateFieldOptional.ifPresent(field -> processField(target, field));

    Optional<Field> createdByFieldOptional = getFirst(fields, CreatedBy.class);

    createdByFieldOptional.ifPresent(field -> processField(target, field));
  }

  @PreUpdate
  public void touchForUpdate(Object target) {
    Assert.notNull(target, "Entity must not be null!");

    List<Field> fields = getFields(target);

    Optional<Field> createdDateFieldOptional = getFirst(fields, LastModifiedDate.class);

    createdDateFieldOptional.ifPresent(field -> processField(target, field));

    Optional<Field> createdByFieldOptional = getFirst(fields, LastModifiedBy.class);

    createdByFieldOptional.ifPresent(field -> processField(target, field));
  }

  private void processField(Object target, Field field) {
    AuditingHandlerWrapper wrapper = getHandlerWrapper(field);
    Object value = wrapper.auditingProvider.getCurrentAuditor().orElse(null);
    writeField(target, field, value);
  }

  private AuditingHandlerWrapper getHandlerWrapper(Field field) {
    return auditingProviders.stream()
        .filter(wrapper -> wrapper.getProvidedClass().isAssignableFrom(field.getType()))
        .findFirst().orElseThrow();
  }


  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = applicationContext;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    auditingProviders = Arrays.stream(context.getBeanNamesForType(AuditingProvider.class))
        .map(this::resolve).collect(Collectors.toList());

    log.info("Resolved with providers info {}", auditingProviders);

  }

  private AuditingHandlerWrapper resolve(String name) {

    AuditingProvider<?> provider = context.getBean(name, AuditingProvider.class);

    Class<?> providerType = context.getType(name);

    return new AuditingHandlerWrapper(provider,
        GenericTypeResolver.resolveTypeArgument(providerType, AuditingProvider.class));
  }

  @Value
  private static class AuditingHandlerWrapper {

    AuditingProvider<?> auditingProvider;
    Class<?> providedClass;
  }
}
