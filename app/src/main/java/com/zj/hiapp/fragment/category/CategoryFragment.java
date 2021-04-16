package com.zj.hiapp.fragment.category;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.zj.common.ui.component.HiBaseFragment;
import com.zj.hi_library.hiLog.HiLog;
import com.zj.hi_library.restful.HiCallback;
import com.zj.hi_library.restful.HiResponse;
import com.zj.hi_ui.ui.slide.HiSliderView;
import com.zj.hi_ui.ui.tab.bottom.HiTabBottomLayout;
import com.zj.hiapp.R;
import com.zj.hiapp.http.ApiFactory;
import com.zj.hiapp.http.api.CategoryApi;
import com.zj.hiapp.http.model.CategoryModel;
import com.zj.hiapp.http.model.ChildCategory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class CategoryFragment extends HiBaseFragment {

    private CategoryApi categoryApi;
    private HiSliderView hiSliderView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewGroup root_container = view.findViewById(R.id.root_container);
        HiTabBottomLayout.clipBottomPadding(root_container);

        hiSliderView = view.findViewById(R.id.slider_view);

        categoryApi = ApiFactory.INSTANCE.create(CategoryApi.class);
        queryCategoryList();
    }

    private void queryCategoryList() {
        categoryApi.getTreeJson().enqueue(new HiCallback<List<CategoryModel>>() {
            @Override
            public void onSuccess(@NotNull HiResponse<List<CategoryModel>> response) {
                bindSlierMenu(response.getData());
            }

            @Override
            public void onFailed(@NotNull Throwable throwable) {
                HiLog.e("throwable" + throwable.getMessage());
            }
        });

    }

    private Map<Integer, String> parentChapterMap = new HashMap<>();

    private void bindSlierMenu(List<CategoryModel> data) {
        if (data == null || data.size() <= 0) {
            return;
        }
        List<ChildCategory> childrenList = new ArrayList<>();
        for (CategoryModel datum : data) {
            if (data.size() > 0) {
                childrenList.addAll(datum.component1());
                parentChapterMap.put(datum.getId(), datum.getName());
            }
        }

        bindSlierContent(childrenList);
        hiSliderView.setMenuViewVisibility(false);

        /*hiSliderView.bindMenuView(R.layout.hi_slider_menu_item, data.size(), (hiViewHolder, integer) -> {
            CategoryModel categoryModel = data.get(integer);
            if (!TextUtils.isEmpty(categoryModel.getName())) {
                TextView itemTitle = hiViewHolder.itemView.findViewById(R.id.menu_item_title);
                itemTitle.setText(categoryModel.getName());
            }
            return Unit.INSTANCE;
        }, (hiViewHolder, integer) -> {
            bindSlierContent(data.get(integer).component1());
            return Unit.INSTANCE;
        });*/
    }

    private void bindSlierContent(List<ChildCategory> childrenList) {

        if (childrenList == null || childrenList.size() <= 0) {
            return;
        }
        CategoryItemDecoration categoryItemDecoration = new CategoryItemDecoration(integer -> {
            int parentChapterId = childrenList.get(integer).component6();
            return parentChapterMap.get(parentChapterId);
        }, 3);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        gridLayoutManager.setSpanSizeLookup(new CategorySpanSizeLookup(childrenList, 3));
        hiSliderView.bindContentView(R.layout.hi_slider_content_item,
                childrenList.size(),
                categoryItemDecoration,
                gridLayoutManager,
                (hiViewHolder, integer) -> {
                    ChildCategory children = childrenList.get(integer);

                    if (!TextUtils.isEmpty(children.getName())) {
                        TextView itemTitle = hiViewHolder.itemView.findViewById(R.id.content_item_title);
                        itemTitle.setText(children.getName());
                    }
                    return Unit.INSTANCE;
                },
                (hiViewHolder, integer) -> {
                    ChildCategory children = childrenList.get(integer);
                    Toast.makeText(requireContext(), children.getName(), Toast.LENGTH_SHORT).show();
                    return Unit.INSTANCE;
                });
    }
}