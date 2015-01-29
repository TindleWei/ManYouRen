package com.manyouren.manyouren.widget.gestureimg;

public class MoveAnimation implements Animation {

    private boolean mFirstFrame = true;

    private float mStartX;
    private float mStartY;

    private float mTargetX;
    private float mTargetY;
    private long mAnimationTimeMS = 100;
    private long mTotalTime = 0;

    private MoveAnimationListener mMoveAnimationListener;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.polites.android.Animation#update(com.polites.android.GestureImageView
     * , long)
     */
    @Override
    public boolean update(GestureImageView view, long time) {
        mTotalTime += time;

        if (mFirstFrame) {
            mFirstFrame = false;
            mStartX = view.getImageX();
            mStartY = view.getImageY();
        }

        if (mTotalTime < mAnimationTimeMS) {

            float ratio = (float) mTotalTime / mAnimationTimeMS;

            float newX = ((mTargetX - mStartX) * ratio) + mStartX;
            float newY = ((mTargetY - mStartY) * ratio) + mStartY;

            if (mMoveAnimationListener != null) {
                mMoveAnimationListener.onMove(newX, newY);
            }

            return true;
        } else {
            if (mMoveAnimationListener != null) {
                mMoveAnimationListener.onMove(mTargetX, mTargetY);
            }
        }

        return false;
    }

    public void reset() {
        mFirstFrame = true;
        mTotalTime = 0;
    }

    public float getTargetX() {
        return mTargetX;
    }

    public void setTargetX(float targetX) {
        this.mTargetX = targetX;
    }

    public float getTargetY() {
        return mTargetY;
    }

    public void setTargetY(float targetY) {
        this.mTargetY = targetY;
    }

    public long getAnimationTimeMS() {
        return mAnimationTimeMS;
    }

    public void setAnimationTimeMS(long animationTimeMS) {
        this.mAnimationTimeMS = animationTimeMS;
    }

    public void setMoveAnimationListener(
            MoveAnimationListener moveAnimationListener) {
        this.mMoveAnimationListener = moveAnimationListener;
    }
}
