package com.zj.hi_ui.ui.tab.bottom;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

/**
 * @param <Color> 颜色，支持字符串和int的类型
 */
public class HiTabBottomInfo<Color> {

    public enum TabType {
        BITMAP, ICON
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
    /**
     * Tips：在java代码中直接设置iconfont字符串无效，需要定义在String.xml中
     */
    public String iconFont;

    public String defaultIconName;
    public String selectedIconName;
    public Color defaultColor;
    public Color tintColor;
    public TabType tabType;

    public HiTabBottomInfo(String name, Bitmap defaultBitmap, Bitmap selectedBitmap) {
        this.name = name;
        this.defaultBitmap = defaultBitmap;
        this.selectedBitmap = selectedBitmap;
        this.tabType = TabType.BITMAP;
    }

    public HiTabBottomInfo(String name, String iconFont, String defaultIconName, String selectedIconName, Color defaultColor, Color tintColor) {
        this.name = name;
        this.iconFont = iconFont;
        this.defaultIconName = defaultIconName;
        this.selectedIconName = selectedIconName;
        this.defaultColor = defaultColor;
        this.tintColor = tintColor;
        this.tabType = TabType.ICON;
    }
}
