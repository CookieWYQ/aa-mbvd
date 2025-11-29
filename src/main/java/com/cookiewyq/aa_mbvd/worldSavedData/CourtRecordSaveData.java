package com.cookiewyq.aa_mbvd.worldSavedData;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CourtRecordSaveData extends WorldSavedData {

    public static final String NAME = "CourtRecordData";

    private final Map<UUID, Inventory> all_data = new HashMap<>();

    public CourtRecordSaveData() {
        super(NAME);
    }

    // 获取全局实例
    public static CourtRecordSaveData get(World world) {
        if (!(world instanceof ServerWorld)) {
            throw new RuntimeException("只能在服务端获取世界数据");
        }
        ServerWorld serverWorld = (ServerWorld) world;
        DimensionSavedDataManager storage = serverWorld.getSavedData();
        return storage.getOrCreate(CourtRecordSaveData::new, NAME);
    }

    public void removeData(UUID uuid) {
        all_data.remove(uuid);
        markDirty();
    }

    public boolean hasData(UUID uuid) {
        return all_data.containsKey(uuid);
    }

    public boolean isEmpty() {
        return all_data.isEmpty();
    }

    public void clear() {
        all_data.clear();
        markDirty();
    }

    public void updateData(UUID uuid, Inventory inventory) {
        all_data.put(uuid, inventory);
        markDirty();
    }

    public Inventory getData(UUID uuid) {
        Inventory original = all_data.get(uuid);
        if (original != null) {
            // 创建一个新的Inventory实例并复制原Inventory的内容
            Inventory copy = new Inventory(original.getSizeInventory());
            for (int i = 0; i < original.getSizeInventory(); i++) {
                copy.setInventorySlotContents(i, original.getStackInSlot(i).copy());
            }
            return copy;
        }
        throw new IllegalArgumentException("No data found for UUID: " + uuid);
    }


    @Override
    public void read(CompoundNBT compoundNBT) {
        all_data.clear();
        compoundNBT.keySet().forEach(key -> {
            Inventory inventory = new Inventory();
            inventory.read(compoundNBT.getList(key, 10));
            all_data.put(UUID.fromString(key), inventory);
        });
    }

    @Override
    public CompoundNBT write(CompoundNBT compoundNBT) {
        CompoundNBT nbt = new CompoundNBT();
        all_data.forEach((key, value) -> {
            nbt.put(key.toString(), value.write());
            value.markDirty();
        });
        return nbt;
    }
}
