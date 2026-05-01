package com.example.examplemod.data;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.registry.ModBlocks;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.LootContext;

public final class ModAssetGenerator implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final PackOutput packOutput;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    public ModAssetGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.packOutput = packOutput;
        this.lookupProvider = lookupProvider;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        return lookupProvider.thenCompose(ignored -> CompletableFuture.allOf(
                writeLang(cachedOutput),
                writeBlockStates(cachedOutput),
                writeBlockModels(cachedOutput),
                writeItemModels(cachedOutput),
                writeLootTables(cachedOutput)
        ));
    }

    @Override
    public String getName() {
        return "Example Mod Asset Generator";
    }

    private CompletableFuture<?> writeLang(CachedOutput cachedOutput) {
        Map<String, String> entries = new TreeMap<>();
        ModBlocks.all().forEach(registration ->
                entries.put(registration.block().get().getDescriptionId(), registration.definition().displayName()));

        JsonObject json = new JsonObject();
        entries.forEach(json::addProperty);

        Path outputPath = packOutput.getOutputFolder().resolve("assets/" + ExampleMod.MODID + "/lang/en_us.json");
        return DataProvider.saveStable(cachedOutput, GSON.toJsonTree(json), outputPath);
    }

    private CompletableFuture<?> writeBlockStates(CachedOutput cachedOutput) {
        return CompletableFuture.allOf(ModBlocks.all().stream()
                .map(registration -> {
                    JsonObject variants = new JsonObject();
                    variants.add("facing=north", variantModel(registration.definition().id(), 0));
                    variants.add("facing=east", variantModel(registration.definition().id(), 90));
                    variants.add("facing=south", variantModel(registration.definition().id(), 180));
                    variants.add("facing=west", variantModel(registration.definition().id(), 270));

                    JsonObject root = new JsonObject();
                    root.add("variants", variants);

                    Path outputPath = packOutput.getOutputFolder().resolve("assets/" + ExampleMod.MODID + "/blockstates/" + registration.definition().id() + ".json");
                    return DataProvider.saveStable(cachedOutput, GSON.toJsonTree(root), outputPath);
                })
                .toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> writeBlockModels(CachedOutput cachedOutput) {
        return CompletableFuture.allOf(ModBlocks.all().stream()
                .map(registration -> {
                    JsonObject textures = new JsonObject();
                    textures.addProperty("all", registration.definition().itemTexture().toString());

                    JsonObject root = new JsonObject();
                    root.addProperty("parent", "minecraft:block/cube_all");
                    root.add("textures", textures);

                    Path outputPath = packOutput.getOutputFolder().resolve("assets/" + ExampleMod.MODID + "/models/block/" + registration.definition().id() + ".json");
                    return DataProvider.saveStable(cachedOutput, GSON.toJsonTree(root), outputPath);
                })
                .toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> writeItemModels(CachedOutput cachedOutput) {
        return CompletableFuture.allOf(ModBlocks.all().stream()
                .map(registration -> {
                    JsonObject textures = new JsonObject();
                    textures.addProperty("layer0", registration.definition().itemTexture().toString());

                    JsonObject root = new JsonObject();
                    root.addProperty("parent", "minecraft:item/generated");
                    root.add("textures", textures);

                    Path outputPath = packOutput.getOutputFolder().resolve("assets/" + ExampleMod.MODID + "/models/item/" + registration.definition().id() + ".json");
                    return DataProvider.saveStable(cachedOutput, GSON.toJsonTree(root), outputPath);
                })
                .toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> writeLootTables(CachedOutput cachedOutput) {
        return CompletableFuture.allOf(ModBlocks.all().stream()
                .map(registration -> {
                    JsonObject root = new JsonObject();
                    root.addProperty("type", "minecraft:block");

                    JsonArray pools = new JsonArray();
                    JsonObject pool = new JsonObject();
                    pool.addProperty("rolls", 1);

                    JsonArray entries = new JsonArray();
                    JsonObject entry = new JsonObject();
                    entry.addProperty("type", "minecraft:item");
                    entry.addProperty("name", ExampleMod.MODID + ":" + registration.definition().id());
                    entries.add(entry);

                    pool.add("entries", entries);
                    pools.add(pool);
                    root.add("pools", pools);

                    Path outputPath = packOutput.getOutputFolder().resolve("data/" + ExampleMod.MODID + "/loot_table/blocks/" + registration.definition().id() + ".json");
                    return DataProvider.saveStable(cachedOutput, GSON.toJsonTree(root), outputPath);
                })
                .toArray(CompletableFuture[]::new));
    }

    private JsonObject variantModel(String id, int yRotation) {
        JsonObject model = new JsonObject();
        model.addProperty("model", ExampleMod.MODID + ":block/" + id);

        if (yRotation != 0) {
            model.addProperty("y", yRotation);
        }

        return model;
    }
}
