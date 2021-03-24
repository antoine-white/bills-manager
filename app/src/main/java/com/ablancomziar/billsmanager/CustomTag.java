package com.ablancomziar.billsmanager;

import android.content.Context;
import android.graphics.drawable.Drawable;

public final class CustomTag implements ITag {

    static private int ID_TAG = R.drawable.label;
    static private boolean HAS_ICON = true;

    private String name;
    private Drawable icon;
    private final int id;
    private final int color;


    public CustomTag(Context ctx, String name, int id, int color) {
        this.name = name;
        icon = ctx.getDrawable(ID_TAG);
        this.id = id;
        this.color = color;
    }

    @Override
    public int getId() {
        return id;
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

    @Override
    public int getColor() {
        return color;
    }
}
