package com.example.examplemod.registry;

import java.util.List;
import java.util.Locale;
import net.minecraft.resources.ResourceLocation;

import static com.example.examplemod.ExampleMod.MODID;

public final class CasingDefinitions {
    private CasingDefinitions() {
    }

    // Single-source casing config:
    // 1. Add a frame type.
    // 2. Add an overlay type.
    // 3. Add a family.
    // The full block/item/model/lang/tag variant set is generated automatically.
    //
    // Quick examples:
    // `public static final CasingFrameType STEEL_FRAME = addonConnectedFrame("steel");`
    // Expects textures under assets/examplemod/textures/block/frame/:
    // steel_overlay.png, steel_corners.png, steel_horizontal.png,
    // steel_particle.png, steel_vertical.png
    //
    // `addonOverlay("panel")`
    // Expects assets/examplemod/textures/block/casing/overlay/panel.png
    //
    // `addonFamilyFromFrame("stone_brick", "Stone Brick", STEEL_FRAME).color()`
    // Makes the full generated family use the controller color tint like native MMR casings.
    //
    // `addonFamilyFromFrame("stone_brick", "Stone Brick", STEEL_FRAME)`
    // Expects assets/examplemod/textures/block/casing/:
    // stone_brick.png, stone_brick_corners.png, stone_brick_horizontal.png,
    // stone_brick_vertical.png, optional stone_brick_empty.png
    public static final List<CasingOverlayType> OVERLAY_TYPES = List.of(
            mmrOverlay("firebox", "Firebox Casing"),
            mmrOverlay("gearbox", "Machine Gearbox Casing"),
            mmrOverlay("vent", "Machine Vent Casing"),
            mmrOverlay("circuitry", "Circuitry Casing")
    );

    public static final CasingFrameType PLAIN_FRAME = plainFrame(
            "plain",
            "plain_casing",
            "Plain Casing",
            plainFamilyNativeConnections()
    );

    public static final CasingFrameType REINFORCED_FRAME = mmrFrame(
            "reinforced",
            "Reinforced Casing",
            List.of(nativeBlock("casing_reinforced"))
    );

    // Example of the shorthand for a custom frame:
    // Textures expected under assets/examplemod/textures/block/frame/:
    // arcane_overlay.png, arcane_corners.png, arcane_horizontal.png,
    // arcane_particle.png, arcane_vertical.png
    public static final CasingFrameType ARCANE_FRAME = addonConnectedFrame(
            "arcane",
            List.of(nativeBlock("casing_arcane"))
    );

    public static final List<CasingFamilyDefinition> FAMILIES = List.of(
            // Uses MMR's native plain casing texture as the base and auto-adds all shared overlays.
            family("example", "Example", mmrBlock("casing_plain"), PLAIN_FRAME).color(),
            // Uses this addon's stone brick CTM textures and auto-adds all shared overlays
            // on top of the reinforced frame family.
            addonFamilyFromFrame("stone_brick", "Stone Brick", REINFORCED_FRAME),
            addonFamilyFromFrame("spruce_planks","spruce",REINFORCED_FRAME)
    );


    public static final List<CasingDefinition> ALL = FAMILIES.stream()
            .flatMap(family -> family.expand(OVERLAY_TYPES).stream())
            .toList();

    public static CasingFamilyDefinition family(
            String idPrefix,
            String displayPrefix,
            ResourceLocation baseTexture,
            CasingFrameType frameType
    ) {
        return new CasingFamilyDefinition(idPrefix, displayPrefix, baseTexture, frameType, false);
    }

    public static CasingFamilyDefinition familyFromVariant(
            String variantId,
            String variantDisplayName,
            ResourceLocation baseTexture,
            CasingFrameType frameType
    ) {
        return CasingFamilyDefinition.fromVariant(variantId, variantDisplayName, baseTexture, frameType);
    }

    public static CasingFamilyDefinition addonFamily(String name, CasingFrameType frameType) {
        return addonFamily(name, titleCase(name), frameType);
    }

    public static CasingFamilyDefinition addonFamily(String name, String displayName, CasingFrameType frameType) {
        String path = normalizedName(name);
        return family(path, displayName, modBlock("casing/" + path), frameType);
    }

    public static CasingFamilyDefinition addonFamilyFromFrame(String name, CasingFrameType frameType) {
        return addonFamilyFromFrame(name, titleCase(name), frameType);
    }

