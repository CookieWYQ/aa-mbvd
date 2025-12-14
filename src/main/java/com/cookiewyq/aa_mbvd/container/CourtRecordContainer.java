package com.cookiewyq.aa_mbvd.container;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.configs.ModConfigs;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.atomic.AtomicBoolean;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault

public class CourtRecordContainer extends Container {

    private final int capSize;
    private final PlayerInventory playerInventory;

    public CourtRecordContainer(int id, PlayerInventory inv) {
        super(ModContainerTypes.COURTRECORDS_CONTAINER.get(), id);
        this.capSize = 4 * 9;
        this.playerInventory = inv;

        inv.player.getCapability(Capabilities.COURT_RECORD_CAPABILITY).ifPresent(cap -> {
            for (int i = 0; i < capSize; i++) {
                addSlot(new CourtRecordSlot(
                        cap.getInventory(),
                        i,
                        8 + (i % 9) * 18,
                        18 + (i / 9) * 18
                ));
            }
        });

        layoutPlayerInventory(inv, 8, 18 + ((capSize + 8) / 9) * 18 + 10);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity p, int index) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot == null || !slot.getHasStack()) return empty;

        ItemStack stack = slot.getStack();
        ItemStack copy = stack.copy();

        if (index < capSize) {
            if (!mergeItemStack(stack, capSize, inventorySlots.size(), true))
                return empty;
        } else {
            if (!mergeItemStack(stack, 0, capSize, false))
                return empty;
        }

        if (stack.isEmpty()) slot.putStack(ItemStack.EMPTY);
        else slot.onSlotChanged();

        return copy;
    }

    public boolean isAttorneysBadge() {
        AtomicBoolean isAttorneysBadge = new AtomicBoolean(false);
        playerInventory.player.getCapability(Capabilities.COURT_RECORD_CAPABILITY).ifPresent(cap ->
                isAttorneysBadge.set(cap.isAttorneysBadge()));
        return isAttorneysBadge.get();
    }

    private void layoutPlayerInventory(PlayerInventory inv, int x, int y) {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 9; c++)
                addSlot(new Slot(inv, c + r * 9 + 9, x + c * 18, y + r * 18));

        for (int c = 0; c < 9; c++)
            addSlot(new Slot(inv, c, x + c * 18, y + 58));
    }

    @Override
    public boolean canInteractWith(PlayerEntity p) {
        return true;
    }
}
