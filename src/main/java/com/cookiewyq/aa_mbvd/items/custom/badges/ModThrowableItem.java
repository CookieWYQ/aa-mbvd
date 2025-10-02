package com.cookiewyq.aa_mbvd.items.custom.badges;

import com.cookiewyq.aa_mbvd.entities.custom.ModThrowableEntity;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class ModThrowableItem extends Item implements IModThrowableItem {

    public ModThrowableItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        if (this.getIsShiftPressedWhenThrow() && !Screen.hasShiftDown()) return ActionResult.func_233538_a_(itemStack, false);


        playSound(world, player);
        if (!world.isRemote) {
            ModThrowableEntity modThrowableEntity = getBadgeEntity(world, player, itemStack);
            modThrowableEntity.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
            world.addEntity(modThrowableEntity);
        }

        player.addStat(Stats.ITEM_USED.get(this));

        if (!player.abilities.isCreativeMode) {
            itemStack.shrink(1);
        }

        int coolDownTicks = getCoolDownTicks(itemStack, player);
        if (coolDownTicks != -1) {
            // 设置冷却时间
            player.getCooldownTracker().setCooldown(this, coolDownTicks);
        }

        return ActionResult.func_233538_a_(itemStack, world.isRemote());
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> components, ITooltipFlag iTooltipFlag) {
        if (this.getIsShiftPressedWhenThrow()){
            components.add(new TranslationTextComponent("tooltip.need_shift_tip").mergeStyle(TextFormatting.UNDERLINE).mergeStyle(TextFormatting.YELLOW));
        }
        super.addInformation(itemStack, world, components, iTooltipFlag);
    }

    private ModThrowableEntity getBadgeEntity(World world, PlayerEntity player, ItemStack itemStack) {
        ModThrowableEntity modThrowableEntity = new ModThrowableEntity(world, player) {
            @Override
            protected void onEntityHit(EntityRayTraceResult rayTraceResult) {
                super.onEntityHit(rayTraceResult);
                ModThrowableItem.this.onEntityHit(rayTraceResult, world, player, itemStack, this);
            }

            @Override
            protected void onImpact(RayTraceResult rayTraceResult) {
                super.callSupperOnImpact(rayTraceResult);
                if (!this.world.isRemote) {
                    this.world.setEntityState(this, (byte) 3);
                    ModThrowableItem.this.onImpact(rayTraceResult, world, player, itemStack, this);
                    if (ModThrowableItem.this.isRemoveOnImpact(itemStack, this)) {
                        this.remove();
                    }
                }
            }

            @Override
            public void tick() {
                super.tick();
                ModThrowableItem.this.tick(world, player, itemStack, this);
            }

            @Override
            public ItemStack getItem() {
                return itemStack;
            }
        };
        modThrowableEntity.setItem(itemStack);
        return modThrowableEntity;
    }
}
