package com.cookiewyq.aa_mbvd.items.custom.badges;

import com.cookiewyq.aa_mbvd.entities.custom.ModThrowableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public interface IModThrowableItem{

    boolean isRemoveOnImpact(ItemStack itemStack, ModThrowableEntity modThrowableEntity);

    void onEntityHit(EntityRayTraceResult rayTraceResult, World world, PlayerEntity player, ItemStack itemStack, ModThrowableEntity modThrowableEntity);

    void onImpact(RayTraceResult rayTraceResult, World world, PlayerEntity player, ItemStack itemStack, ModThrowableEntity modThrowableEntity);

    default boolean getIsShiftPressedWhenThrow() {
        return false;
    }

    default void playSound(World world, PlayerEntity player) {
    }

    default void tick(World world, PlayerEntity player, ItemStack itemStack, ModThrowableEntity modThrowableEntity) {
    }

    default int getCoolDownTicks(ItemStack itemStack, PlayerEntity player) {
        return -1;
    }
}
