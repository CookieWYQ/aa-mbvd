package com.cookiewyq.aa_mbvd.items.custom.photos;

import com.cookiewyq.aa_mbvd.items.ModItemGroup;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class PhotoItem extends Item implements ICurioItem, IGetPhotoInfo {

    private final ResourceLocation photo_resourceLocation;
    private final int photo_width;
    private final int photo_height;

    public PhotoItem(ResourceLocation photo_resourceLocation, int photo_width, int photo_height) {
        super(new Properties()
                .maxStackSize(1)
                .setNoRepair()
                .group(ModItemGroup.AA_MBVD_TAB)
        );
        this.photo_resourceLocation = photo_resourceLocation;
        this.photo_width = photo_width;
        this.photo_height = photo_height;
    }

    public ResourceLocation getResourceLocation() {
        return photo_resourceLocation;
    }

    public int getPhotoWidth() {
        return photo_width;
    }

    public int getPhotoHeight() {
        return photo_height;
    }
}
