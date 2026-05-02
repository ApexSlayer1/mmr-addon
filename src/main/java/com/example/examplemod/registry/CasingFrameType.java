package com.example.examplemod.registry;

import java.util.List;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;

public record CasingFrameType(
        String name,
        String baseIdSuffix,
        String baseDisplayName,
        String connectionType,
        List<ResourceLocation> nativeConnections,
        ResourceLocation itemOverlayTexture,
        ResourceLocation ctmOverlayCenter,
        ResourceLocation ctmOverlayHorizontal,
        ResourceLocation ctmOverlayParticle,
        ResourceLocation ctmOverlayVertical
) {
    public CasingFrameType {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(baseIdSuffix, "baseIdSuffix");
        Objects.requireNonNull(baseDisplayName, "baseDisplayName");
        Objects.requireNonNull(connectionType, "connectionType");
        nativeConnections = List.copyOf(nativeConnections);
        Objects.requireNonNull(itemOverlayTexture, "itemOverlayTexture");
    }

    public boolean hasConnectedFrameOverlay() {
        return ctmOverlayCenter != null
                && ctmOverlayHorizontal != null
                && ctmOverlayParticle != null
                && ctmOverlayVertical != null;
    }

    public String displayPrefix() {
        String suffix = " Casing";
        if (baseDisplayName.endsWith(suffix)) {
            return baseDisplayName.substring(0, baseDisplayName.length() - suffix.length());
        }
        return baseDisplayName;
    }
}
