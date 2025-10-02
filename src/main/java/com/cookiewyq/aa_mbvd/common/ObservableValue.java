package com.cookiewyq.aa_mbvd.common;

import java.util.Objects;

public class ObservableValue<T> {
    public T value;
    private Runnable onChangeListener;

    public ObservableValue(T initialValue) {
        this.value = initialValue;
    }

    public ObservableValue(T initialValue, Runnable onChangeListener) {
        this.value = initialValue;
        this.onChangeListener = onChangeListener;
    }

    public void set(T newValue) {
        if (!Objects.equals(this.value, newValue)) {
            this.value = newValue;
            if (onChangeListener != null) {
                onChangeListener.run();
            }
        }
    }

    public T get() {
        return this.value;
    }

    public void setOnChangeListener(Runnable listener) {
        this.onChangeListener = listener;
    }
}
