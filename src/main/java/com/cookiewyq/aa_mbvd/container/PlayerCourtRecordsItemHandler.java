package com.cookiewyq.aa_mbvd.container;

import com.cookiewyq.aa_mbvd.capability.IGetPlayerCourtRecordsTileEntityData;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;


public class PlayerCourtRecordsItemHandler implements IItemHandler, IItemHandlerModifiable {
    private final IGetPlayerCourtRecordsTileEntityData data;

    public PlayerCourtRecordsItemHandler(IGetPlayerCourtRecordsTileEntityData data) {
        this.data = data;
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        if (slot >= 0 && slot < 54) {
            return data.getCourtRecordsItems()[slot];
        }
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slot >= 0 && slot < 54) {
            ItemStack existing = data.getCourtRecordsItems()[slot];

            // 如果槽位为空，直接放入
            if (existing.isEmpty()) {
                if (!simulate) {
                    data.getCourtRecordsItems()[slot] = stack.copy();
                }
                return ItemStack.EMPTY;
            }

            // 如果槽位已有相同物品，尝试合并
            if (existing.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(stack, existing)) {
                int limit = Math.min(stack.getMaxStackSize(), getSlotLimit(slot));

                if (existing.getCount() + stack.getCount() <= limit) {
                    if (!simulate) {
                        existing.grow(stack.getCount());
                    }
                    return ItemStack.EMPTY;
                }
            }
        }
        return stack;
    }


    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot >= 0 && slot < 54) {
            ItemStack existing = data.getCourtRecordsItems()[slot];
            if (!existing.isEmpty()) {
                int limit = Math.min(existing.getCount(), amount);
                ItemStack extracted = existing.copy();
                extracted.setCount(limit);

                if (!simulate) {
                    existing.shrink(limit);
                    if (existing.isEmpty()) {
                        data.getCourtRecordsItems()[slot] = ItemStack.EMPTY;
                    }
                }
                return extracted;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (slot >= 0 && slot < 54) {
            data.getCourtRecordsItems()[slot] = stack.copy();
        }
    }
}
