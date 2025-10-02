package com.cookiewyq.aa_mbvd.items;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModItemGroup {
    public static final ItemGroup AA_MBVD_TAB = new ItemGroup("aa_mbvd_tab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.AttorneysBadgeItem.get());
        }
    };
}
