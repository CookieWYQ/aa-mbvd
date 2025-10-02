package com.cookiewyq.aa_mbvd.container;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.tileentity.CourtRecordsTileEntity;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CourtRecordsContainer extends Container {

    private final CourtRecordsTileEntity tileEntity; // 改为具体的TileEntity类型
    private final PlayerEntity playerEntity;
    private final IItemHandler playerInventory;

    public static final int GUI_WIDTH = 176;
    public static final int GUI_HEIGHT = 200; // 增加到200以容纳所有槽位

    public CourtRecordsContainer(int windowId, World world, BlockPos pos,
                                 PlayerInventory playerInventory, PlayerEntity player) {
        super(ModContainer.COURTRECORDS_CONTAINER.get(), windowId);
        // 创建虚拟TileEntity而不是从世界中获取
        this.tileEntity = new CourtRecordsTileEntity();
        playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);

        setupSlots(0, 0);
    }

private CourtRecordsTileEntity getTileEntityByCapability(PlayerEntity player) {
    return player.getCapability(Capabilities.GET_PLAYER_COURT_RECORDS_TILE_ENTITY_CAPABILITY)
            .map(data -> {
                // 假设 IGetPlayerCourtRecordsTileEntityData 有一个方法返回 CourtRecordsTileEntity 实例
                // 示例：return data.getTileEntity();
                // 如果没有，请根据实际Capability接口定义调整
                return (CourtRecordsTileEntity) data; // 若data本身就是CourtRecordsTileEntity则可以这样转
            })
            .orElse(new CourtRecordsTileEntity());
}



    public void setupSlots(int x, int y) {
        clearSlots();

        // 先添加容器槽位 (54个槽位，6行×9列)
        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                // 容器槽位从(8, 18)开始排列
                for (int i = 0; i < 6; i++) { // 6行
                    for (int j = 0; j < 9; j++) { // 9列
                        addSlot(new SlotItemHandler(h, i * 9 + j, 8 + j * 18, 18 + i * 18));
                    }
                }
            });
        }

        // 再添加玩家物品栏槽位，位置在容器槽位下方
        layoutPlayerInventorySlots(8, 140); // 调整Y坐标，使其在容器槽位下方
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // 玩家背包主要区域 (27个槽位)
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // 玩家快捷栏 (9个槽位)
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    public void clearSlots() {
        inventorySlots.clear();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }

        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }

        return index;
    }


    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 54;  // 6行×9列=54个槽位

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        Slot sourceSlot = inventorySlots.get(index);
        if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots (player inventory)
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            // 尝试将物品放入容器槽位(索引从36开始的54个槽位)
            if (!mergeItemStack(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX,
                    TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX,
                    VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }

        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.putStack(ItemStack.EMPTY);
        } else {
            sourceSlot.onSlotChanged();
        }

        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

}