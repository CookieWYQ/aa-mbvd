package com.cookiewyq.aa_mbvd.container;

import com.cookiewyq.aa_mbvd.configs.ModConfigs;
import com.cookiewyq.aa_mbvd.screen.CourtRecordScreen;
import com.cookiewyq.aa_mbvd.util.Pos2D;
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

import static com.cookiewyq.aa_mbvd.container.ModContainerTypes.COURTRECORDS_CONTAINER;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CourtRecordContainer extends Container {

    private final IItemHandlerModifiable courtRecordInventory;
    private final int courtRecordRows;
    private CourtRecordScreen courtRecordScreen;
    private final PlayerInventory playerInventory;
    private boolean hasAddedSlots = false;

    public CourtRecordContainer(int windowId, PlayerInventory playerInventory, IItemHandlerModifiable courtRecordInventory) {
        super(COURTRECORDS_CONTAINER.get(), windowId);
        this.courtRecordInventory = courtRecordInventory;
        this.courtRecordRows = ModConfigs.COURT_RECORD_ROWS.get();
        this.playerInventory = playerInventory;
    }

    public void addSlots(CourtRecordScreen screen) {
        if (hasAddedSlots) return;
        hasAddedSlots = true;

        this.courtRecordScreen = screen;

        int xOffset = 1;
        int yOffset = 1;

        // 添加法庭记录槽位 (索引 0 到 courtRecordRows*9-1)
        for (int i = 0; i < courtRecordRows * 9; i++) {
            Pos2D pos = screen.getSlotPosition(i);
            this.addSlot(new CourtRecordSlot(courtRecordInventory, i, xOffset + pos.getX(), yOffset + pos.getY()));
        }

        // 添加玩家物品栏槽位(27个) (索引 courtRecordRows*9 到 courtRecordRows*9+26)
        for (int i = 0; i < 27; i++) {
            Pos2D pos = screen.getSlotPosition(courtRecordRows * 9 + i);
            this.addSlot(new Slot(playerInventory, i + 9, xOffset + pos.getX(), yOffset + pos.getY()));
        }

        // 添加快捷栏槽位(9个) (索引 courtRecordRows*9+27 到 courtRecordRows*9+35)
        for (int i = 0; i < 9; i++) {
            Pos2D pos = screen.getSlotPosition(courtRecordRows * 9 + 27 + i);
            this.addSlot(new Slot(playerInventory, i, xOffset + pos.getX(), yOffset + pos.getY()));
        }
    }

    // 处理Shift+点击物品转移
    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        // 添加边界检查防止IndexOutOfBoundsException
        if (index < 0 || index >= this.inventorySlots.size()) {
            return ItemStack.EMPTY;
        }

        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            // 法庭记录槽位索引范围: 0 to (courtRecordRows * 9 - 1)
            // 玩家物品栏槽位索引范围: (courtRecordRows * 9) to (courtRecordRows * 9 + 35)

            if (index < courtRecordRows * 9) {
                // 从法庭记录槽位转移到玩家物品栏
                if (!this.mergeItemStack(itemstack1, courtRecordRows * 9, courtRecordRows * 9 + 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= courtRecordRows * 9 && index < courtRecordRows * 9 + 36) {
                // 从玩家物品栏转移到法庭记录槽位
                if (!this.mergeItemStack(itemstack1, 0, courtRecordRows * 9, false)) {
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