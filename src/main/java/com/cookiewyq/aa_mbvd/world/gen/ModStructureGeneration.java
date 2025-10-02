//package com.cookiewyq.aa_mbvd.world.gen;
//
//import com.cookiewyq.cookiemod.world.structure.ModStructures;
//import com.cookiewyq.cookiemod.world.structure.configs.CourtStructureConfig;
//import net.minecraft.util.RegistryKey;
//import net.minecraft.util.registry.Registry;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.gen.feature.IFeatureConfig;
//import net.minecraft.world.gen.feature.StructureFeature;
//import net.minecraftforge.common.BiomeDictionary;
//import net.minecraftforge.event.world.BiomeLoadingEvent;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.Random;
//import java.util.Set;
//import java.util.function.Supplier;
//
//public class ModStructureGeneration {
//    public static void generateStructures(final BiomeLoadingEvent event) {
//        RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, Objects.requireNonNull(event.getName()));
//        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);
//        if (types.contains(BiomeDictionary.Type.JUNGLE)) {
//            List<Supplier<StructureFeature<?, ?>>> supplierList = event.getGeneration().getStructures();
//            supplierList.add(() -> ModStructures.HOUSE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
//        }
//        if (types.contains(BiomeDictionary.Type.PLAINS)) {
//            List<Supplier<StructureFeature<?, ?>>> supplierList = event.getGeneration().getStructures();
//            supplierList.add(() -> ModStructures.COURT.get().withConfiguration(new CourtStructureConfig(new Random().nextInt(), new Random().nextInt(), new Random().nextInt())));
//        }
//    }
//}
