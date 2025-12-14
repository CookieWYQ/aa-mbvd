package com.cookiewyq.aa_mbvd.container;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CourtRecordSlot extends SlotItemHandler {
    public CourtRecordSlot(IItemHandler iItemHandler, int index, int xPosition, int yPosition) {
        super(iItemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return canBePutInto(itemStack);
    }

    public static boolean canBePutInto(ItemStack stack) {
        return stack.getItem().getRegistryName() != null &&
                "aa_mbvd".equals(stack.getItem().getRegistryName().getNamespace());
    }

//    @Nullable
//    @Override
//    public Pair<ResourceLocation, ResourceLocation> getBackground() {
//        return new Pair<>(BACKGROUND_TEXTURE, SLOT_TEXTURE);
//    }
}