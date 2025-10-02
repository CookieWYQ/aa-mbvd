package com.cookiewyq.aa_mbvd.villagers;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.items.ModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = AA_MbvdMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModVillagers {
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType().equals(ModVillagerProfessions.CBT.get())) {
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();

            // 添加自定义交易
            trades.get(1).add(new SilverItemsForEmeraldsTrade(
                    new ItemStack(ModItems.AttorneysBadgeItem.get()), 2, 5, 5));
            trades.get(2).add(new SilverItemsForEmeraldsTrade(
                    new ItemStack(ModItems.MayasMagatamaItem.get()), 15, 8, 10));
        }
    }
}
