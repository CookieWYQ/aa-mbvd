package com.cookiewyq.aa_mbvd.world.structure.structures;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.world.structure.configs.CourtStructureConfig;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CourtStructure extends Structure<CourtStructureConfig> {

    public CourtStructure() {
        super(CourtStructureConfig.CODEC);
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public IStartFactory<CourtStructureConfig> getStartFactory() {
        return Start::new;
    }

    public static class Start extends StructureStart<CourtStructureConfig> {
        public Start(Structure<CourtStructureConfig> structure, int chunkX, int chunkZ,
                     MutableBoundingBox boundingBox, int references, long seed) {
            super(structure, chunkX, chunkZ, boundingBox, references, seed);
        }

        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistryManager,
                                   ChunkGenerator chunkGenerator,
                                   TemplateManager templateManager,
                                   int chunkX, int chunkZ,
                                   Biome biome,
                                   CourtStructureConfig config) {
            // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            BlockPos blockpos = new BlockPos(x, 0, z);

            //addpieces()
            JigsawManager.func_242837_a(dynamicRegistryManager,
                    new VillageConfig(() -> dynamicRegistryManager.getRegistry(Registry.JIGSAW_POOL_KEY)
                            .getOrDefault(new ResourceLocation(AA_MbvdMod.MOD_ID, "court/start_pool")),
                            10), AbstractVillagePiece::new, chunkGenerator, templateManager,
                    blockpos, this.components, this.rand, false, true);

            this.components.forEach(piece -> piece.offset(0, 1, 0));
            this.components.forEach(piece -> piece.getBoundingBox().minY -= 1);

            this.recalculateStructureSize();
        }
    }
}

