package com.cookiewyq.aa_mbvd.common;

import javax.annotation.Nullable;

public class NumberHolder<T extends Number> {
    public T value;
    public NumberHolder(T value) {
        this.value = value;
    }
    public NumberHolder() {
        this.value = null;
    }
    public void set(T value) {
        this.value = value;
    }

    @Nullable
    public T get() {
        return value;
    }
}
