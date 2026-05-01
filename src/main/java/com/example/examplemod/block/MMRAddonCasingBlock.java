package com.example.examplemod.block;

import com.example.examplemod.registry.ModCasingBlockEntities;
import com.mojang.serialization.MapCodec;
import es.degrassi.mmreborn.common.block.BlockMachineComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MMRAddonCasingBlock extends BlockMachineComponent {
    public static final MapCodec<MMRAddonCasingBlock> CODEC = simpleCodec(MMRAddonCasingBlock::new);

    public MMRAddonCasingBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static BlockBehaviour.Properties defaultProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .strength(2.0F, 10.0F)
                .sound(SoundType.METAL)
                .dynamicShape()
                .noOcclusion();
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        if (adjacentBlockState.getBlock() == state.getBlock()
                && adjacentBlockState.getRenderShape() == state.getRenderShape()) {
            return true;
        }

        return super.skipRendering(state, adjacentBlockState, side);
    }

    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter level, net.minecraft.core.BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModCasingBlockEntities.create(pos, state);
    }
}
