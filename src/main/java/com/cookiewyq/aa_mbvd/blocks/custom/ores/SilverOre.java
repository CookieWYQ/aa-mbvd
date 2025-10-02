package com.cookiewyq.aa_mbvd.blocks.custom.ores;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class SilverOre extends OreBlock {
    public SilverOre() {
        super(AbstractBlock.Properties.create(Material.ROCK)

                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
                .setRequiresTool()
                .hardnessAndResistance(7.8f)

        );
    }
}
