package com.pet.businessdomain.personservice.exceptions;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s no encontrado con %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceName = null;
        this.fieldName = null;
        this.fieldValue = null;
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s no encontrado con id: %d", resourceName, id));
        this.resourceName = resourceName;
        this.fieldName = "id";
        this.fieldValue = id;
    }

    public ResourceNotFoundException(Class<?> resourceClass, Long id) {
        this(resourceClass.getSimpleName(), id);
    }
}