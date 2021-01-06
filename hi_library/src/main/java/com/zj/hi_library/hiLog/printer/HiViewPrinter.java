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
import com.zj.hi_library.hiLog.HiLogType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 在app界面上显示
 *
 * @author zhangjin
 */
public class HiViewPrinter implements HiLogPrinter, HiViewPrinterProvider.RecycleViewCallback {

    private List<HiLogMo> hiLogMoList = new ArrayList<>();
    private final HiViewPrinterProvider hiViewPrinterProvider;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    public HiViewPrinter(Application app) {
        hiViewPrinterProvider = HiViewPrinterProvider.init(app, this);
    }

    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String printString) {

        hiLogMoList.add(new HiLogMo(level, tag, printString, simpleDateFormat.format(System.currentTimeMillis())));

        RecyclerView recyclerView = hiViewPrinterProvider.getRecyclerView();
        if (recyclerView == null) {
            return;
        }

        if (recyclerView.getAdapter() == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
            recyclerView.setLayoutManager(layoutManager);
            LogAdapter adapter = new LogAdapter(hiLogMoList);
            recyclerView.setAdapter(adapter);
        } else {
            HiViewPrinter.LogAdapter adapter = (HiViewPrinter.LogAdapter) recyclerView.getAdapter();
            adapter.notifyItemChanged(hiLogMoList.size() - 1);
        }

        // 滚动到对应的位置
        recyclerView.getLayoutManager().scrollToPosition(Math.max(hiLogMoList.size() - 1, 0));
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
                layoutManager.scrollToPosition(Math.max(hiLogMoList.size() - 1, 0));
            } else {
                recyclerView.getAdapter().notifyDataSetChanged();
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
            HiLogMo hiLogMo = hiLogMoList.get(position);
            holder.tag.setText(hiLogMo.getFlattened());
            holder.message.setText(hiLogMo.log);

            int highLightColor = getHighLightColor(hiLogMo.level);
            holder.tag.setTextColor(highLightColor);
            holder.message.setTextColor(highLightColor);
        }

        @Override
        public int getItemCount() {
            return hiLogMoList.size();
        }

        private int getHighLightColor(int level) {
            int highlightColor;
            switch (level) {
                case HiLogType.V:
                    highlightColor = 0xffbbbbbb;
                    break;
                case HiLogType.D:
                    highlightColor = 0xffffffff;
                    break;
                case HiLogType.I:
                    highlightColor = 0xff6a8759;
                    break;
                case HiLogType.W:
                    highlightColor = 0xffbbb529;
                    break;
                case HiLogType.E:
                    highlightColor = 0xffff6b68;
                    break;
                default:
                    highlightColor = 0xffffff00;
                    break;
            }
            return highlightColor;
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
