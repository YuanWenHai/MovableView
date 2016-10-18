package com.example.will.movableview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by will on 2016/10/18.
 */

public class MovableView extends FrameLayout {

    private int lastX,lastY;


    private int movedX,movedY;

    public static final int HORIZONTAL = 0,VERTICAL = 1;

    private int touchSlop;

    private boolean moved;

    private int mOrientation = HORIZONTAL;

    private float autoRemoveMultiplier = 0.5f;

    private ContentRemoveCallback mRemoveCallback;

    private boolean removed;
    private Scroller mScroller;
    public MovableView(Context context, AttributeSet attr){
        super(context,attr);
        mScroller = new Scroller(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!removed){
                    if(mOrientation == HORIZONTAL){
                        movedX += (int)event.getX() - lastX;
                        if(Math.abs(movedX) > touchSlop){
                            scrollTo(-movedX,-movedY);
                            requestDisallowInterceptTouchEvent(true);
                            moved = true;
                        }
                    }else{
                        movedY += (int) event.getY() - lastY;
                        if(Math.abs(movedY) > touchSlop){
                            scrollTo(-movedX,-movedY);
                            requestDisallowInterceptTouchEvent(true);
                            moved = true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                smoothScroll(0,0);
                requestDisallowInterceptTouchEvent(false);
                if(!moved){
                    performClick();
                }else{
                    performMovement();
                }
                movedX = 0;
                movedY = 0;
        }
        lastX = (int)event.getX();
        lastY = (int) event.getY();
        return true;
    }
    private void smoothScroll(int desX,int desY){
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int deltaX = desX - scrollX;
        int deltaY = desY - scrollY;
        mScroller.startScroll(scrollX,scrollY,deltaX,deltaY,500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }else if(removed && mRemoveCallback != null){
            mRemoveCallback.onRemove();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
    public void setScrollOrientation(int orientation){
        if(orientation > 1 || orientation < 0){
            mOrientation = HORIZONTAL;
        }else{
            mOrientation = orientation;
        }
    }
    private void performMovement(){
        if(mOrientation == HORIZONTAL){
            if(movedX > getWidth()*autoRemoveMultiplier) {
                smoothScroll(-getWidth(), 0);
                removed = true;
            }else if(movedX < -getWidth()*autoRemoveMultiplier ){
                smoothScroll(getWidth(),0);
                removed = true;
            }else{
                smoothScroll(0,0);
            }
        }
    }
    public void setOnRemoveCallback(ContentRemoveCallback callback){
        mRemoveCallback = callback;
    }
    public interface ContentRemoveCallback{
        void onRemove();
    }
}
