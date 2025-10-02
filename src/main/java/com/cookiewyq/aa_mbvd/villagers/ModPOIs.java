package com.cookiewyq.aa_mbvd.villagers;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.blocks.ModBlocks;
import com.google.common.collect.ImmutableSet;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModPOIs {
    public static final DeferredRegister<PointOfInterestType> POI_TYPES =
        DeferredRegister.create(ForgeRegistries.POI_TYPES, AA_MbvdMod.MOD_ID);
    
    public static final RegistryObject<PointOfInterestType> CBT_POI =
        POI_TYPES.register("cbt_work_block",
            () -> new PointOfInterestType("cbt_work",
                ImmutableSet.of(ModBlocks.SilverBlock.get().getDefaultState()), // 工作方块状态
                3, // 有效范围
                1)); // 最大村民数量

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
    }
}
