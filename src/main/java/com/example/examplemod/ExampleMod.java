package com.example.examplemod;

import com.example.examplemod.client.AnimatedMachineBlockRenderer;
import com.example.examplemod.data.ModAssetGenerator;
import com.example.examplemod.registry.ModBlockEntities;
import com.example.examplemod.registry.ModCasingBlockEntities;
import com.example.examplemod.registry.ModBlocks;
import com.example.examplemod.registry.ModCasings;
import com.example.examplemod.registry.ModCreativeTabs;
import com.mojang.logging.LogUtils;
import es.degrassi.mmreborn.client.ModularMachineryRebornClient;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

@Mod(ExampleMod.MODID)
public class ExampleMod {
    public static final String MODID = "examplemod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ExampleMod(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCasings.register(modEventBus);
        ModCasingBlockEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
    public static final class DataEvents {
        private DataEvents() {
        }

        @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
            event.getGenerator().addProvider(event.includeClient(), new ModAssetGenerator(
                    event.getGenerator().getPackOutput(),
                    event.getLookupProvider()
            ));
        }
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static final class ClientEvents {
        private ClientEvents() {
        }

        @SubscribeEvent
        public static void registerRenderers(net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers event) {
            ModBlockEntities.all().forEach(entry ->
                    event.registerBlockEntityRenderer(entry.type().get(), context -> new AnimatedMachineBlockRenderer(entry.type().get())));
        }

        @SubscribeEvent
        public static void registerBlockColors(net.neoforged.neoforge.client.event.RegisterColorHandlersEvent.Block event) {
            net.minecraft.world.level.block.Block[] coloredBlocks = ModCasings.coloredBlocks();
            if (coloredBlocks.length > 0) {
                event.register(ModularMachineryRebornClient::blockColor, coloredBlocks);
            }
        }

        @SubscribeEvent
        public static void registerItemColors(net.neoforged.neoforge.client.event.RegisterColorHandlersEvent.Item event) {
            net.minecraft.world.item.Item[] coloredItems = ModCasings.coloredItems();
            if (coloredItems.length > 0) {
                event.register(ModularMachineryRebornClient::itemColor, coloredItems);
            }
        }
    }
}
