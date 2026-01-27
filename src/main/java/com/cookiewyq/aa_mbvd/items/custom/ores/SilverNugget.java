package com.cookiewyq.aa_mbvd.items.custom.ores;

import com.cookiewyq.aa_mbvd.items.ModItemGroup;
import com.cookiewyq.aa_mbvd.particles.ModParticles;
import com.cookiewyq.aa_mbvd.particles.ObjectionParticleData;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.Objects;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SilverNugget extends Item {
    public SilverNugget() {
        super(new Properties()
                .group(ModItemGroup.AA_MBVD_TAB)
                .maxStackSize(64)
                .rarity(Rarity.UNCOMMON)
        );
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        // 添加调试输出
        System.out.println("SilverNugget onItemUse called!");
        
        if (context.getWorld().isRemote) {
            System.out.println("Adding particle on client side");
            
            // 在玩家位置生成粒子效果
            double x = context.getPlayer().getPosX();
            double y = context.getPlayer().getPosY() + 1.0; // 稍微高一点的位置
            double z = context.getPlayer().getPosZ();
            
            System.out.println("Player position: " + x + ", " + y + ", " + z);
            
            // 使用更明显的粒子参数
            Vector3d speed = new Vector3d(0.0, 0.0, 0.0); // 初始速度设为0
            Color color = new Color(255, 215, 0); // 金色粒子，更显眼
            float diameter = 2.0f; // 更大的尺寸
            
            // 尝试添加一个简单的内置粒子类型来测试是否是自定义粒子的问题
            context.getWorld().addParticle(net.minecraft.particles.ParticleTypes.END_ROD, 
                x, y, z, 
                0.0, 0.1, 0.0);
            
            System.out.println("Added end rod particle");
            
            // 发射自定义粒子
            context.getWorld().addParticle(
                new ObjectionParticleData(speed, color, diameter),
                x, y, z,
                0.0, 0.05, 0.0
            );
            
            System.out.println("Added objection particle");
            
            // 添加更多的粒子形成圆圈效果
            int particleCount = 8;
            for (int i = 0; i < particleCount; i++) {
                double angle = 2 * Math.PI * i / particleCount;
                double offsetX = Math.cos(angle) * 0.5;
                double offsetZ = Math.sin(angle) * 0.5;
                
                context.getWorld().addParticle(
                    new ObjectionParticleData(new Vector3d(0, 0.1, 0), new Color(255, 215, 0), 1.5f),
                    x + offsetX, y, z + offsetZ,
                    offsetX * 0.02, 0.05, offsetZ * 0.02
                );
            }
            
            System.out.println("Added circle particles");
        } else {
            System.out.println("Not on client side, skipping particle addition");
        }
        return super.onItemUse(context);
    }
}