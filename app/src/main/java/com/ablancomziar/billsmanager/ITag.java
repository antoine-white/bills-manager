package com.ablancomziar.billsmanager;

import android.graphics.drawable.Drawable;
import android.graphics.Color;

interface ITag {
    int getId();
    String getName();
    boolean hasIcon();
    Drawable getIcon();
    int getColor();
}
