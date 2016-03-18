package cn.finalteam.loadingviewfinal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.loadingviewfinal.srl.R;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/11 上午10:57
 */
public class SwipeRefreshLayoutFinal extends SwipeRefreshLayout {
    private OnRefreshListener mOnRefreshListener;

    private Handler mHandler = new Handler();
    private List<View> mSwipeableScrollChildren = new ArrayList<>();
    private int mTouchSlop;
    private float mPrevX;
    // Indicate if we've already declined the move event
    private boolean mDeclined;

    public SwipeRefreshLayoutFinal(Context context) {
        super(context);

        init(context, null);
    }

    public SwipeRefreshLayoutFinal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeRefreshLayoutFinal);
        if(a.hasValue(R.styleable.SwipeRefreshLayoutFinal_refreshLoadingColor)) {
            int color = a.getColor(R.styleable.SwipeRefreshLayoutFinal_refreshLoadingColor, Color.BLACK);
            setColorSchemeColors(color);
        }
        a.recycle();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        super.setOnRefreshListener(listener);
        this.mOnRefreshListener = listener;
    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onRefresh();
                }
            }
        }, 200);
    }

    public void onRefreshComplete() {
        setRefreshing(false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getViewGroup(this);
    }

    public void getViewGroup(ViewGroup viewGroup ) {
        for(int i = 0 ; i < viewGroup.getChildCount(); i++){
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                if (view instanceof ScrollView || view instanceof ListView
                        || view instanceof GridView || view instanceof RecyclerView) {
                    mSwipeableScrollChildren.add(view);
                } else {
                    getViewGroup((ViewGroup) view);
                }
            }
        }
    }

    @Override
    public boolean canChildScrollUp() {
        // check if any supplied swipeable children can scroll up
        for (View view : mSwipeableScrollChildren) {
            if (view.isShown() && ViewCompat.canScrollVertically(view, -1)) {
                // prevent refresh gesture
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                mDeclined = false; // New action
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);

                if (mDeclined || xDiff > mTouchSlop) {
                    mDeclined = true; // Memorize
                    return false;
                }
        }

        return super.onInterceptTouchEvent(event);
    }
}
