package com.twd.SpringSecurityJWT.exception;

public class FixedAssetNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FixedAssetNotFoundException(String string) {
        super("FixedAsset not found with id: " + string);
    }
}
