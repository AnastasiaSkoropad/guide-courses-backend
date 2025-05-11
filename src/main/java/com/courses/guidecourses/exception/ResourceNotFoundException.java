package com.courses.guidecourses.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(String.format("%s з ідентифікатором %s не знайдено", resourceName, resourceId));
    }

}
