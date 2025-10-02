package com.cookiewyq.aa_mbvd.util;

public class tool_enums {
    public enum CameraMode {
        FIRST_PERSON(0),
        THIRD_PERSON_BACK(1),
        THIRD_PERSON_FRONT(2);

        private final int value;

        CameraMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static CameraMode fromValue(int value) {
            for (CameraMode mode : values()) {
                if (mode.value == value) {
                    return mode;
                }
            }
            return null;
        }
    }

}
