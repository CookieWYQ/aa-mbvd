package com.cookiewyq.aa_mbvd.items;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.items.custom.badges.AttorneysBadge;
import com.cookiewyq.aa_mbvd.items.custom.badges.Prosbadge;
import com.cookiewyq.aa_mbvd.items.custom.ores.SilverIngot;
import com.cookiewyq.aa_mbvd.items.custom.ores.SilverNugget;
import com.cookiewyq.aa_mbvd.items.custom.other.GantBoom;
import com.cookiewyq.aa_mbvd.items.custom.other.MayasMagatama;
import com.cookiewyq.aa_mbvd.items.custom.other.MetalDetector;
import com.cookiewyq.aa_mbvd.items.custom.other.QiansHandPrintCloth;
import com.cookiewyq.aa_mbvd.items.custom.photos.PhotoHelper;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AA_MbvdMod.MOD_ID);

    public static final RegistryObject<Item> AttorneysBadgeItem = ITEMS.register("attorneys_badge",
            AttorneysBadge::new);

    public static final RegistryObject<Item> ProsbadgeItem = ITEMS.register("prosbadge",
            Prosbadge::new);

    public static final RegistryObject<Item> SilverIngotItem = ITEMS.register("silver_ingot",
            SilverIngot::new);

    public static final RegistryObject<Item> SilverNuggetItem = ITEMS.register("silver_nugget",
            SilverNugget::new);

    public static final RegistryObject<Item> MayasMagatamaItem = ITEMS.register("mayas_magatama",
            MayasMagatama::new);

    public static final RegistryObject<Item> MetalDetectorItem = ITEMS.register("metal_detector",
            MetalDetector::new);

    public static final RegistryObject<Item> QiansHandPrintClothItem = ITEMS.register("cloth_with_qians_handprint_printed_on_it",
            QiansHandPrintCloth::new);

    public static final RegistryObject<Item> GantBoomItem = ITEMS.register("gant_boom",
            GantBoom::new);

    // Photos

    public static final RegistryObject<Item> PHOTO_annihilator_killer = PhotoHelper.registryPhoto("photo_annihilator_killer", 874, 640);

    public static final RegistryObject<Item> PHOTO_king_neil = PhotoHelper.registryPhoto("photo_king_neil", 960, 640);


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
