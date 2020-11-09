package com.marte5.beautifulvino.Utility;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ScrollView;

public class ExtendedScrollView extends ScrollView {

    private View inner;// View

    private float y;// Click the Y coordinate

    private Rect normal = new Rect();// Rectangular (here is just a form, is used for judging whether to animation.)

    private boolean isCount = false;// Whether to start the calculation

    private ImageView imageView;

    private int initbottom;// Initial height
    private int bottom;// Drag the constant height.

    public void setImageView(ImageView imageView) {
        this.inner=imageView;
        this.imageView = imageView;
    }

    private OnDetectScrollListener onDetectScrollListener;

    public ExtendedScrollView(Context context) {
        super(context);
    }

    public ExtendedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendedScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrollChanged(int w, int h, int ow, int oh) {
        if (onDetectScrollListener!=null) {
            if (h < oh) {
                onDetectScrollListener.onDownScrolling();
            } else if (h > oh) {
                onDetectScrollListener.onUpScrolling();
            }
        }
    }

    public void setOnDetectScrollListener(OnDetectScrollListener onDetectScrollListener) {
        this.onDetectScrollListener = onDetectScrollListener;
    }

    public void animation() {
        TranslateAnimation taa = new TranslateAnimation(0, 0, imageView.getTop() + 200,
                imageView.getTop() + 200);
        taa.setDuration(200);
        imageView.startAnimation(taa);
        imageView.layout(imageView.getLeft(), imageView.getTop(), imageView.getRight(),
                initbottom);

        // Open mobile animation
        TranslateAnimation ta = new TranslateAnimation(0, 0, imageView.getTop(),
                normal.top);
        ta.setDuration(200);
        inner.startAnimation(ta);
        // Set back to normal position
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();

        isCount = false;
        y = 0;// The finger will be 0

    }

    // The need to open the animation
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (inner != null) {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                bottom = initbottom = imageView.getBottom();
                break;

            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    animation();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                final float preY = y;// Y coordinates when pressed
                float nowY = ev.getY();// All the Y coordinates
                int deltaY = (int) (nowY - preY);// Sliding distance
                if (!isCount) {
                    deltaY = 0; // To be here at 0
                }

                if (deltaY <0)
                    return;

                // When rolling up to the top or the time will not be rolling, then moving layout
             //   isNeedMove();

                    // Initialized rectangular head
                    if (normal.isEmpty()) {
                        // Save layout in normal position
                        normal.set(inner.getLeft(), inner.getTop(),inner.getRight(), inner.getBottom());
                    }

                    // Mobile layout
                    inner.layout(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom() + deltaY / 3);

                    bottom += (deltaY / 6);
                    imageView.layout(imageView.getLeft(), imageView.getTop(), imageView.getRight(), bottom);


                isCount = true;
                y = nowY;
                break;

            default:
                break;

        }
    }

    public interface OnDetectScrollListener {

        void onUpScrolling();

        void onDownScrolling();
    }
}