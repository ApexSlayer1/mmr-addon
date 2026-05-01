package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.AnimatedMachineBlock;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ExampleMod.MODID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExampleMod.MODID);
    private static final List<RegisteredMachineBlock> REGISTRATIONS = MachineBlockDefinitions.ALL.stream()
            .map(ModBlocks::registerMachineBlock)
            .toList();
    private static final Map<Block, RegisteredMachineBlock> REGISTRATIONS_BY_BLOCK = new IdentityHashMap<>();

    private ModBlocks() {
    }

    private static RegisteredMachineBlock registerMachineBlock(MachineBlockDefinition definition) {
        DeferredBlock<AnimatedMachineBlock> block = BLOCKS.register(definition.id(), () -> new AnimatedMachineBlock(definition.createProperties()));
        DeferredItem<BlockItem> item = ITEMS.register(definition.id(), () -> new BlockItem(block.get(), new Item.Properties()));
        return new RegisteredMachineBlock(definition, block, item);
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }

    public static List<RegisteredMachineBlock> all() {
        return REGISTRATIONS;
    }

    public static MachineBlockDefinition definitionFor(Block block) {
        return registrationFor(block).definition();
    }

    public static RegisteredMachineBlock registrationFor(Block block) {
        RegisteredMachineBlock registration = REGISTRATIONS_BY_BLOCK.computeIfAbsent(block, key ->
                REGISTRATIONS.stream()
                        .filter(candidate -> candidate.block().get() == key)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("No machine block registered for " + Objects.toString(key))));

        return registration;
    }

    public record RegisteredMachineBlock(
            MachineBlockDefinition definition,
            DeferredBlock<AnimatedMachineBlock> block,
            DeferredItem<BlockItem> item
    ) {
    }
}
