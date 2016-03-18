package cn.finalteam.loadingviewfinal.sample.ui.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/8/1 上午11:32
 */
public class ChildViewPager extends AutoScrollViewPager {

    /**  下拉刷新  **/
    private final int DISTANCE_XY = 200;

    /** 触摸时按下的点 **/
    PointF downP = new PointF();
    /** 触摸时当前的点 **/
    PointF curP = new PointF();
    OnSingleTouchListener onSingleTouchListener;

    private boolean mInterceptTouch;

    public ChildViewPager(Context paramContext) {
        super(paramContext);
    }

    public ChildViewPager(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //当拦截触摸事件到达此位置的时候，返回true，
        //说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
        onViewPagerClick();
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        //每次进行onTouch事件都记录当前的按下的坐标

        if(arg0.getAction() == MotionEvent.ACTION_DOWN){
            //记录按下时候的坐标
            //切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
            curP.x = arg0.getX();
            curP.y = arg0.getY();
            mInterceptTouch = true;
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if(arg0.getAction() == MotionEvent.ACTION_MOVE){
            downP.x = arg0.getX();
            downP.y = arg0.getY();
            mInterceptTouch = true;
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if(arg0.getAction() == MotionEvent.ACTION_UP){
            //在up时判断是否按下和松手的坐标为一个点
            downP.x = arg0.getX();
            downP.y = arg0.getY();
            mInterceptTouch = false;

            //根据手机密度来区分touch 和 click
            float density = getResources().getDisplayMetrics().density + 2;
            float rx = downP.x - curP.x;
            float ry = downP.y - curP.y;
            //Logger.d("downP.x=" + downP.x + " downP.y=" + downP.y + "  curP.x=" + curP.x + " curP.y=" + curP.y + "  density=" + density);
            if( rx <= density && rx >= -density && ry <= density && ry >= -density){
                onSingleTouch(getCurrentItem());
                return super.onTouchEvent(arg0);
            }
            if(downP.y - curP.y>DISTANCE_XY){
                return super.onTouchEvent(arg0);
            }
        }

        return super.onTouchEvent(arg0);
    }


    public boolean isInterceptTouch() {
        return mInterceptTouch;
    }

    /**
     * 单击
     */
    private void onSingleTouch(int position) {
        if (onSingleTouchListener!= null) {
            onSingleTouchListener.onSingleTouch(position);
        }
    }

    /**
     * 创建点击事件接口
     * @author wanpg
     *
     */
    public interface OnSingleTouchListener {
        void onSingleTouch(int position);
    }

    public void setOnSingleTouchListener(OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }

    public OnViewPagerListener onViewPagerListener;
    public interface OnViewPagerListener{
        Object onViewPagerOnClick();
    }
    public void setOnViewPagerListener(OnViewPagerListener onViewPagerListener){
        this.onViewPagerListener = onViewPagerListener;
    }

    private Object onViewPagerClick(){
        if (onViewPagerListener!= null) {
            return onViewPagerListener.onViewPagerOnClick();
        }else{
            return null;
        }
    }


}
