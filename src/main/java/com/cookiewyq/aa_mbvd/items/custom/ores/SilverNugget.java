package com.cookiewyq.aa_mbvd.items.custom.ores;

import com.cookiewyq.aa_mbvd.items.ModItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

public class SilverNugget extends Item {
    public SilverNugget() {
        super(new Properties()
                .group(ModItemGroup.AA_MBVD_TAB)
                .maxStackSize(64)
                .rarity(Rarity.UNCOMMON)
        );
    }
}
