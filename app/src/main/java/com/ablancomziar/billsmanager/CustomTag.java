package com.ablancomziar.billsmanager;

import android.graphics.drawable.Drawable;

public final class CustomTag implements ITag {
    private String name;

    public CustomTag(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasIcon() {
        return false;
    }

    @Override
    public Drawable getIcon() {
        return null;
    }
}
