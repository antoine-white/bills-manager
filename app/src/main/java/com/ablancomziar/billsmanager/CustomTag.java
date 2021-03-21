package com.ablancomziar.billsmanager;

import android.content.Context;
import android.graphics.drawable.Drawable;

public final class CustomTag implements ITag {

    static private int ID_TAG = R.drawable.label;
    static private boolean HAS_ICON = true;

    private String name;
    private Drawable icon;

    public CustomTag(Context ctx, String name) {
        this.name = name;
        icon = ctx.getDrawable(ID_TAG);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasIcon() {
        return HAS_ICON;
    }

    @Override
    public Drawable getIcon() {
        return icon;
    }
}
