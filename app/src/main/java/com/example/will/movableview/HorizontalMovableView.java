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

public class HorizontalMovableView extends FrameLayout {

    private int lastX;

    private int movedX;

    private int touchSlop;

    private boolean moved;


    private float autoRemoveMultiplier = 0.5f;

    private ContentRemoveCallback mRemoveCallback;

    private boolean removed;

    private Scroller mScroller;
    public HorizontalMovableView(Context context, AttributeSet attr){
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
                    movedX += (int)event.getX() - lastX;
                    if(moved){
                        scrollTo(-movedX,0);
                    }else if (Math.abs(movedX) > touchSlop ){
                        scrollTo(-movedX,0);
                        requestDisallowInterceptTouchEvent(true);
                        moved = true;
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
                moved = false;
                movedX = 0;
        }
        lastX = (int)event.getX();
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
            removed = false;
            scrollTo(0,0);
        }
    }


    private void performMovement(){
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
    public void setAutoRemoveMultiplier(float f){
        autoRemoveMultiplier = f;
    }
    public void setOnRemoveCallback(ContentRemoveCallback callback){
        mRemoveCallback = callback;
    }
    public interface ContentRemoveCallback{
        void onRemove();
    }
}
