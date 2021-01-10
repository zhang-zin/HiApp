package com.zj.hi_ui.ui.tab.top;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

/**
 * @param <Color> 颜色，支持字符串和int的类型
 */
public class HiTabTopInfo<Color> {

    public enum TabType {
        BITMAP, TEXT
    }

    /**
     * tabBottom持有的Fragment
     */
    public Class<? extends Fragment> fragment;
    public String name;
    /**
     * bitmap 图标
     */
    public Bitmap defaultBitmap;
    public Bitmap selectedBitmap;

    public Color defaultColor;
    public Color tintColor;
    public TabType tabType;

    public HiTabTopInfo(String name, Bitmap defaultBitmap, Bitmap selectedBitmap) {
        this.name = name;
        this.defaultBitmap = defaultBitmap;
        this.selectedBitmap = selectedBitmap;
        this.tabType = TabType.BITMAP;
    }

    public HiTabTopInfo(String name, Color defaultColor, Color tintColor) {
        this.name = name;
        this.defaultColor = defaultColor;
        this.tintColor = tintColor;
        this.tabType = TabType.TEXT;
    }
}
