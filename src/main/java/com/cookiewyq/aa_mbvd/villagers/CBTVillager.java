package com.cookiewyq.aa_mbvd.villagers;

import com.cookiewyq.aa_mbvd.blocks.ModBlocks;
import com.cookiewyq.aa_mbvd.items.ModItems;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.merchant.villager.VillagerProfession;

public class CBTVillager extends VillagerProfession {
    public CBTVillager() {
        super("cbt", ModPOIs.CBT_POI.get(), ImmutableSet.of(ModItems.AttorneysBadgeItem.get(), ModItems.MayasMagatamaItem.get(), ModItems.SilverIngotItem.get()),
                ImmutableSet.of(ModBlocks.SilverBlock.get()), null);
    }
}
