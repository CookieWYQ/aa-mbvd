package com.cookiewyq.aa_mbvd.util.little_matter;

public class LittleMatter_Tools {
    public static String getSoundString(LittleMatter_Words word, LittleMatter_Roles role, LittleMatter_Langs  lang) {
        return role.getId() + "_" + word.getId() + "_" + lang.getId();
    }
}
