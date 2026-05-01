package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.entity.MMRAddonCasingBlockEntity;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCasingBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ExampleMod.MODID);

    private static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MMRAddonCasingBlockEntity>> CASING =
            BLOCK_ENTITY_TYPES.register("mmr_addon_casing", () -> BlockEntityType.Builder.of(
                    ModCasingBlockEntities::create,
                    ModCasings.all().stream().map(registration -> registration.block().get()).toArray(net.minecraft.world.level.block.Block[]::new)
            ).build(null));

    private ModCasingBlockEntities() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }

    public static MMRAddonCasingBlockEntity create(BlockPos pos, BlockState state) {
        return new MMRAddonCasingBlockEntity(type(), pos, state);
    }

    public static BlockEntityType<MMRAddonCasingBlockEntity> type() {
        return CASING.get();
    }
}
