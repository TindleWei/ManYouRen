package com.manyouren.manyouren.widget.gestureimg;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

class FlingListener extends SimpleOnGestureListener {

    private float mVelocityX;
    private float mVelocityY;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        this.mVelocityX = velocityX;
        this.mVelocityY = velocityY;
        return true;
    }

    public float getVelocityX() {
        return mVelocityX;
    }

    public float getVelocityY() {
        return mVelocityY;
    }
}
