package cn.finalteam.loadingviewfinal;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Constructor;

import cn.finalteam.loadingviewfinal.loadingview.R;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/7 上午11:34
 */
public class ListViewFinal extends ListView implements OnScrollBottomListener{

    /**
     * 加载更多UI
     */
    ILoadMoreView mLoadMoreView;

    /**
     * 加载更多方式，默认滑动到底部加载更多
     */
    LoadMoreMode mLoadMoreMode = LoadMoreMode.SCROLL;
    /**
     * 加载更多lock
     */
    private boolean mLoadMoreLock;
    /**
     * 是否可以加载跟多
     */
    boolean mHasLoadMore = true;
    /**
     * 是否加载失败
     */
    private boolean mHasLoadFail;

    /**
     * 加载更多事件回调
     */
    private OnLoadMoreListener mOnLoadMoreListener;

    /**
     * 没有更多了是否隐藏loadmoreview
     */
    private boolean mNoLoadMoreHideView;

    private boolean mAddLoadMoreFooterFlag;

    private int mLoadMoreViewBottom,mLoadMoreViewTop,mLoadMoreViewLeft,mLoadMoreViewRight;
    private boolean mHasLoadMoreViewShowState;

    public ListViewFinal(Context context) {
        super(context);
        init(context, null);
    }

