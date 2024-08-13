package com.twd.SpringSecurityJWT.service;

import com.twd.SpringSecurityJWT.entity.AssetHolder;

import java.util.List;
import java.util.Optional;

public interface AssetHolderService {
    AssetHolder createAssetHolder(AssetHolder assetHolder);
    void deleteAssetHolder(Long id);
    List<AssetHolder> getAllAssetHolders();
    Optional<AssetHolder> getAssetHolderById(Long id);
    AssetHolder updateAssetHolder(Long id, AssetHolder assetHolder);
}
