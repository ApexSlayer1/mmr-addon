package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.MMRAddonCasingBlock;
import es.degrassi.mmreborn.common.item.ItemBlockMachineComponent;
import java.util.List;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCasings {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ExampleMod.MODID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExampleMod.MODID);
    private static final List<RegisteredCasing> REGISTRATIONS = CasingDefinitions.ALL.stream()
            .map(ModCasings::registerCasing)
            .toList();

    private ModCasings() {
    }

    private static RegisteredCasing registerCasing(CasingDefinition definition) {
        DeferredBlock<MMRAddonCasingBlock> block = BLOCKS.register(definition.id(), () -> new MMRAddonCasingBlock(MMRAddonCasingBlock.defaultProperties()));
        DeferredItem<ItemBlockMachineComponent> item = ITEMS.register(
                definition.id(),
                () -> new ItemBlockMachineComponent(block.get(), new net.minecraft.world.item.Item.Properties())
        );
        return new RegisteredCasing(definition, block, item);
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }

    public static List<RegisteredCasing> all() {
        return REGISTRATIONS;
    }

    public static Block[] coloredBlocks() {
        return REGISTRATIONS.stream()
                .filter(registration -> registration.definition().colored())
                .map(registration -> registration.block().get())
                .toArray(Block[]::new);
    }

    public static Item[] coloredItems() {
        return REGISTRATIONS.stream()
                .filter(registration -> registration.definition().colored())
                .map(registration -> registration.item().get())
                .toArray(Item[]::new);
    }

    public record RegisteredCasing(
            CasingDefinition definition,
            DeferredBlock<MMRAddonCasingBlock> block,
            DeferredItem<ItemBlockMachineComponent> item
    ) {
    }
}
