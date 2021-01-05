package com.zj.hi_library.hiLog.printer;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zj.hi_library.R;
import com.zj.hi_library.hiLog.HiLogConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 在app界面上显示
 *
 * @author zhangjin
 */
public class HiViewPrinter implements HiLogPrinter, HiViewPrinterProvider.RecycleViewCallback {

    private List<HiLogMo> hiLogMoList = new ArrayList<>();
    private final HiViewPrinterProvider hiViewPrinterProvider;

    public HiViewPrinter(Application app) {
        hiViewPrinterProvider = HiViewPrinterProvider.init(app, this);
    }

    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String printString) {

        hiLogMoList.add(new HiLogMo());

        RecyclerView recyclerView = hiViewPrinterProvider.getRecyclerView();
        if (recyclerView == null) {
            return;
        }

        if (recyclerView.getAdapter() == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
            recyclerView.setLayoutManager(layoutManager);
            LogAdapter adapter = new LogAdapter(hiLogMoList);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void showLogView() {
        RecyclerView recyclerView = hiViewPrinterProvider.getRecyclerView();
        if (recyclerView != null) {
            if (recyclerView.getAdapter() == null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                LogAdapter adapter = new LogAdapter(hiLogMoList);
                recyclerView.setAdapter(adapter);
            } else {
//                LogAdapter adapter = (LogAdapter) recyclerView.getAdapter();
//                adapter.hiLogMoList = this.hiLogMoList;
//                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void closeLogView() {

    }

    private static class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {

        private List<HiLogMo> hiLogMoList;

        public LogAdapter(List<HiLogMo> hiLogMoList) {
            this.hiLogMoList = hiLogMoList;
        }

        @NonNull
        @Override
        public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.hilog_item, parent, false);
            return new HiViewPrinter.LogViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return hiLogMoList.size();
        }
    }

    private static class LogViewHolder extends RecyclerView.ViewHolder {

        TextView tag;
        TextView message;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.tag);
            message = itemView.findViewById(R.id.message);
        }
    }
}
