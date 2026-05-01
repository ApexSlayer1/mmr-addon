package com.example.examplemod.registry;

import java.util.List;

public final class MachineBlockDefinitions {
    private MachineBlockDefinitions() {
    }

    // This is the single file you edit when you want to add another machine block.
    // GeckoLib assets should use the same id:
    // assets/<modid>/geo/block/<id>.geo.json
    // assets/<modid>/animations/block/<id>.animation.json
    // assets/<modid>/textures/block/<id>.png
    public static final List<MachineBlockDefinition> ALL = List.of(
            quickMachine("example_machine", "Example Machine", "item/example_machine", 1.0D, "animation.example_machine.idle", 1.0D),

            MachineBlockDefinition.builder("example_offset_machine")
                    .displayName("Example Offset Machine")
                    .itemTexture("item/example_offset_machine")
                    .offset(0.0D, 0.75D, 0.0D)
                    .scale(1.0D)
                    .renderSize(3.0D, 4.0D, 3.0D)
                    .idleAnimation("animation.example_offset_machine.idle")
                    .animationSpeed(1.0D)
                    .build(),

            quickMachine("example_scale_machine", "Example Scale Machine", "item/example_scale_machine", 1.75D, "animation.example_scale_machine.idle", 0.5D),
            

            quickMachine("example_test", "Test", "item/example_test", 5.0D, "spin", 0.2D)

    );

    public static MachineBlockDefinition quickMachine(
            String blockId,
            String displayName,
            String itemTexture,
            double scale,
            String animation,
            double animationSpeed
    ) {
        double renderSize = Math.max(2.0D, scale * 2.0D);
        return MachineBlockDefinition.builder(blockId)
                .displayName(displayName)
                .itemTexture(itemTexture)
                .scale(scale)
                .renderSize(renderSize, renderSize, renderSize)
                .idleAnimation(animation)
                .animationSpeed(animationSpeed)
                .build();
    }
}
