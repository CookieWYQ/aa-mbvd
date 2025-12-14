package com.cookiewyq.aa_mbvd.configs;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = "aa_mbvd", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfigs {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue isShowLittleMatterWhenUsingKillingEntity;
    public static ForgeConfigSpec.BooleanValue isEnableOldSchoolShowingEvidenceHUD;
//    public static final ForgeConfigSpec.IntValue COURT_RECORD_ROWS;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        // 先push一个配置节
        COMMON_BUILDER.push("general");

        // 添加注释分隔
        COMMON_BUILDER.comment("=======================================");
        COMMON_BUILDER.comment("");
        COMMON_BUILDER.comment("---------- AA-MBVD Settings ----------");

        isShowLittleMatterWhenUsingKillingEntity = COMMON_BUILDER
                .comment("If show Little Matter 'Take That' When someone Killing entity.")
                .define("is_show_little_matter_when_killing_entity", false);

        isEnableOldSchoolShowingEvidenceHUD = COMMON_BUILDER
                .comment("If enable Old School Showing Evidence HUD.")
                .define("is_enable_old_school_showing_evidence_hud", false);

//        COURT_RECORD_ROWS = COMMON_BUILDER
//                .comment("The number of court_record_rows (1-6)")
//                .defineInRange("court_record_rows", 3, 1, 6);

        // 在结束前pop
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @SubscribeEvent
    public static void onConfigReload(ModConfig.Reloading event) {
        // 配置重载时可以在这里添加处理逻辑
        // 例如：通知客户端重新创建GUI等
    }
}
