package com.cookiewyq.aa_mbvd.container;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.configs.ModConfigs;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CourtRecordInventory implements IItemHandlerModifiable, Capability.IStorage<CourtRecordInventory> {

    private NonNullList<ItemStack> items;

    public CourtRecordInventory() {
        // 根据配置初始化槽位数量（行数 × 9列）
        int rows = ModConfigs.COURT_RECORD_ROWS.get();
        this.items = NonNullList.withSize(rows * 9, ItemStack.EMPTY);
    }

    @Override
    public int getSlots() {
        return items.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!isItemValid(slot, stack)) {
            return stack; // 不允许放入的物品直接返回
        }

        // 正常的插入逻辑
        if (stack.isEmpty()) return ItemStack.EMPTY;
        ItemStack existing = items.get(slot);
        int limit = Math.min(getSlotLimit(slot), stack.getMaxStackSize());

        if (!existing.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;
            limit -= existing.getCount();
        }

        if (limit <= 0) return stack;
        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                items.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        // 正常的提取逻辑
        if (amount == 0) return ItemStack.EMPTY;

        ItemStack existing = items.get(slot);
        if (existing.isEmpty()) return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());
        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                items.set(slot, ItemStack.EMPTY);
                return existing;
            } else {
                return existing.copy();
            }
        } else {
            if (!simulate) {
                items.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
            }
            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        // 这里定义可以放入的物品
        return isEvidenceItem(stack);
    }

    private boolean isEvidenceItem(ItemStack stack) {
        // 获取物品的注册名
        ResourceLocation itemId = stack.getItem().getRegistryName();

        // 检查物品是否属于特定模组
        if (itemId != null) {
            // 允许您模组中的所有物品作为证据
            return itemId.getNamespace().equals("aa_mbvd");
        }

        return false;
    }

    public static class Provider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {
        private final LazyOptional<CourtRecordInventory> instance = LazyOptional.of(CourtRecordInventory::new);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == Capabilities.COURT_RECORD_INVENTORY_CAPABILITY ? instance.cast() : LazyOptional.empty();
        }

        // 确保Provider可以被正确序列化
        public CompoundNBT serializeNBT() {
            return (CompoundNBT) instance.map(inv -> Objects.requireNonNull(inv.writeNBT(Capabilities.COURT_RECORD_INVENTORY_CAPABILITY, inv, null))).orElse(new CompoundNBT());
        }

        public void deserializeNBT(CompoundNBT nbt) {
            instance.ifPresent(inv -> inv.readNBT(Capabilities.COURT_RECORD_INVENTORY_CAPABILITY, inv, null, nbt));
        }
    }

    public static class Factory implements java.util.concurrent.Callable<CourtRecordInventory> {
        @Override
        public CourtRecordInventory call() {
            return new CourtRecordInventory();
        }
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        items.set(slot, stack);
    }

    @Nullable
    @Override
    public INBT writeNBT(Capability<CourtRecordInventory> capability, CourtRecordInventory instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        for (int i = 0; i < instance.items.size(); i++) {
            if (!instance.items.get(i).isEmpty()) {
                nbt.put("slot_" + i, instance.items.get(i).serializeNBT());
            }
        }
        nbt.putInt("rows", instance.items.size() / 9); // 保存行数信息
        return nbt;
    }

    @Override
    public void readNBT(Capability<CourtRecordInventory> capability, CourtRecordInventory instance, Direction side, INBT nbt) {
        CompoundNBT compound = (CompoundNBT) nbt;

        int savedRows = compound.contains("rows") ? compound.getInt("rows") : 3;
        int currentRows = ModConfigs.COURT_RECORD_ROWS.get();

        // 调整大小时保留原有物品
        if (savedRows != currentRows || instance.items.size() != currentRows * 9) {
            NonNullList<ItemStack> oldItems = instance.items;
            instance.items = NonNullList.withSize(currentRows * 9, ItemStack.EMPTY);

            // 复制原有物品到新列表
            int minSize = Math.min(oldItems.size(), instance.items.size());
            for (int i = 0; i < minSize; i++) {
                if (!oldItems.get(i).isEmpty()) {
                    instance.items.set(i, oldItems.get(i).copy());
                }
            }
        }

        // 读取保存的物品
        for (int i = 0; i < instance.items.size(); i++) {
            if (compound.contains("slot_" + i)) {
                instance.items.set(i, ItemStack.read(compound.getCompound("slot_" + i)));
            }
        }
    }


}
