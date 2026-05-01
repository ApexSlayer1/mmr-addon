package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExampleMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MACHINE_BLOCKS = CREATIVE_MODE_TABS.register(
            "machine_blocks",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + ExampleMod.MODID + ".machine_blocks"))
                    .icon(() -> new ItemStack(ModBlocks.all().getFirst().item().get()))
                    .displayItems((parameters, output) ->
                            ModBlocks.all().forEach(registration -> output.accept(registration.item().get())))
                    .build()
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CASINGS = CREATIVE_MODE_TABS.register(
            "casings",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + ExampleMod.MODID + ".casings"))
                    .icon(() -> new ItemStack(ModCasings.all().getFirst().item().get()))
                    .displayItems((parameters, output) ->
                            ModCasings.all().forEach(registration -> output.accept(registration.item().get())))
                    .build()
    );

    private ModCreativeTabs() {
    }

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
