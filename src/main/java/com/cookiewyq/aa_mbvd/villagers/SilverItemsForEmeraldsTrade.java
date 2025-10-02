package com.cookiewyq.aa_mbvd.villagers;

import com.cookiewyq.aa_mbvd.items.ModItems;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;


@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SilverItemsForEmeraldsTrade implements VillagerTrades.ITrade {
    private final ItemStack itemStack;
    private final int cost;
    private final int maxUses;
    private final int xpValue;

    public SilverItemsForEmeraldsTrade(ItemStack itemStack, int cost, int maxUses, int xpValue) {
        this.itemStack = itemStack;
        this.cost = cost;
        this.maxUses = maxUses;
        this.xpValue = xpValue;
    }

    @Override
    public MerchantOffer getOffer(Entity trader, Random rand) {
        ItemStack emeraldStack = new ItemStack(ModItems.SilverIngotItem.get(), cost);
        return new MerchantOffer(emeraldStack, itemStack.copy(), maxUses, xpValue, 0.5F * rand.nextFloat());
    }
}
