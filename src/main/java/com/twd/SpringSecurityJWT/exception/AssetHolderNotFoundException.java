package com.twd.SpringSecurityJWT.exception;

public class AssetHolderNotFoundException extends RuntimeException {
    public AssetHolderNotFoundException(Long id) {
        super("Asset Holder not found with id: " + id);
    }
}