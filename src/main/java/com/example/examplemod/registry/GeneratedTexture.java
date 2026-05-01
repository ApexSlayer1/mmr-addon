package com.example.examplemod.registry;

import java.util.List;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;

public record GeneratedTexture(ResourceLocation output, List<ResourceLocation> layers) {
    public GeneratedTexture {
        Objects.requireNonNull(output, "output");
        layers = List.copyOf(layers);
    }
}