    public ListViewFinal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ListViewFinal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingViewFinal);

        if (a.hasValue(R.styleable.LoadingViewFinal_loadMoreMode)) {
            mLoadMoreMode = LoadMoreMode.mapIntToValue(a.getInt(R.styleable.LoadingViewFinal_loadMoreMode, 0x01));
        } else {
            mLoadMoreMode = LoadMoreMode.SCROLL;
        }

        if (a.hasValue(R.styleable.LoadingViewFinal_noLoadMoreHideView)) {
            mNoLoadMoreHideView = a.getBoolean(R.styleable.LoadingViewFinal_noLoadMoreHideView, false);
        } else {
            mNoLoadMoreHideView = false;
        }

        if (a.hasValue(R.styleable.LoadingViewFinal_loadMoreView)) {
            try {
                String loadMoreViewName = a.getString(R.styleable.LoadingViewFinal_loadMoreView);
                Class clazz = Class.forName(loadMoreViewName);
                Constructor c = clazz.getConstructor(Context.class);
                ILoadMoreView loadMoreView = (ILoadMoreView) c.newInstance(context);
                mLoadMoreView = loadMoreView;
            } catch (Exception e) {
                e.printStackTrace();
                mLoadMoreView = new DefaultLoadMoreView(context);
            }
        } else {
            mLoadMoreView = new DefaultLoadMoreView(context);
        }

        setOnScrollListener(new ListViewOnScrollListener());
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        showNoMoreUI();
        hideLoadMoreView();
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (!mAddLoadMoreFooterFlag) {
            mAddLoadMoreFooterFlag = true;
            addFooterView(mLoadMoreView.getFooterView());
        }
        super.setAdapter(adapter);
        if( adapter instanceof BaseAdapter) {
            try {
                adapter.unregisterDataSetObserver(mDataObserver);
            } catch (Exception e){}
            adapter.registerDataSetObserver(mDataObserver);
        }
    }

    @Override
    public void onScorllBootom() {
        if(mHasLoadMore && mLoadMoreMode == LoadMoreMode.SCROLL) {
            executeLoadMore();
        }
    }

    /**
     * 设置LoadMoreView(需要在setAdapter之前)
     * @param loadMoreView
     */
    public void setLoadMoreView(ILoadMoreView loadMoreView) {
//        if (mLoadMoreView != null) {
//            try {
//                removeFooterView(mLoadMoreView.getFooterView());
//                mAddLoadMoreFooterFlag = false;
//            } catch (Exception e){}
//        }

        mLoadMoreView = loadMoreView;
        mLoadMoreView.getFooterView().setOnClickListener(new OnMoreViewClickListener());
    }

    /**
     * 设置加载更多模式
     * @param mode
     */
    public void setLoadMoreMode(LoadMoreMode mode) {
        mLoadMoreMode = mode;
    }

    /**
     * 设置没有更多数据了，是否隐藏fooler view
     * @param hide
     */
    public void setNoLoadMoreHideView(boolean hide) {
        this.mNoLoadMoreHideView = hide;
    }

    /**
     * 没有很多了
     */
    void showNoMoreUI() {
        mLoadMoreLock = false;
        mLoadMoreView.showNoMore();
    }

    /**
     * 显示失败UI
     */
    public void showFailUI() {
        mHasLoadFail = true;
        mLoadMoreLock = false;
        mLoadMoreView.showFail();
    }

    /**
     * 显示默认UI
     */
    void showNormalUI() {
        mLoadMoreLock = false;
        mLoadMoreView.showNormal();
    }

    /**
     * 显示加载中UI
     */
    void showLoadingUI(){
        mHasLoadFail = false;
        mLoadMoreView.showLoading();
    }

    /**
     * 是否有更多
     * @param hasLoadMore
     */
    public void setHasLoadMore(boolean hasLoadMore) {
        mHasLoadMore = hasLoadMore;
        if (!mHasLoadMore) {
            showNoMoreUI();
            if(mNoLoadMoreHideView && mHasLoadMoreViewShowState) {
                hideLoadMoreView();
            }
        } else {
            if(!mHasLoadMoreViewShowState) {
                showLoadMoreView();
            }
            showNormalUI();
        }
    }

    /**
     * 设置加载更多事件回调
     * @param loadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.mOnLoadMoreListener = loadMoreListener;
    }

    /**
     * 完成加载更多
     */
    public void onLoadMoreComplete() {
        if (mHasLoadFail) {
            showFailUI();
        } else if (mHasLoadMore) {
            showNormalUI();
        }
    }

    /**
     * 点击more view加载更多
     */
    class OnMoreViewClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if(mHasLoadMore) {
                executeLoadMore();
            }
        }
    }

    /**
     * 加载更多
     */
    void executeLoadMore() {
        if(!mLoadMoreLock && mHasLoadMore) {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.loadMore();
            }
            mLoadMoreLock = true;//上锁
            showLoadingUI();
        }
    }

    private void hideLoadMoreView() {
        mHasLoadMoreViewShowState = false;
        mLoadMoreViewBottom = mLoadMoreView.getFooterView().getPaddingBottom();
        mLoadMoreViewTop = mLoadMoreView.getFooterView().getPaddingTop();
        mLoadMoreViewLeft = mLoadMoreView.getFooterView().getPaddingLeft();
        mLoadMoreViewRight = mLoadMoreView.getFooterView().getPaddingRight();
        mLoadMoreView.getFooterView().setVisibility(View.GONE);
        mLoadMoreView.getFooterView().setPadding(0, -mLoadMoreView.getFooterView().getHeight(), 0, 0);
    }

    private void showLoadMoreView(){
        mHasLoadMoreViewShowState = true;
        mLoadMoreView.getFooterView().setVisibility(View.VISIBLE);
        mLoadMoreView.getFooterView().setPadding(mLoadMoreViewLeft, mLoadMoreViewTop, mLoadMoreViewRight, mLoadMoreViewBottom);
    }

    /**
     * 滚动到底部自动加载更多数据
     */
    private class ListViewOnScrollListener implements OnScrollListener{

        @Override
        public void onScrollStateChanged(AbsListView listView, int scrollState) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    && listView.getLastVisiblePosition() + 1 == listView.getCount()) {// 如果滚动到最后一行
                onScorllBootom();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }

    /**
     * 刷新数据时停止滑动,避免出现数组下标越界问题
     */
    private DataSetObserver mDataObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
        }

        @Override
        public void onInvalidated() {
        }
    };


}
