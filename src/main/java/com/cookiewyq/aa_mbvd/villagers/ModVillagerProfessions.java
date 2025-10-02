package com.cookiewyq.aa_mbvd.villagers;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ModVillagerProfessions {
    public static DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, AA_MbvdMod.MOD_ID);

    public static final RegistryObject<VillagerProfession> CBT = VILLAGER_PROFESSIONS.register("cbt", CBTVillager::new);

    public static void register(IEventBus bus) {
        VILLAGER_PROFESSIONS.register(bus);
    }
}
