package com.example.examplemod.registry;

import java.util.List;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class MachineBlockDefinitions {
    private MachineBlockDefinitions() {
    }

    // This is the single file you edit when you want to add another machine block.
    // GeckoLib assets should use the same id:
    // assets/<modid>/geo/block/<id>.geo.json
    // assets/<modid>/animations/block/<id>.animation.json
    // assets/<modid>/textures/block/<id>.png
    public static final List<MachineBlockDefinition> ALL = List.of(
            MachineBlockDefinition.builder("example_machine")
                    .displayName("Example Machine")
                    .properties(() -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(4.0F)
                            .sound(SoundType.METAL)
                            .noOcclusion())
                    .itemTexture("item/example_machine")
                    .offset(0.0D, 0.0D, 0.0D)
                    .scale(1.0D)
                    .renderSize(3.0D, 3.0D, 3.0D)
                    .idleAnimation("animation.example_machine.idle")
                    .animationSpeed(1.0D)
                    .build(),

            MachineBlockDefinition.builder("example_offset_machine")
                    .displayName("Example Offset Machine")
                    .properties(() -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(4.0F)
                            .sound(SoundType.METAL)
                            .noOcclusion())
                    .itemTexture("item/example_offset_machine")
                    .offset(0.0D, 0.75D, 0.0D)
                    .scale(1.0D)
                    .renderSize(3.0D, 4.0D, 3.0D)
                    .idleAnimation("animation.example_offset_machine.idle")
                    .animationSpeed(1.0D)
                    .build(),

            MachineBlockDefinition.builder("example_scale_machine")
                    .displayName("Example Scale Machine")
                    .properties(() -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(4.0F)
                            .sound(SoundType.METAL)
                            .noOcclusion())
                    .itemTexture("item/example_scale_machine")
                    .offset(0.0D, 0.0D, 0.0D)
                    .scale(1.75D)
                    .renderSize(2.0D, 2.0D, 2.0D)
                    .idleAnimation("animation.example_scale_machine.idle")
                    .animationSpeed(0.5D)
                    .build(),

            MachineBlockDefinition.builder("example_offset_scale_machine")
                    .displayName("Example Offset Scale Machine")
                    .properties(() -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(4.0F)
                            .sound(SoundType.METAL)
                            .noOcclusion())
                    .itemTexture("item/example_offset_scale_machine")
                    .offset(0.0D, 1.0D, 0.0D)
                    .scale(1.5D, 2.0D, 1.5D)
                    .renderSize(2.0D, 3.0D, 2.0D)
                    .idleAnimation("animation.example_offset_scale_machine.idle")
                    .animationSpeed(1.5D)
                    .build(),

            MachineBlockDefinition.builder("example_test")
                    .displayName("Test")
                    .properties(() -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(4.0F)
                            .sound(SoundType.METAL)
                            .noOcclusion())
                    .itemTexture("item/example_test")
                    .offset(0.0D, 0.0D, 0.0D)
                    .scale(5.D, 5.0D, 5.D)
                    .renderSize(8.0D, 8.0D, 8.0D)
                    .idleAnimation("spin")
                    .animationSpeed(0.2D)
                    .build()

    );
}
