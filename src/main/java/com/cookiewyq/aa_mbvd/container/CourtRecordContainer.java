package com.cookiewyq.aa_mbvd.container;

import com.cookiewyq.aa_mbvd.configs.ModConfigs;
import com.cookiewyq.aa_mbvd.network.Networking;
import com.cookiewyq.aa_mbvd.network.sendPacks.GetClientCourtRecordDataSendPack;
import com.cookiewyq.aa_mbvd.network.sendPacks.GetServerCourtRecordDataSendPack;
import com.cookiewyq.aa_mbvd.network.sendPacks.UpdateCourtRecordDataSendPack;
import com.cookiewyq.aa_mbvd.screen.CourtRecordScreen;
import com.cookiewyq.aa_mbvd.util.Pos2D;
import com.cookiewyq.aa_mbvd.worldSavedData.CourtRecordSaveData;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
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
    private Inventory courtRecordInventoryHandle;
    private final int courtRecordRows;
    private CourtRecordScreen courtRecordScreen;
    private final PlayerInventory playerInventory;
    public boolean hasAddedSlots = false;

    public CourtRecordContainer(int windowId, PlayerInventory playerInventory, IItemHandlerModifiable courtRecordInventory) {
        super(COURTRECORDS_CONTAINER.get(), windowId);
        this.courtRecordInventory = courtRecordInventory;
        this.courtRecordRows = ModConfigs.COURT_RECORD_ROWS.get();
        this.playerInventory = playerInventory;
    }

    public void addSlots(CourtRecordScreen screen) {
        if (hasAddedSlots) return;

        this.courtRecordScreen = screen;

        // Create empty slots immediately
        createEmptySlots(screen);

        // Then request data from server
        loadFromPlayer();
    }

    private void createEmptySlots(CourtRecordScreen screen) {
        int xOffset = 1;
        int yOffset = 1;

        // Add court record slots (indices 0 to courtRecordRows*9-1)
        for (int i = 0; i < courtRecordRows * 9; i++) {
            Pos2D pos = screen.getSlotPosition(i);
            this.addSlot(new CourtRecordSlot(courtRecordInventory, i, xOffset + pos.getX(), yOffset + pos.getY()) {
                @Override
                public void onSlotChanged() {
                    super.onSlotChanged();
                    saveToPlayer();
                }
            });
        }

        // Add player inventory slots (27 slots) (indices courtRecordRows*9 to courtRecordRows*9+26)
        for (int i = 0; i < 27; i++) {
            Pos2D pos = screen.getSlotPosition(courtRecordRows * 9 + i);
            this.addSlot(new Slot(playerInventory, i + 9, xOffset + pos.getX(), yOffset + pos.getY()));
        }

        // Add hotbar slots (9 slots) (indices courtRecordRows*9+27 to courtRecordRows*9+35)
        for (int i = 0; i < 9; i++) {
            Pos2D pos = screen.getSlotPosition(courtRecordRows * 9 + 27 + i);
            this.addSlot(new Slot(playerInventory, i, xOffset + pos.getX(), yOffset + pos.getY()));
        }

        hasAddedSlots = true;
    }

    public void updateSlotsWithData(Inventory inventoryWithData) {
        // Populate the slots with actual data once it arrives
        for (int i = 0; i < Math.min(courtRecordRows * 9, inventoryWithData.getSizeInventory()); i++) {
            courtRecordInventory.setStackInSlot(i, inventoryWithData.getStackInSlot(i).copy());
        }
        // Mark the container as dirty to ensure the GUI updates
        this.detectAndSendChanges();
    }

    private void loadFromPlayer() {
        // Only send network request on client side
        if (!playerInventory.player.world.isRemote) {
            // Server-side already has the data or will provide it through packets
            return;
        }
        System.out.println("Sending request to server for court record data");
        Networking.INSTANCE.sendToServer(new GetServerCourtRecordDataSendPack(playerInventory.player.getUniqueID()));
    }

    private void saveToPlayer() {
        // 创建一个适配器将 IItemHandlerModifiable 转换为 Inventory
        Inventory adapterInventory = new Inventory(courtRecordRows * 9) {
            @Override
            public ItemStack getStackInSlot(int index) {
                return courtRecordInventory.getStackInSlot(index);
            }

            @Override
            public void setInventorySlotContents(int index, ItemStack stack) {
                courtRecordInventory.setStackInSlot(index, stack);
            }
        };

        // 复制所有物品到适配器
        for (int i = 0; i < courtRecordRows * 9; i++) {
            adapterInventory.setInventorySlotContents(i, courtRecordInventory.getStackInSlot(i));
        }

        Networking.INSTANCE.sendToServer(new UpdateCourtRecordDataSendPack(playerInventory.player.getUniqueID(), adapterInventory));
    }


    @Override
    public void onContainerClosed(PlayerEntity player) {
        player.inventory.markDirty();
        super.onContainerClosed(player);
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
            player.inventory.markDirty();
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

        @Override
        public void onSlotChange(@Nonnull ItemStack oldStackIn, @Nonnull ItemStack newStackIn) {

        }

        @Override
        public void onSlotChanged() {
            super.onSlotChanged();
            // 这里不需要特殊处理，Forge会处理ItemStackHandler的脏标记
        }
    }

}