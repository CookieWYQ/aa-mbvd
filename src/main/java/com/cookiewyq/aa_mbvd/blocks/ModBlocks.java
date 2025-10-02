package com.cookiewyq.aa_mbvd.blocks;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.blocks.custom.ores.SilverBlock;
import com.cookiewyq.aa_mbvd.blocks.custom.ores.SilverOre;
import com.cookiewyq.aa_mbvd.items.ModItemGroup;
import com.cookiewyq.aa_mbvd.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, AA_MbvdMod.MOD_ID);

    public static final RegistryObject<Block> SilverBlock = registryBlock("silver_block",
            SilverBlock::new, Rarity.UNCOMMON);


    public static final RegistryObject<Block> SilverOreBlock = registryBlock("silver_ore",
            SilverOre::new, Rarity.UNCOMMON);

    private static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block) {
        RegistryObject<T> tRegistryObject = BLOCKS.register(name, block);
        registryBlockItem(name, tRegistryObject);
        return tRegistryObject;
    }

    private static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block, Rarity rarity) {
        RegistryObject<T> tRegistryObject = BLOCKS.register(name, block);
        registryBlockItem(name, tRegistryObject, rarity);
        return tRegistryObject;
    }

    private static <T extends Block> void registryBlockItem(String name, Supplier<T> block, Rarity rarity) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()
                        .group(ModItemGroup.AA_MBVD_TAB)
                        .rarity(rarity)
        ));
    }

    private static <T extends Block> void registryBlockItem(String name, Supplier<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().group(ModItemGroup.AA_MBVD_TAB)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
