package com.ablancomziar.billsmanager;

import android.content.Context;
import android.graphics.drawable.Drawable;

public final class DefaultTag implements ITag {
    private String name;
    private Drawable icon;
    private final int id;

    private DefaultTag(String name, Drawable Icon, int id) {
        this.name = name;
        icon = Icon;
        this.id = id;
    }

    static public DefaultTag[] getAllDefaultTag(Context ctx){
        return new DefaultTag[]{
            new DefaultTag(ctx.getString(R.string.alim),ctx.getDrawable(R.drawable.food),0),
            new DefaultTag(ctx.getString(R.string.home),ctx.getDrawable(R.drawable.home),1),
            new DefaultTag(ctx.getString(R.string.life),ctx.getDrawable(R.drawable.coffe),2),
            new DefaultTag(ctx.getString(R.string.tran),ctx.getDrawable(R.drawable.car),3),
            new DefaultTag(ctx.getString(R.string.heal),ctx.getDrawable(R.drawable.health),4),
            new DefaultTag(ctx.getString(R.string.digi),ctx.getDrawable(R.drawable.digital),5)
        };
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
        return icon != null;
    }

    @Override
    public Drawable getIcon() {
        return icon;
    }
}
