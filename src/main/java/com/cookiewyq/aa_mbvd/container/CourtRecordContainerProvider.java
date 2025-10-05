package com.cookiewyq.aa_mbvd.container;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CourtRecordContainerProvider implements INamedContainerProvider {
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.court_record");
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        IItemHandlerModifiable courtRecordInventory = player.getCapability(Capabilities.COURT_RECORD_INVENTORY_CAPABILITY)
                .orElseThrow(() -> new IllegalStateException("Court record capability not found"));
        return new CourtRecordContainer(windowId, playerInventory, courtRecordInventory);
    }

    private IItemHandlerModifiable getCourtRecordInventory(PlayerEntity player) {
        // 从玩家的能力中获取法庭记录库存
        return player.getCapability(Capabilities.COURT_RECORD_INVENTORY_CAPABILITY)
                .orElseThrow(() -> new IllegalStateException("Court record capability not found"));
    }
}
