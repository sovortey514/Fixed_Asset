package com.twd.SpringSecurityJWT.exception;

public class BuildingNotFoundException extends RuntimeException {
    public BuildingNotFoundException(Long id) {
        super("Building not found with id: " + id);
    }
}