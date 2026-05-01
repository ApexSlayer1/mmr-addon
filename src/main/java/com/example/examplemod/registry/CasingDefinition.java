package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;

public record CasingDefinition(
        String id,
        String displayName,
        String connectionType,
        boolean colored,
        List<ResourceLocation> nativeConnections,
        Map<String, ResourceLocation> blockModelTextures,
        Map<String, ResourceLocation> ctmTextures,
        List<GeneratedTexture> generatedTextures
) {
    public CasingDefinition {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(displayName, "displayName");
        Objects.requireNonNull(connectionType, "connectionType");
        nativeConnections = List.copyOf(nativeConnections);
        blockModelTextures = Map.copyOf(blockModelTextures);
        ctmTextures = Map.copyOf(ctmTextures);
        generatedTextures = List.copyOf(generatedTextures);
    }

    public ResourceLocation connectionTag() {
        return ExampleMod.id("casing_connect/" + connectionType);
    }

    public static CasingDefinition baseVariant(CasingFamilyDefinition family) {
        return create(family, null);
    }

    public static CasingDefinition overlayVariant(CasingFamilyDefinition family, CasingOverlayType overlayType) {
        return create(family, overlayType);
    }

    private static CasingDefinition create(CasingFamilyDefinition family, CasingOverlayType overlayType) {
        CasingFrameType frameType = family.frameType();
        ResourceLocation baseTexture = family.baseTexture();
        ResourceLocation centerTexture = derivedTexture(baseTexture, "_corners", MmrCasingAssets.PLAIN_CENTER);
        ResourceLocation emptyTexture = derivedTexture(baseTexture, "_empty", baseTexture);
        ResourceLocation horizontalTexture = derivedTexture(baseTexture, "_horizontal", MmrCasingAssets.PLAIN_HORIZONTAL);
        ResourceLocation verticalTexture = derivedTexture(baseTexture, "_vertical", MmrCasingAssets.PLAIN_VERTICAL);
        ResourceLocation fixedOverlayTexture = overlayType == null
                ? MmrCasingAssets.OVERLAY_TRANSPARENT
                : overlayType.texture();

        String id = family.idPrefix() + "_" + (overlayType == null ? frameType.baseIdSuffix() : overlayType.idSuffix());
        String displayName = family.displayPrefix() + " " + (overlayType == null ? frameType.baseDisplayName() : overlayType.displayName());

        LinkedHashMap<String, ResourceLocation> ctmTextures = new LinkedHashMap<>();
        ctmTextures.put("center", centerTexture);
        ctmTextures.put("empty", emptyTexture);
        ctmTextures.put("horizontal", horizontalTexture);
        ctmTextures.put("particle", baseTexture);
        ctmTextures.put("vertical", verticalTexture);

        List<GeneratedTexture> generatedTextures = new ArrayList<>();
        ResourceLocation itemOverlayTexture = fixedOverlayTexture;

        if (frameType.hasConnectedFrameOverlay()) {
            ctmTextures.put("ov_center", frameType.ctmOverlayCenter());
            ctmTextures.put("ov_horizontal", frameType.ctmOverlayHorizontal());
            ctmTextures.put("ov_particle", frameType.ctmOverlayParticle());
            ctmTextures.put("ov_vertical", frameType.ctmOverlayVertical());
            ctmTextures.put("overlay", fixedOverlayTexture);

            ResourceLocation overlayAll = generatedTexture(id, "overlay_all");
            generatedTextures.add(new GeneratedTexture(overlayAll, List.of(frameType.itemOverlayTexture(), fixedOverlayTexture)));
            itemOverlayTexture = overlayAll;
        } else if (overlayType != null) {
            ctmTextures.put("overlay", fixedOverlayTexture);
        }

        return new CasingDefinition(
                id,
                displayName,
                frameType.connectionType(),
                family.colored(),
                frameType.nativeConnections(),
                Map.of(
                        "bg_all", baseTexture,
                        "ov_all", itemOverlayTexture
                ),
                ctmTextures,
                generatedTextures
        );
    }

    private static ResourceLocation generatedTexture(String id, String textureName) {
        return ExampleMod.id("block/generated/" + id + "/" + textureName);
    }

    private static ResourceLocation derivedTexture(ResourceLocation baseTexture, String suffix, ResourceLocation fallback) {
        if (!ExampleMod.MODID.equals(baseTexture.getNamespace())) {
            return fallback;
        }
        return ResourceLocation.fromNamespaceAndPath(baseTexture.getNamespace(), baseTexture.getPath() + suffix);
    }
}
