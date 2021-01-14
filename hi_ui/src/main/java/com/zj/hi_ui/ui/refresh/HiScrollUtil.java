package com.zj.hi_ui.ui.refresh;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zj.hi_library.hiLog.HiLog;

/**
 * @author 张锦
 */
public class HiScrollUtil {

    /**
     * 寻找可滑动的view
     *
     * @param viewGroup view容器
     * @return 返回找到的结果
     */
    public static View findScrollableChild(@NonNull ViewGroup viewGroup) {
        View child = viewGroup.getChildAt(1);
        if (child instanceof RecyclerView || child instanceof AdapterView) {
            return child;
        }
        if (child instanceof ViewGroup) {
            View tempChild = ((ViewGroup) child).getChildAt(0);
            if (tempChild instanceof RecyclerView || tempChild instanceof AdapterView) {
                child = tempChild;
            }
        }
        return child;
    }

    /**
     * 判断child是否发生了滚动
     *
     * @return true 发生了滚动
     */
    public static boolean childScrolled(@NonNull View child) {
        if (child instanceof AdapterView) {
            AdapterView adapterView = (AdapterView) child;
            boolean b = adapterView.getFirstVisiblePosition() != 0
                    || adapterView.getFirstVisiblePosition() == 0 && adapterView.getChildAt(0) != null;
            if (b && adapterView.getChildAt(0).getTop() < 0) {
                return true;
            }
        } else if (child.getScrollY() > 0) {
            return true;
        }
        if (child instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) child;
            View view = recyclerView.getChildAt(0);
            int firstPosition = recyclerView.getChildAdapterPosition(view);
            HiLog.d("----:top", view.getTop() + "");
            return firstPosition != 0 || view.getTop() != 0;
        }
        return false;
    }
}
