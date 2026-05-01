package com.example.examplemod.block.entity;

import com.example.examplemod.registry.MachineBlockDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AnimatedMachineBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final MachineBlockDefinition definition;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AnimatedMachineBlockEntity(BlockEntityType<? extends AnimatedMachineBlockEntity> type, BlockPos pos, BlockState blockState, MachineBlockDefinition definition) {
        super(type, pos, blockState);
        this.definition = definition;
    }

    public MachineBlockDefinition definition() {
        return definition;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", animationState -> {
            if (definition.idleAnimation().isBlank()) {
                return PlayState.STOP;
            }

            return animationState.setAndContinue(RawAnimation.begin().thenLoop(definition.idleAnimation()));
        }).setAnimationSpeed(definition.animationSpeed()));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public AABB getRenderBoundingBox() {
        Vec3 scale = definition.renderScale();
        Vec3 baseSize = definition.renderSize();
        Vec3 size = new Vec3(baseSize.x * scale.x, baseSize.y * scale.y, baseSize.z * scale.z);
        Vec3 center = Vec3.atCenterOf(worldPosition).add(definition.renderOffset());
        double halfX = size.x * 0.5D;
        double halfY = size.y * 0.5D;
        double halfZ = size.z * 0.5D;

        return new AABB(
                center.x - halfX,
                center.y - halfY,
                center.z - halfZ,
                center.x + halfX,
                center.y + halfY,
                center.z + halfZ
        );
    }
}
