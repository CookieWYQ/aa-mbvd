package com.cookiewyq.aa_mbvd.items.custom.photos;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import static com.cookiewyq.aa_mbvd.items.ModItems.ITEMS;

public class PhotoHelper {
    public static RegistryObject<Item> registryPhoto(String id, int width, int height) {
        return ITEMS.register(id, () -> new PhotoItem(new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/photos/" + id + ".png"), width, height));
    }

    public static int[] getFittingSize(int photoWidth, int photoHeight, int screenWidth, int screenHeight) {
        // 添加边界检查
        if (photoWidth <= 0 || photoHeight <= 0) {
            return new int[]{screenWidth, screenHeight}; // 或其他默认值
        }

        for (int i = 100; i > 0; i--) { // 从100%开始递减，确保获得最大可能的合适尺寸
            int newWidth = photoWidth * i / 100;
            int newHeight = photoHeight * i / 100;
            if (newWidth <= screenWidth && newHeight <= screenHeight - 10) {
                return new int[]{newWidth, newHeight};
            }
        }
        return new int[]{photoWidth / 2, photoHeight / 2}; // 保底返回
    }
}
