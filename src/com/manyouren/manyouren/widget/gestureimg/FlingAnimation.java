package com.manyouren.manyouren.widget.gestureimg;


public class FlingAnimation implements Animation {

    private float mVelocityX;
    private float mVelocityY;

    private float mFactor = 0.85f;

    private FlingAnimationListener listener;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.polites.android.Transformer#update(com.polites.android.GestureImageView
     * , long)
     */
    @Override
    public boolean update(GestureImageView view, long time) {
        float seconds = (float) time / 1000.0f;

        float dx = mVelocityX * seconds;
        float dy = mVelocityY * seconds;

        mVelocityX *= mFactor;
        mVelocityY *= mFactor;

        float threshold = 10;
        boolean active = (Math.abs(mVelocityX) > threshold && Math
                .abs(mVelocityY) > threshold);

        if (listener != null) {
            listener.onMove(dx, dy);

            if (!active) {
                listener.onComplete();
            }
        }

        return active;
    }

    public void setVelocityX(float velocityX) {
        this.mVelocityX = velocityX;
    }

    public void setVelocityY(float velocityY) {
        this.mVelocityY = velocityY;
    }

    public void setFactor(float factor) {
        this.mFactor = factor;
    }

    public void setListener(FlingAnimationListener listener) {
        this.listener = listener;
    }
}