    public static CasingFamilyDefinition addonFamilyFromFrame(String name, String displayName, CasingFrameType frameType) {
        String path = normalizedName(name);
        return familyFromVariant(
                path + "_" + frameType.baseIdSuffix(),
                displayName + " " + frameType.baseDisplayName(),
                modBlock("casing/" + path),
                frameType
        );
    }

    public static CasingFrameType plainFrame(
            String name,
            String baseIdSuffix,
            String baseDisplayName,
            List<ResourceLocation> nativeConnections
    ) {
        return new CasingFrameType(
                name,
                baseIdSuffix,
                baseDisplayName,
                name,
                nativeConnections,
                MmrCasingAssets.OVERLAY_TRANSPARENT,
                null,
                null,
                null,
                null
        );
    }

    public static CasingFrameType connectedFrame(
            String name,
            String baseIdSuffix,
            String baseDisplayName,
            List<ResourceLocation> nativeConnections,
            ResourceLocation itemOverlayTexture,
            ResourceLocation ctmOverlayCenter,
            ResourceLocation ctmOverlayHorizontal,
            ResourceLocation ctmOverlayParticle,
            ResourceLocation ctmOverlayVertical
    ) {
        return new CasingFrameType(
                name,
                baseIdSuffix,
                baseDisplayName,
                name,
                nativeConnections,
                itemOverlayTexture,
                ctmOverlayCenter,
                ctmOverlayHorizontal,
                ctmOverlayParticle,
                ctmOverlayVertical
        );
    }

    public static CasingFrameType mmrFrame(
            String name,
            String baseDisplayName,
            List<ResourceLocation> nativeConnections
    ) {
        String path = normalizedName(name);
        return connectedFrame(
                path,
                path + "_casing",
                baseDisplayName,
                nativeConnections,
                mmrBlock("overlay_" + path),
                mmrBlock("casing_" + path + "_corners"),
                mmrBlock("casing_" + path + "_horizontal"),
                mmrBlock("casing_" + path),
                mmrBlock("casing_" + path + "_vertical")
        );
    }

    public static CasingFrameType addonConnectedFrame(String name) {
        return addonConnectedFrame(name, List.of());
    }

    public static CasingFrameType addonConnectedFrame(String name, List<ResourceLocation> nativeConnections) {
        String path = normalizedName(name);
        String title = titleCase(name);
        return connectedFrame(
                path,
                path + "_casing",
                title + " Casing",
                nativeConnections,
                modBlock("frame/" + path + "_overlay"),
                modBlock("frame/" + path + "_corners"),
                modBlock("frame/" + path + "_horizontal"),
                modBlock("frame/" + path + "_particle"),
                modBlock("frame/" + path + "_vertical")
        );
    }

    public static CasingOverlayType overlay(String idSuffix, String displayName, String texturePath) {
        return new CasingOverlayType(idSuffix, displayName, mmrBlock(texturePath));
    }

    public static CasingOverlayType mmrOverlay(String name, String displayName) {
        String path = normalizedName(name);
        return new CasingOverlayType(path + "_casing", displayName, mmrBlock("overlay_" + path));
    }

    public static CasingOverlayType addonOverlay(String name) {
        return addonOverlay(name, titleCase(name) + " Casing");
    }

    public static CasingOverlayType addonOverlay(String name, String displayName) {
        String path = normalizedName(name);
        return new CasingOverlayType(path + "_casing", displayName, modBlock("casing/overlay/" + path));
    }

    private static List<ResourceLocation> plainFamilyNativeConnections() {
        return List.of(
                nativeBlock("casing_plain"),
                nativeBlock("casing_firebox"),
                nativeBlock("casing_gearbox"),
                nativeBlock("casing_vent"),
                nativeBlock("casing_circuitry")
        );
    }

    private static ResourceLocation mmr(String path) {
        return ResourceLocation.fromNamespaceAndPath("modular_machinery_reborn", path);
    }

    private static ResourceLocation mmrBlock(String path) {
        return mmr("block/" + path);
    }

    private static ResourceLocation modBlock(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, "block/" + path);
    }

    private static String normalizedName(String name) {
        return name.trim().toLowerCase(Locale.ROOT).replace(' ', '_');
    }

    private static String titleCase(String name) {
        String[] parts = normalizedName(name).split("_");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (!builder.isEmpty()) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                builder.append(part.substring(1));
            }
        }
        return builder.toString();
    }

    private static ResourceLocation nativeBlock(String path) {
        return mmr(path);
    }
}
