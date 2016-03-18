package cn.finalteam.loadingviewfinal;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.loadingviewfinal.loadingview.R;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/10 下午5:10
 */
public class NestedScrollViewFinal extends NestedScrollView implements OnScrollBottomListener{
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

    private boolean mAddLoadMoreFooterFlag;

    private List<View> mFooterViewTempList = new ArrayList<>();
    private List<View> mHeaderViewTempList = new ArrayList<>();

    private LinearLayout mSvContentView;

    /**
     * 没有更多了是否隐藏loadmoreview
     */
    private boolean mNoLoadMoreHideView;

    public NestedScrollViewFinal(Context context) {
        super(context);
        init(context, null);
    }

    public NestedScrollViewFinal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NestedScrollViewFinal(Context context, AttributeSet attrs, int defStyleAttr) {
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

        mLoadMoreView.getFooterView().setOnClickListener(new OnMoreViewClickListener());
        a.recycle();
        setHasLoadMore(false);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (t + getHeight() >= computeVerticalScrollRange()) {
            //ScrollView滑动到底部了
            onScorllBootom();
        }
    }

    @Override
    public void onScorllBootom() {
        if(mHasLoadMore && mLoadMoreMode == LoadMoreMode.SCROLL) {
            executeLoadMore();
        }
    }

    /**
     * 设置LoadMoreView
     * @param loadMoreView
     */
    public void setLoadMoreView(ILoadMoreView loadMoreView) {
        if (mLoadMoreView != null) {
            try {
                removeFooterView(mLoadMoreView.getFooterView());
                mAddLoadMoreFooterFlag = false;
            } catch (Exception e){}
        }
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
            if(mNoLoadMoreHideView) {
                mAddLoadMoreFooterFlag = false;
                removeFooterView(mLoadMoreView.getFooterView());
            }
        } else {
            if(!mAddLoadMoreFooterFlag) {
                mAddLoadMoreFooterFlag = true;
                addFooterView(mLoadMoreView.getFooterView());
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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = getChildAt(0);
        if ( view instanceof LinearLayout) {
            mSvContentView = (LinearLayout) view;
            for(int i = 0; i < mHeaderViewTempList.size(); i++) {
                View headerView = mHeaderViewTempList.get(i);
                mSvContentView.addView(headerView, i, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }

            for(int i = 0; i < mFooterViewTempList.size(); i++) {
                View footerView = mFooterViewTempList.get(i);
                mSvContentView.addView(footerView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
        } else {
            throw new IllegalStateException("ScrollView child view must be a LinearLayout");
        }
    }

    public void addFooterView(View view) {
        mFooterViewTempList.add(view);
        if(mSvContentView != null) {
            int childCount = mSvContentView.getChildCount();
            mSvContentView.addView(view, childCount);
        }
    }

    public void removeFooterView(View view) {
        mFooterViewTempList.remove(view);
        if(mSvContentView != null) {
            mSvContentView.removeView(view);
        }
    }

    public void addHeaderView(View view) {
        mHeaderViewTempList.add(view);
        if (mSvContentView != null) {
            mSvContentView.addView(view, mHeaderViewTempList.size());
        }
    }

    public void removeHeaderView(View view) {
        mHeaderViewTempList.remove(view);
        if (mSvContentView != null) {
            mSvContentView.removeView(view);
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
}
