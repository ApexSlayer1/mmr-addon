package com.example.examplemod.registry;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;

public record CasingFamilyDefinition(
        String baseId,
        String baseDisplayName,
        String variantIdPrefix,
        String variantDisplayPrefix,
        ResourceLocation baseTexture,
        CasingFrameType frameType,
        boolean colored
) {
    public CasingFamilyDefinition {
        Objects.requireNonNull(baseId, "baseId");
        Objects.requireNonNull(baseDisplayName, "baseDisplayName");
        Objects.requireNonNull(variantIdPrefix, "variantIdPrefix");
        Objects.requireNonNull(variantDisplayPrefix, "variantDisplayPrefix");
        Objects.requireNonNull(baseTexture, "baseTexture");
        Objects.requireNonNull(frameType, "frameType");
    }

    public static CasingFamilyDefinition of(
            String baseId,
            String baseDisplayName,
            String variantIdPrefix,
            String variantDisplayPrefix,
            ResourceLocation baseTexture,
            CasingFrameType frameType
    ) {
        return new CasingFamilyDefinition(
                baseId,
                baseDisplayName,
                variantIdPrefix,
                variantDisplayPrefix,
                baseTexture,
                frameType,
                false
        );
    }

    public static CasingFamilyDefinition fromVariant(
            String variantId,
            String variantDisplayName,
            ResourceLocation baseTexture,
            CasingFrameType frameType
    ) {
        String idSuffix = "_" + frameType.baseIdSuffix();
        String displaySuffix = " " + frameType.baseDisplayName();
        String variantIdPrefix = variantId.endsWith(idSuffix)
                ? variantId.substring(0, variantId.length() - idSuffix.length())
                : variantId;
        String variantDisplayPrefix = variantDisplayName.endsWith(displaySuffix)
                ? variantDisplayName.substring(0, variantDisplayName.length() - displaySuffix.length())
                : variantDisplayName;
        return of(
                variantId,
                variantDisplayName,
                variantIdPrefix,
                variantDisplayPrefix,
                baseTexture,
                frameType
        );
    }

    public List<CasingDefinition> expand(List<CasingOverlayType> overlayTypes) {
        return Stream.concat(
                Stream.of(CasingDefinition.baseVariant(this)),
                overlayTypes.stream().map(overlayType -> CasingDefinition.overlayVariant(this, overlayType))
        ).toList();
    }

    public CasingFamilyDefinition color() {
        return new CasingFamilyDefinition(
                baseId,
                baseDisplayName,
                variantIdPrefix,
                variantDisplayPrefix,
                baseTexture,
                frameType,
                true
        );
    }
}
