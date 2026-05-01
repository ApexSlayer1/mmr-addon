package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.entity.AnimatedMachineBlockEntity;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ExampleMod.MODID);
    private static final List<RegisteredMachineBlockEntity> REGISTRATIONS = ModBlocks.all().stream()
            .map(ModBlockEntities::registerType)
            .toList();
    private static final Map<String, RegisteredMachineBlockEntity> REGISTRATIONS_BY_ID = REGISTRATIONS.stream()
            .collect(java.util.stream.Collectors.toMap(entry -> entry.definition().id(), entry -> entry));

    private ModBlockEntities() {
    }

    private static RegisteredMachineBlockEntity registerType(ModBlocks.RegisteredMachineBlock registration) {
        DeferredHolder<BlockEntityType<?>, BlockEntityType<AnimatedMachineBlockEntity>> type = BLOCK_ENTITY_TYPES.register(
                registration.definition().id(),
                () -> BlockEntityType.Builder.of(
                        (pos, state) -> new AnimatedMachineBlockEntity(typeFor(registration.definition().id()), pos, state, registration.definition()),
                        registration.block().get()
                ).build(null)
        );

        return new RegisteredMachineBlockEntity(registration.definition(), type);
    }

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }

    public static List<RegisteredMachineBlockEntity> all() {
        return REGISTRATIONS;
    }

    public static AnimatedMachineBlockEntity createFor(Block block, BlockPos pos, BlockState state) {
        MachineBlockDefinition definition = ModBlocks.definitionFor(block);
        return new AnimatedMachineBlockEntity(typeFor(definition.id()), pos, state, definition);
    }

    public static BlockEntityType<AnimatedMachineBlockEntity> typeFor(String id) {
        return REGISTRATIONS_BY_ID.get(id).type().get();
    }

    public record RegisteredMachineBlockEntity(
            MachineBlockDefinition definition,
            DeferredHolder<BlockEntityType<?>, BlockEntityType<AnimatedMachineBlockEntity>> type
    ) {
    }
}
