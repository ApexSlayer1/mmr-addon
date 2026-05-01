package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;

public final class MachineBlockDefinition {
    private final String id;
    private final String displayName;
    private final Supplier<BlockBehaviour.Properties> propertiesFactory;
    private final ResourceLocation itemTexture;
    private final Vec3 renderOffset;
    private final Vec3 renderScale;
    private final Vec3 renderSize;
    private final String idleAnimation;
    private final double animationSpeed;

    private MachineBlockDefinition(Builder builder) {
        this.id = builder.id;
        this.displayName = builder.displayName;
        this.propertiesFactory = builder.propertiesFactory;
        this.itemTexture = builder.itemTexture;
        this.renderOffset = builder.renderOffset;
        this.renderScale = builder.renderScale;
        this.renderSize = builder.renderSize;
        this.idleAnimation = builder.idleAnimation;
        this.animationSpeed = builder.animationSpeed;
    }

    public String id() {
        return id;
    }

    public String displayName() {
        return displayName;
    }

    public BlockBehaviour.Properties createProperties() {
        return propertiesFactory.get();
    }

    public ResourceLocation itemTexture() {
        return itemTexture;
    }

    public Vec3 renderOffset() {
        return renderOffset;
    }

    public Vec3 renderScale() {
        return renderScale;
    }

    public Vec3 renderSize() {
        return renderSize;
    }

    public String idleAnimation() {
        return idleAnimation;
    }

    public double animationSpeed() {
        return animationSpeed;
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static final class Builder {
        private final String id;
        private String displayName;
        private Supplier<BlockBehaviour.Properties> propertiesFactory;
        private ResourceLocation itemTexture;
        private Vec3 renderOffset = Vec3.ZERO;
        private Vec3 renderScale = new Vec3(1.0D, 1.0D, 1.0D);
        private Vec3 renderSize = new Vec3(1.0D, 1.0D, 1.0D);
        private String idleAnimation = "";
        private double animationSpeed = 1.0D;

        private Builder(String id) {
            this.id = Objects.requireNonNull(id, "id");
            this.displayName = humanize(id);
            this.propertiesFactory = () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5F)
                    .sound(SoundType.METAL)
                    .noOcclusion();
            this.itemTexture = ExampleMod.id("item/" + id);
        }

        public Builder displayName(String displayName) {
            this.displayName = Objects.requireNonNull(displayName, "displayName");
            return this;
        }

        public Builder properties(Supplier<BlockBehaviour.Properties> propertiesFactory) {
            this.propertiesFactory = Objects.requireNonNull(propertiesFactory, "propertiesFactory");
            return this;
        }

        public Builder itemTexture(String path) {
            this.itemTexture = ExampleMod.id(path);
            return this;
        }

        public Builder offset(double x, double y, double z) {
            this.renderOffset = new Vec3(x, y, z);
            return this;
        }

        public Builder scale(double uniformScale) {
            return scale(uniformScale, uniformScale, uniformScale);
        }

        public Builder scale(double x, double y, double z) {
            this.renderScale = new Vec3(x, y, z);
            return this;
        }

        public Builder renderSize(double x, double y, double z) {
            this.renderSize = new Vec3(x, y, z);
            return this;
        }

        public Builder idleAnimation(String animationName) {
            this.idleAnimation = Objects.requireNonNull(animationName, "animationName");
            return this;
        }

        public Builder animationSpeed(double animationSpeed) {
            this.animationSpeed = animationSpeed;
            return this;
        }

        public MachineBlockDefinition build() {
            return new MachineBlockDefinition(this);
        }

        private static String humanize(String id) {
            String[] parts = id.split("_");
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < parts.length; i++) {
                if (i > 0) {
                    builder.append(' ');
                }

                String part = parts[i].toLowerCase(Locale.ROOT);
                builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
            }

            return builder.toString();
        }
    }
}
