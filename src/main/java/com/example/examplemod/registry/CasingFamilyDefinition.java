package com.example.examplemod.registry;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;

public record CasingFamilyDefinition(
        String idPrefix,
        String displayPrefix,
        ResourceLocation baseTexture,
        CasingFrameType frameType,
        boolean colored
) {
    public CasingFamilyDefinition {
        Objects.requireNonNull(idPrefix, "idPrefix");
        Objects.requireNonNull(displayPrefix, "displayPrefix");
        Objects.requireNonNull(baseTexture, "baseTexture");
        Objects.requireNonNull(frameType, "frameType");
    }

    public static CasingFamilyDefinition fromVariant(
            String variantId,
            String variantDisplayName,
            ResourceLocation baseTexture,
            CasingFrameType frameType
    ) {
        String idSuffix = "_" + frameType.baseIdSuffix();
        String displaySuffix = " " + frameType.baseDisplayName();
        String idPrefix = variantId.endsWith(idSuffix)
                ? variantId.substring(0, variantId.length() - idSuffix.length())
                : variantId;
        String displayPrefix = variantDisplayName.endsWith(displaySuffix)
                ? variantDisplayName.substring(0, variantDisplayName.length() - displaySuffix.length())
                : variantDisplayName;
        return new CasingFamilyDefinition(idPrefix, displayPrefix, baseTexture, frameType, false);
    }

    public List<CasingDefinition> expand(List<CasingOverlayType> overlayTypes) {
        return Stream.concat(
                Stream.of(CasingDefinition.baseVariant(this)),
                overlayTypes.stream().map(overlayType -> CasingDefinition.overlayVariant(this, overlayType))
        ).toList();
    }

    public CasingFamilyDefinition color() {
        return new CasingFamilyDefinition(idPrefix, displayPrefix, baseTexture, frameType, true);
    }
}
