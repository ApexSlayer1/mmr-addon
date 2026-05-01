package com.example.examplemod.client;

import com.example.examplemod.block.AnimatedMachineBlock;
import com.example.examplemod.block.entity.AnimatedMachineBlockEntity;
import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class AnimatedMachineBlockRenderer extends GeoBlockRenderer<AnimatedMachineBlockEntity> {
    public AnimatedMachineBlockRenderer(BlockEntityType<? extends AnimatedMachineBlockEntity> blockEntityType) {
        super(blockEntityType);
    }

    @Override
    public void preRender(PoseStack poseStack, AnimatedMachineBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int renderColor) {
        poseStack.translate(0.5F, 0.0F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRotation(animatable)));
        poseStack.translate(-0.5F, 0.0F, -0.5F);
        poseStack.translate(
                animatable.definition().renderOffset().x,
                animatable.definition().renderOffset().y,
                animatable.definition().renderOffset().z
        );
        poseStack.scale(
                (float) animatable.definition().renderScale().x,
                (float) animatable.definition().renderScale().y,
                (float) animatable.definition().renderScale().z
        );

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
    }

    @Override
    public boolean shouldRenderOffScreen(AnimatedMachineBlockEntity blockEntity) {
        return true;
    }

    @Override
    public boolean shouldRender(AnimatedMachineBlockEntity blockEntity, Vec3 cameraPos) {
        Vec3 renderCenter = Vec3.atCenterOf(blockEntity.getBlockPos()).add(blockEntity.definition().renderOffset());
        double extraReach = scaledSize(blockEntity).length();
        return renderCenter.closerThan(cameraPos, getViewDistance() + extraReach);
    }

    @Override
    public AABB getRenderBoundingBox(AnimatedMachineBlockEntity blockEntity) {
        return blockEntity.getRenderBoundingBox();
    }

    private static Vec3 scaledSize(AnimatedMachineBlockEntity blockEntity) {
        Vec3 size = blockEntity.definition().renderSize();
        Vec3 scale = blockEntity.definition().renderScale();
        return new Vec3(size.x * scale.x, size.y * scale.y, size.z * scale.z);
    }

    private static float yRotation(AnimatedMachineBlockEntity blockEntity) {
        Direction facing = blockEntity.getBlockState().getValue(AnimatedMachineBlock.FACING);
        return switch (facing) {
            case EAST -> 270.0F;
            case SOUTH -> 180.0F;
            case WEST -> 90.0F;
            default -> 0.0F;
        };
    }
}
