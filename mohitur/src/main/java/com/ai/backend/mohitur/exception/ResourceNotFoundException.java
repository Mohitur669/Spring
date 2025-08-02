package com.ai.backend.mohitur.exception;

public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }

    public ResourceNotFoundException(String resourceType, Object id) {
        super(String.format("%s not found with id: %s", resourceType, id), "RESOURCE_NOT_FOUND");
    }

    public ResourceNotFoundException(String resourceType, String field, Object value) {
        super(String.format("%s not found with %s: %s", resourceType, field, value), "RESOURCE_NOT_FOUND");
    }
}