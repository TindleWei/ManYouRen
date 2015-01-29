package com.manyouren.manyouren.widget.gestureimg;

import android.graphics.PointF;


public class ZoomAnimation implements Animation {

    private boolean mFirstFrame = true;

    private float mTouchX;
    private float mTouchY;

    private float mZoom;

    private float mStartX;
    private float mStartY;
    private float mStartScale;

    private float mXDiff;
    private float mYDiff;
    private float mScaleDiff;

    private long mAnimationLengthMS = 200;
    private long mTotalTime = 0;

    private ZoomAnimationListener zoomAnimationListener;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.polites.android.Animation#update(com.polites.android.GestureImageView
     * , long)
     */
    @Override
    public boolean update(GestureImageView view, long time) {
        if (mFirstFrame) {
            mFirstFrame = false;

            mStartX = view.getImageX();
            mStartY = view.getImageY();
            mStartScale = view.getScale();
            mScaleDiff = (mZoom * mStartScale) - mStartScale;

            if (mScaleDiff > 0) {
                // Calculate destination for midpoint
                VectorF vector = new VectorF();

                // Set the touch point as start because we want to move the end
                vector.setStart(new PointF(mTouchX, mTouchY));
                vector.setEnd(new PointF(mStartX, mStartY));

                vector.calculateAngle();

                // Get the current length
                float length = vector.calculateLength();

                // Multiply length by zoom to get the new length
                vector.length = length * mZoom;

                // Now deduce the new endpoint
                vector.calculateEndPoint();

                mXDiff = vector.end.x - mStartX;
                mYDiff = vector.end.y - mStartY;
            } else {
                // Zoom out to center
                mXDiff = view.getCenterX() - mStartX;
                mYDiff = view.getCenterY() - mStartY;
            }
        }

        mTotalTime += time;

        float ratio = (float) mTotalTime / (float) mAnimationLengthMS;

        if (ratio < 1) {

            if (ratio > 0) {
                // we still have time left
                float newScale = (ratio * mScaleDiff) + mStartScale;
                float newX = (ratio * mXDiff) + mStartX;
                float newY = (ratio * mYDiff) + mStartY;

                if (zoomAnimationListener != null) {
                    zoomAnimationListener.onZoom(newScale, newX, newY);
                }
            }

            return true;
        } else {

            float newScale = mScaleDiff + mStartScale;
            float newX = mXDiff + mStartX;
            float newY = mYDiff + mStartY;

            if (zoomAnimationListener != null) {
                zoomAnimationListener.onZoom(newScale, newX, newY);
                zoomAnimationListener.onComplete();
            }

            return false;
        }
    }

    public void reset() {
        mFirstFrame = true;
        mTotalTime = 0;
    }

    public void setZoom(float zoom) {
        this.mZoom = zoom;
    }

    public void setTouchX(float touchX) {
        this.mTouchX = touchX;
    }

    public void setTouchY(float touchY) {
        this.mTouchY = touchY;
    }

    public void setZoomAnimationListener(
            ZoomAnimationListener zoomAnimationListener) {
        this.zoomAnimationListener = zoomAnimationListener;
    }
}
