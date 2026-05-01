package com.example.examplemod.registry;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;

public record CasingOverlayType(
        String idSuffix,
        String displayName,
        ResourceLocation texture
) {
    public CasingOverlayType {
        Objects.requireNonNull(idSuffix, "idSuffix");
        Objects.requireNonNull(displayName, "displayName");
        Objects.requireNonNull(texture, "texture");
    }
}
