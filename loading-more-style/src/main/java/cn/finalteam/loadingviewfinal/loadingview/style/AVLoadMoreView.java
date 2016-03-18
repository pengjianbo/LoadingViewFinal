package cn.finalteam.loadingviewfinal.loadingview.style;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.finalteam.loadingviewfinal.ILoadMoreView;


/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/10 下午6:14
 */
public class AVLoadMoreView extends RelativeLayout implements ILoadMoreView {
    private TextView mTvMessage;
    private AVLoadingIndicatorView mAviLoading;

    public AVLoadMoreView(Context context) {
        super(context);
        init(context);
    }

    public AVLoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AVLoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.loading_view_final_footer_style, this);;
        mAviLoading = (AVLoadingIndicatorView) findViewById(R.id.av_loading_indicator);
        mTvMessage = (TextView) findViewById(R.id.tv_loading_msg);
    }

    public void setIndicatorId(@AVLoadingIndicatorView.Indicator int indicatorId){
        mAviLoading.setIndicatorId(indicatorId);
    }

    public void setIndicatorColor(@ColorInt int indicatorColor){
        mAviLoading.setIndicatorColor(indicatorColor);
    }

    @Override
    public void showNormal() {
        mAviLoading.setVisibility(View.GONE);
        mTvMessage.setVisibility(View.VISIBLE);
        mTvMessage.setText("点击加载更多");
    }

    @Override
    public void showNoMore() {
        mAviLoading.setVisibility(View.GONE);
        mTvMessage.setVisibility(View.VISIBLE);
        mTvMessage.setText("没有更多了");
    }

    @Override
    public void showLoading() {
        mAviLoading.setVisibility(View.VISIBLE);
        mTvMessage.setVisibility(View.GONE);
        mTvMessage.setText("加载中…");
    }

    @Override
    public void showFail() {
        mAviLoading.setVisibility(View.GONE);
        mTvMessage.setVisibility(View.VISIBLE);
        mTvMessage.setText("网路异常，点击重试");
    }

    @Override
    public View getFooterView() {
        return this;
    }
}
