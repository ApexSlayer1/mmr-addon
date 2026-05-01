package com.example.examplemod.block.entity;

import es.degrassi.mmreborn.common.entity.base.ColorableMachineComponentEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MMRAddonCasingBlockEntity extends ColorableMachineComponentEntity {
    public MMRAddonCasingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
