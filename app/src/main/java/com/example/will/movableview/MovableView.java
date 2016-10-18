package com.example.will.movableview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by will on 2016/10/18.
 */

public class MovableView extends FrameLayout {

    int lastX,lastY;


    int movedX,movedY;

    int originX,originY;

    private static final int HORIZONTAL = 0,VERTICAL = 1;

    private int mDirection = HORIZONTAL;
    private Scroller mScroller;
    public MovableView(Context context, AttributeSet attr){
        super(context,attr);
        mScroller = new Scroller(context);
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
                if(mDirection == HORIZONTAL){
                    movedX += (int)event.getX() - lastX;
                }else{
                    movedY += (int) event.getY() - lastY;
                }
                scrollTo(-movedX,-movedY);
                break;
            case MotionEvent.ACTION_UP:
                smoothScroll(0,0);
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
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        originX = getLeft();
        originY = getTop() - params.topMargin - getPaddingLeft();
    }
}
