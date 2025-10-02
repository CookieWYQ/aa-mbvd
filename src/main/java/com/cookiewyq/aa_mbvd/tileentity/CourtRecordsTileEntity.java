package com.cookiewyq.aa_mbvd.tileentity;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CourtRecordsTileEntity extends TileEntity {
    private final ItemStackHandler itemHandler = new ItemStackHandler(54) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
            // 同步到客户端
            if (world != null && !world.isRemote) {
                world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public CourtRecordsTileEntity() {
        super(ModTileEntities.COURT_RECORDS_TILE_ENTITY.get());
    }

    public CourtRecordsTileEntity(TileEntity type) {
        super(type.getType());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        handler.invalidate();
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        itemHandler.deserializeNBT(nbt.getCompound("Items"));
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.put("Items", itemHandler.serializeNBT());
        return super.write(nbt);
    }

    // 添加获取ItemHandler的方法
    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }
}
