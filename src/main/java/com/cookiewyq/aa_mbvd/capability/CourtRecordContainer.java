package com.cookiewyq.aa_mbvd.capability;

import com.cookiewyq.aa_mbvd.configs.ModConfigs;
import com.cookiewyq.aa_mbvd.container.ModContainerTypes;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CourtRecordContainer extends Container {

    private final IItemHandlerModifiable courtRecordInventory;
    private final int courtRecordRows;

    public CourtRecordContainer(int windowId, PlayerInventory playerInventory, IItemHandlerModifiable courtRecordInventory) {
        super(ModContainerTypes.COURTRECORDS_CONTAINER.get(), windowId);
        this.courtRecordInventory = courtRecordInventory;
        this.courtRecordRows = ModConfigs.COURT_RECORD_ROWS.get();

        // 添加法庭记录槽位（动态行数×9列，索引从36开始）
        for (int i = 0; i < courtRecordRows; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new CourtRecordSlot(courtRecordInventory, j + i * 9, 8 + j * 18, - 2 + i * 18));
            }
        }

        // 添加玩家物品栏 (固定36个槽位，索引0-35)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, - 1 + courtRecordRows * 18 + i * 18 + 11));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, courtRecordRows * 18 + 3 * 18 + 10 + 7));
        }
    }



    // 处理Shift+点击物品转移
    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int courtRecordStartIndex = 36; // 玩家物品栏槽位索引: 0-35
            int courtRecordEndIndex = courtRecordStartIndex + (courtRecordRows * 9); // 法庭记录槽位索引范围

            // 从法庭记录槽位转移到玩家物品栏
            if (index >= courtRecordStartIndex && index < courtRecordEndIndex) {
                if (!this.mergeItemStack(itemstack1, 0, 36, true)) {
                    return ItemStack.EMPTY;
                }
            }
            // 从玩家物品栏转移到法庭记录槽位
            else if (index < 36) {
                if (!this.mergeItemStack(itemstack1, courtRecordStartIndex, courtRecordEndIndex, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerEntity) {
        return true;
    }

    // 自定义槽位类
    private static class CourtRecordSlot extends SlotItemHandler {
        public CourtRecordSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return this.getItemHandler().isItemValid(this.getSlotIndex(), stack);
        }
    }
}
