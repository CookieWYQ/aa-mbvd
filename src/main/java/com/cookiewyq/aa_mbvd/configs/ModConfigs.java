package com.cookiewyq.aa_mbvd.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfigs {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue isShowLittleMatterWhenUsingKillingEntity;
    public static ForgeConfigSpec.BooleanValue isEnableOldSchoolShowingEvidenceHUD;

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

        // 在结束前pop
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
