package com.ablancomziar.billsmanager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

public final class DefaultTag implements ITag {
    private String name;
    private Drawable icon;
    private final int id;
    private int color;

    private DefaultTag(String name, Drawable Icon, int id, int c) {
        this.name = name;
        icon = Icon;
        this.id = id;
        color = c;
    }

    static public DefaultTag[] getAllDefaultTag(Context ctx){
        return new DefaultTag[]{
            new DefaultTag(ctx.getString(R.string.alim),ctx.getDrawable(R.drawable.food),0,Color.rgb(20, 155, 20)),
            new DefaultTag(ctx.getString(R.string.home),ctx.getDrawable(R.drawable.home),1,Color.rgb(0, 0, 0)),
            new DefaultTag(ctx.getString(R.string.life),ctx.getDrawable(R.drawable.coffe),2, Color.rgb(200, 0, 200)),
            new DefaultTag(ctx.getString(R.string.tran),ctx.getDrawable(R.drawable.car),3, Color.rgb(0, 100, 100)),
            new DefaultTag(ctx.getString(R.string.heal),ctx.getDrawable(R.drawable.health),4 , Color.rgb(255, 0, 0)),
            new DefaultTag(ctx.getString(R.string.digi),ctx.getDrawable(R.drawable.digital),5 ,Color.rgb(0, 0, 255))
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

    @Override
    public int getColor() {
        return color;
    }
}
