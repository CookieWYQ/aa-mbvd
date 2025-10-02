package com.cookiewyq.aa_mbvd.util.little_matter;

public enum LittleMatter_Words {
    Objection("objection"),
    Hold_it("holdit"),
    Take_that("takethat"),
    None("none_word");

    private final String id;

    LittleMatter_Words(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static LittleMatter_Words getWord(String id) {
        for (LittleMatter_Words word : values()) {
            if (word.getId().equals(id)) {
                return word;
            }
        }
        return Objection;
    }

    public static LittleMatter_Words[] getAllWords() {
        return values();
    }

    public static LittleMatter_Words getRandomWord() {
        LittleMatter_Words t = values()[(int) (Math.random() * values().length)];
        if (t == None) {
            while (t == None) {
                t = values()[(int) (Math.random() * values().length)];
            }
        }
        return t;
    }
}
