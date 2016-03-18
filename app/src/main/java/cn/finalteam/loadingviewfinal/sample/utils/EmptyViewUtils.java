package cn.finalteam.loadingviewfinal.sample.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.finalteam.loadingviewfinal.sample.R;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/9 上午11:54
 */
public class EmptyViewUtils {

    public static void showLoading(ViewGroup emptyView) {
        if(emptyView == null) {
            return;
        }
        ProgressBar pbLoading = (ProgressBar) emptyView.findViewById(R.id.pb_loading);
        pbLoading.setVisibility(View.VISIBLE);
        TextView tvEmptyMsg = (TextView) emptyView.findViewById(R.id.tv_empty_message);
        tvEmptyMsg.setVisibility(View.GONE);
    }

    public static void showNetErrorEmpty(ViewGroup emptyView) {
        if(emptyView == null) {
            return;
        }
        ProgressBar pbLoading = (ProgressBar) emptyView.findViewById(R.id.pb_loading);
        pbLoading.setVisibility(View.GONE);
        TextView tvEmptyMsg = (TextView) emptyView.findViewById(R.id.tv_empty_message);
        tvEmptyMsg.setVisibility(View.VISIBLE);
        tvEmptyMsg.setText("网络异常~请检查你的网络无误后再重试");
    }

    public static void showNoDataEmpty(ViewGroup emptyView) {
        if(emptyView == null) {
            return;
        }
        ProgressBar pbLoading = (ProgressBar) emptyView.findViewById(R.id.pb_loading);
        pbLoading.setVisibility(View.GONE);
        TextView tvEmptyMsg = (TextView) emptyView.findViewById(R.id.tv_empty_message);
        tvEmptyMsg.setVisibility(View.VISIBLE);
        tvEmptyMsg.setText("^_^没有任何数据");
    }
}
