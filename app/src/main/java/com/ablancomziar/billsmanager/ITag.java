package com.ablancomziar.billsmanager;

import android.graphics.drawable.Drawable;

interface ITag {
    int getId();
    String getName();
    boolean hasIcon();
    Drawable getIcon();
}
