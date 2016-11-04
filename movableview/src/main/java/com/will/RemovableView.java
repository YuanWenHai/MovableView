package com.will;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/**
 * Created by will on 2016/10/13.
 * 一个简单的滑动组件，只可以有一个ChildView,此ChildView将可以被拖动，在本组件中滑动方向是left——right.
 *  <p>实现一些简单的弹性效果，释放时自动归位/移出屏幕</p>
 * <p>可以通过{@link #setAutoRemoveMultiplier(float)}设定释放时的移除临界值，默认为0.5,也就是View的一半距离</p>
 */

public class RemovableView extends LinearLayout {
    private ViewDragHelper mDragger;

    private View mContentView;

    private int originX,originY;

    private OnRemoveCallback mRemoveCallback;

    private float movedXDistance;

    private boolean moved;

    private boolean removed;

    private float lastX;

    private boolean disallowMove;


    private float touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    private float autoRemoveMultiplier = 0.5f;

    public RemovableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragger = ViewDragHelper.create(this, 1f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
        }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if(!disallowMove){
                    return left;
                }
                return 0;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                    int viewWidth = releasedChild.getWidth();
                    float releasedX = releasedChild.getX();
                    float releasedY = releasedChild.getY();
                    //向右滑动
                    if(releasedX > viewWidth * autoRemoveMultiplier ){
                        removed = true;
                        mDragger.settleCapturedViewAt(getWidth(),(int) releasedY);
                    //向左滑动
                    }else if( releasedX < -viewWidth * autoRemoveMultiplier){
                        removed = true;
                        mDragger.settleCapturedViewAt(-getWidth(),(int) releasedY);
                    //滑动距离不足临界值则复位
                    }else{
                        mDragger.settleCapturedViewAt(originX, originY);
                    }
                    invalidate();
            }

        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return  mDragger.shouldInterceptTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                movedXDistance = 0;
                moved = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float absX = Math.abs(event.getX()-lastX);
                movedXDistance += absX;
                //因为重写了onTouchEvent，所以整个layout的onClick将变得混乱，故做一些控制
                //若接受到移动手势，则取消super.onTouchEvent的执行(父类的onTouchEvent将执行onCLick等事件)
                if(!moved && movedXDistance > touchSlop){
                    moved = true;
                }
                //为横向滑动判定增加了touch slop的距离补差,提高操作体验
                if(movedXDistance >touchSlop && !disallowMove){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }else{
                    //当横向滑动距离少于touchSlop时，放弃
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
            if(!moved){
                super.onTouchEvent(event);
            }else{
                cancelLongPress();
            }
            mDragger.processTouchEvent(event);
            return true;
    }
    public void setAutoRemoveMultiplier(float multiplier){
        if(multiplier == 0){
            throw new IllegalArgumentException("The multiplier must not be zero!");
        }
        autoRemoveMultiplier = Math.min(Math.abs(multiplier),1f);
    }

    @Override
    public void computeScroll() {
        if (mDragger.continueSettling(true)) {
            invalidate();
            mDragger.getViewDragState();
        }else if (removed){
            if(mRemoveCallback != null){
                mRemoveCallback.onRemove(mContentView);
            }
            removed = false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        originX = mContentView.getLeft();
        originY = mContentView.getTop();
    }
    public void disallowMove(boolean which){
        disallowMove = which;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount()> 1){
            throw new IllegalArgumentException("The child count of RemovableView can only be one!");
        }
        mContentView = getChildAt(0);
    }
    public interface OnRemoveCallback {
        void onRemove(View removedView);
    }
    public void setOnRemoveCallback(OnRemoveCallback callback){
        mRemoveCallback = callback;
    }
}