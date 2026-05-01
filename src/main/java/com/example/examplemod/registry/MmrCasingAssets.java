package com.example.examplemod.registry;

import net.minecraft.resources.ResourceLocation;

public final class MmrCasingAssets {
    public static final ResourceLocation EMPTY = mmrBlock("casing_empty");
    public static final ResourceLocation OVERLAY_TRANSPARENT = mmrBlock("overlay_transparent");
    public static final ResourceLocation OVERLAY_REINFORCED = mmrBlock("overlay_reinforced");
    public static final ResourceLocation PLAIN_CENTER = mmrBlock("casing_plain_corners");
    public static final ResourceLocation PLAIN_HORIZONTAL = mmrBlock("casing_plain_horizontal");
    public static final ResourceLocation PLAIN_VERTICAL = mmrBlock("casing_plain_vertical");
    public static final ResourceLocation REINFORCED_CENTER = mmrBlock("casing_reinforced_corners");
    public static final ResourceLocation REINFORCED_HORIZONTAL = mmrBlock("casing_reinforced_horizontal");
    public static final ResourceLocation REINFORCED_PARTICLE = mmrBlock("casing_reinforced");
    public static final ResourceLocation REINFORCED_VERTICAL = mmrBlock("casing_reinforced_vertical");

    private MmrCasingAssets() {
    }

    private static ResourceLocation mmrBlock(String path) {
        return ResourceLocation.fromNamespaceAndPath("modular_machinery_reborn", "block/" + path);
    }
}
