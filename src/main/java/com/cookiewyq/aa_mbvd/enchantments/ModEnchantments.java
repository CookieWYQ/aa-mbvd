package com.cookiewyq.aa_mbvd.enchantments;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, AA_MbvdMod.MOD_ID);

    public static final RegistryObject<Enchantment> BOOM_ENCHANTMENT = ENCHANTMENTS.register("boom_enchant",
            BoomEnchantment::new);

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}
