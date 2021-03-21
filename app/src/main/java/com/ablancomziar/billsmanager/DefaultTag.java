package com.ablancomziar.billsmanager;

import android.content.Context;
import android.graphics.drawable.Drawable;

public final class DefaultTag implements ITag {
    private String name;
    private Drawable icon;

    private DefaultTag(String name, Drawable Icon) {
        this.name = name;
        icon = Icon;
    }

    static public DefaultTag[] getAllDefaultTag(Context ctx){
        return new DefaultTag[]{
            new DefaultTag(ctx.getString(R.string.alim),ctx.getDrawable(R.drawable.food)),
            new DefaultTag(ctx.getString(R.string.home),ctx.getDrawable(R.drawable.home)),
            new DefaultTag(ctx.getString(R.string.life),ctx.getDrawable(R.drawable.coffe)),
            new DefaultTag(ctx.getString(R.string.tran),ctx.getDrawable(R.drawable.car)),
            new DefaultTag(ctx.getString(R.string.heal),ctx.getDrawable(R.drawable.health)),
            new DefaultTag(ctx.getString(R.string.digi),ctx.getDrawable(R.drawable.digital))
        };
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasIcon() {
        return icon != null;
    }

    @Override
    public Drawable getIcon() {
        return icon;
    }
}
