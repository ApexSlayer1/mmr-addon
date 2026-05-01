package com.example.examplemod.data;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.registry.ModBlocks;
import com.example.examplemod.registry.ModCasings;
import com.example.examplemod.registry.GeneratedTexture;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

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
                writeCasingBlockStates(cachedOutput),
                writeBlockModels(cachedOutput),
                writeItemModels(cachedOutput),
                writeGeneratedTextures(cachedOutput),
                writeLootTables(cachedOutput),
                writeBlockTags(cachedOutput),
                writeItemTags(cachedOutput)
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
        ModCasings.all().forEach(registration ->
                entries.put(registration.block().get().getDescriptionId(), registration.definition().displayName()));
        entries.put("itemGroup." + ExampleMod.MODID + ".machine_blocks", "Machine Blocks");
        entries.put("itemGroup." + ExampleMod.MODID + ".casings", "Casings");

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

    private CompletableFuture<?> writeCasingBlockStates(CachedOutput cachedOutput) {
        return CompletableFuture.allOf(ModCasings.all().stream()
                .map(registration -> {
                    JsonObject root = new JsonObject();
                    root.addProperty("athena:loader", "modular_machinery_reborn:casing");

                    JsonObject connectTo = new JsonObject();
                    connectTo.addProperty("type", "tag");
                    connectTo.addProperty("tag", registration.definition().connectionTag().toString());
                    root.add("connect_to", connectTo);

                    JsonObject ctmTextures = new JsonObject();
                    registration.definition().ctmTextures().forEach((key, value) -> ctmTextures.addProperty(key, value.toString()));
                    root.add("ctm_textures", ctmTextures);

                    JsonObject variants = new JsonObject();
                    JsonObject variant = new JsonObject();
                    variant.addProperty("model", "minecraft:block/air");
                    variants.add("", variant);
                    root.add("variants", variants);

                    Path outputPath = packOutput.getOutputFolder().resolve("assets/" + ExampleMod.MODID + "/blockstates/" + registration.definition().id() + ".json");
                    return DataProvider.saveStable(cachedOutput, GSON.toJsonTree(root), outputPath);
                })
                .toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> writeBlockModels(CachedOutput cachedOutput) {
        List<CompletableFuture<?>> writes = new ArrayList<>();

        ModBlocks.all().forEach(registration -> {
            JsonObject textures = new JsonObject();
            textures.addProperty("all", registration.definition().itemTexture().toString());

            JsonObject root = new JsonObject();
            root.addProperty("parent", "minecraft:block/cube_all");
            root.add("textures", textures);

            Path outputPath = packOutput.getOutputFolder().resolve("assets/" + ExampleMod.MODID + "/models/block/" + registration.definition().id() + ".json");
            writes.add(DataProvider.saveStable(cachedOutput, GSON.toJsonTree(root), outputPath));
        });

        ModCasings.all().forEach(registration -> {
            JsonObject textures = new JsonObject();
            registration.definition().blockModelTextures().forEach((key, value) -> textures.addProperty(key, value.toString()));

            JsonObject root = new JsonObject();
            root.addProperty("parent", "modular_machinery_reborn:block/blockmodel_overlay_all");
            root.add("textures", textures);

            Path outputPath = packOutput.getOutputFolder().resolve("assets/" + ExampleMod.MODID + "/models/block/" + registration.definition().id() + ".json");
            writes.add(DataProvider.saveStable(cachedOutput, GSON.toJsonTree(root), outputPath));
        });

        return CompletableFuture.allOf(writes.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> writeItemModels(CachedOutput cachedOutput) {
        List<CompletableFuture<?>> writes = new ArrayList<>();

        ModBlocks.all().forEach(registration -> {
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", registration.definition().itemTexture().toString());

            JsonObject root = new JsonObject();
            root.addProperty("parent", "minecraft:item/generated");
            root.add("textures", textures);

            Path outputPath = packOutput.getOutputFolder().resolve("assets/" + ExampleMod.MODID + "/models/item/" + registration.definition().id() + ".json");
            writes.add(DataProvider.saveStable(cachedOutput, GSON.toJsonTree(root), outputPath));
        });

        ModCasings.all().forEach(registration -> {
            JsonObject root = new JsonObject();
            root.addProperty("parent", ExampleMod.MODID + ":block/" + registration.definition().id());

            Path outputPath = packOutput.getOutputFolder().resolve("assets/" + ExampleMod.MODID + "/models/item/" + registration.definition().id() + ".json");
            writes.add(DataProvider.saveStable(cachedOutput, GSON.toJsonTree(root), outputPath));
        });

        return CompletableFuture.allOf(writes.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> writeGeneratedTextures(CachedOutput cachedOutput) {
        return CompletableFuture.allOf(ModCasings.all().stream()
                .flatMap(registration -> registration.definition().generatedTextures().stream())
                .distinct()
                .map(texture -> CompletableFuture.runAsync(() -> writeGeneratedTexture(cachedOutput, texture)))
                .toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> writeLootTables(CachedOutput cachedOutput) {
        List<CompletableFuture<?>> writes = new ArrayList<>();
        ModBlocks.all().forEach(registration ->
                writes.add(writeSimpleBlockLoot(cachedOutput, registration.definition().id())));
        ModCasings.all().forEach(registration ->
                writes.add(writeSimpleBlockLoot(cachedOutput, registration.definition().id())));
        return CompletableFuture.allOf(writes.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> writeBlockTags(CachedOutput cachedOutput) {
        Map<ResourceLocation, List<String>> tags = new LinkedHashMap<>();
        tags.put(tag("modular_machinery_reborn", "casing"), new ArrayList<>());
        tags.put(tag("modular_machinery_reborn", "all_casing"), new ArrayList<>());

        ModCasings.all().forEach(registration -> {
            String blockId = ExampleMod.MODID + ":" + registration.definition().id();
            tags.get(tag("modular_machinery_reborn", "casing")).add(blockId);
            tags.get(tag("modular_machinery_reborn", "all_casing")).add(blockId);
            List<String> values = tags.computeIfAbsent(registration.definition().connectionTag(), key -> new ArrayList<>());
            registration.definition().nativeConnections().forEach(nativeBlock -> {
                String nativeId = nativeBlock.toString();
                if (!values.contains(nativeId)) {
                    values.add(nativeId);
                }
            });
            if (!values.contains(blockId)) {
                values.add(blockId);
            }
        });

        return CompletableFuture.allOf(tags.entrySet().stream()
                .map(entry -> writeTag(cachedOutput, "block", entry.getKey(), entry.getValue()))
                .toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> writeItemTags(CachedOutput cachedOutput) {
        Map<ResourceLocation, List<String>> tags = new LinkedHashMap<>();
        tags.put(tag("modular_machinery_reborn", "casing"), new ArrayList<>());
        tags.put(tag("modular_machinery_reborn", "all_casing"), new ArrayList<>());

        ModCasings.all().forEach(registration -> {
            String itemId = ExampleMod.MODID + ":" + registration.definition().id();
            tags.get(tag("modular_machinery_reborn", "casing")).add(itemId);
            tags.get(tag("modular_machinery_reborn", "all_casing")).add(itemId);
        });

        return CompletableFuture.allOf(tags.entrySet().stream()
                .map(entry -> writeTag(cachedOutput, "item", entry.getKey(), entry.getValue()))
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

    private CompletableFuture<?> writeSimpleBlockLoot(CachedOutput cachedOutput, String id) {
        JsonObject root = new JsonObject();
        root.addProperty("type", "minecraft:block");

        JsonArray pools = new JsonArray();
        JsonObject pool = new JsonObject();
        pool.addProperty("rolls", 1);

        JsonArray entries = new JsonArray();
        JsonObject entry = new JsonObject();
        entry.addProperty("type", "minecraft:item");
        entry.addProperty("name", ExampleMod.MODID + ":" + id);
        entries.add(entry);

        pool.add("entries", entries);
        pools.add(pool);
        root.add("pools", pools);

        Path outputPath = packOutput.getOutputFolder().resolve("data/" + ExampleMod.MODID + "/loot_table/blocks/" + id + ".json");
        return DataProvider.saveStable(cachedOutput, GSON.toJsonTree(root), outputPath);
    }

    private CompletableFuture<?> writeTag(CachedOutput cachedOutput, String kind, ResourceLocation tagId, List<String> values) {
        JsonObject root = new JsonObject();
        root.addProperty("replace", false);

        JsonArray jsonValues = new JsonArray();
        values.forEach(jsonValues::add);
        root.add("values", jsonValues);

        Path outputPath = packOutput.getOutputFolder().resolve("data/" + tagId.getNamespace() + "/tags/" + kind + "/" + tagId.getPath() + ".json");
        return DataProvider.saveStable(cachedOutput, GSON.toJsonTree(root), outputPath);
    }

    private static ResourceLocation tag(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    private void writeGeneratedTexture(CachedOutput cachedOutput, GeneratedTexture texture) {
        try {
            BufferedImage combined = composeTexture(texture.layers());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(combined, "png", output);
            byte[] bytes = output.toByteArray();

            Path outputPath = packOutput.getOutputFolder()
                    .resolve("assets/" + texture.output().getNamespace() + "/textures/" + texture.output().getPath() + ".png");
            cachedOutput.writeIfNeeded(outputPath, bytes, Hashing.sha1().hashBytes(bytes));
        } catch (IOException exception) {
            throw new RuntimeException("Failed to write generated texture " + texture.output(), exception);
        }
    }

    private BufferedImage composeTexture(List<ResourceLocation> layers) throws IOException {
        BufferedImage base = null;
        Graphics2D graphics = null;

        try {
            for (ResourceLocation layer : layers) {
                BufferedImage image = readTexture(layer);
                if (base == null) {
                    base = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    graphics = base.createGraphics();
                }
                graphics.drawImage(image, 0, 0, null);
            }
        } finally {
            if (graphics != null) {
                graphics.dispose();
            }
        }

        if (base == null) {
            throw new IOException("Tried to compose a texture without layers");
        }

        return base;
    }

    private BufferedImage readTexture(ResourceLocation texture) throws IOException {
        String resourcePath = "assets/" + texture.getNamespace() + "/textures/" + texture.getPath() + ".png";
        try (InputStream stream = ModAssetGenerator.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (stream == null) {
                throw new IOException("Missing texture resource " + resourcePath);
            }

            BufferedImage image = ImageIO.read(stream);
            if (image == null) {
                throw new IOException("Unable to decode texture resource " + resourcePath);
            }
            return image;
        }
    }
}
