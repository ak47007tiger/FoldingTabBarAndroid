package com.foldingtabbarandroid.app.widget.foldingtabbar;

import android.animation.*;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.foldingtabbarandroid.app.R;

import java.util.ArrayList;

/**
 * Created by Mars on 2015/10/10.
 */
public class FoldingTabBar extends FrameLayout {
    View mMenuItemLeft;
    View mMenuItemRight;
    View mShowOrHideBtn;
    private Paint mPaint;
    RectF mRectF = new RectF();
    boolean mFolded = true;
    float mOverUnFold = 16f;
    OnClickListener mShowOrHideClickL = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mUnFoldingAnimator.isRunning() || mFoldingAnimator.isRunning()
                    || mFoldBoundAm.isRunning() || mUnFoldBoundAm.isRunning()) {
                return;
            }
            if (mFolded) {
                mShowOrHideBtn.animate().rotation(180 + 45);
                mUnFoldingAnimator.start();
            } else {
                mShowOrHideBtn.animate().rotation(0);
                mFoldingAnimator.start();
            }
            mFolded = !mFolded;
        }
    };
    ObjectAnimator mFoldingAnimator;
    ObjectAnimator mUnFoldingAnimator;
    private ObjectAnimator mUnFoldBoundAm;
    private ObjectAnimator mFoldBoundAm;
    ArrayList<View> mLeftTabItems = new ArrayList<View>();
    ArrayList<View> mRightTabItems = new ArrayList<View>();
    private int mRealHeight;
    private int mRealWidth;

    public FoldingTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#88d490"));
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mShowOrHideBtn = findViewById(R.id.flb_showorhide_btnid);
        mShowOrHideBtn.setOnClickListener(mShowOrHideClickL);
    }

    @Override
    protected void onSizeChanged(final int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRealWidth = w - getPaddingRight() - getPaddingLeft();
        mRealHeight = h - getPaddingTop() - getPaddingBottom();
        setTabWidth(mRealHeight);
        mFoldingAnimator = ObjectAnimator.ofFloat(this, "tabWidth", mRealWidth - mOverUnFold,
                mRealHeight);
        mFoldBoundAm = ObjectAnimator.ofFloat(this, "tabWidth", mRealHeight, mRealHeight -
                mOverUnFold);
        mFoldBoundAm.setRepeatCount(1);
        mFoldBoundAm.setRepeatMode(ValueAnimator.REVERSE);
        mFoldingAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                hideTabs(mLeftTabItems);
                hideTabs(mRightTabItems);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mFoldBoundAm.start();
            }
        });

        mUnFoldingAnimator = ObjectAnimator.ofFloat(this, "tabWidth", mRealHeight, mRealWidth -
                mOverUnFold);
//        mUnFoldingAnimator.setInterpolator(new UnFoldInterpolator((mOverUnFold + w) / w));
        mUnFoldBoundAm = ObjectAnimator.ofFloat(this, "tabWidth", mRealWidth - mOverUnFold,
                mRealWidth);
        mUnFoldBoundAm.setRepeatCount(1);
        mUnFoldBoundAm.setRepeatMode(ValueAnimator.REVERSE);
        mUnFoldingAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mUnFoldBoundAm.start();
                showTabs(mLeftTabItems);
                showTabs(mRightTabItems);
            }
        });
    }

    void hideTabs(ArrayList<View> tabs){
        for (int i = 0; i < tabs.size(); i++){
            View tabItem = tabs.get(i);
            tabItem.setAlpha(0);
            tabItem.setRotation(0);
            tabItem.setScaleX(0.1f);
            tabItem.setScaleY(0.1f);
        }
    }
    void showTabs(ArrayList<View> tabs){
        for (int i = 0; i < tabs.size(); i++){
            View tabItem = tabs.get(i);
            tabItem.animate().alpha(1).rotation(360).scaleX(1).scaleY(1);
        }
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mLeftTabItems.size() > 0){
            layoutLeftTabs();
        }
        if (mRightTabItems.size() > 0){
            layoutRightTabs();
        }
    }

    void layoutLeftTabs() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int minLeft = paddingLeft + mRealHeight / 2;
        int maxRight = (getWidth() - mRealHeight) / 2;
        int tabLength = maxRight - minLeft;
        int eachItemMaxWidth = tabLength / mLeftTabItems.size();
        int eachItemMaxHeight = mRealHeight;
        for (int i = 0; i < mLeftTabItems.size(); i++) {
            View child = mLeftTabItems.get(i);
            int left;
            int right;
            int top;
            int bottom;
            if (child.getMeasuredWidth() < eachItemMaxWidth) {
                left = minLeft + eachItemMaxHeight * i + (eachItemMaxWidth - child
                        .getMeasuredWidth()) / 2;
            } else {
                left = minLeft + eachItemMaxWidth * i;
            }
            /*if (left > (getWidth() - mRealHeight) / 2){
                break;
            }*/
            right = left + child.getMeasuredWidth();
            if (child.getMeasuredHeight() < eachItemMaxHeight) {
                top = (eachItemMaxHeight - child.getMeasuredHeight()) / 2;
            } else {
                top = paddingTop;
            }
            bottom = top + child.getMeasuredHeight();
            if (getHeight() - paddingBottom < bottom) {
                bottom = getHeight() - paddingBottom;
            }
            child.layout(left, top, right, bottom);
        }
    }

    void layoutRightTabs() {
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int minLeft = getWidth() / 2 + mRealHeight / 2;
        int maxRight = getWidth() - paddingRight - mRealHeight / 2;
        int tabLength = maxRight - minLeft;
        int eachItemMaxWidth = tabLength / mRightTabItems.size();
        int eachItemMaxHeight = mRealHeight;
        for (int i = 0; i < mRightTabItems.size(); i++) {
            View child = mRightTabItems.get(i);
            int left;
            int right;
            int top;
            int bottom;
            if (child.getMeasuredWidth() < eachItemMaxWidth) {
                left = minLeft + eachItemMaxHeight * i + (eachItemMaxWidth - child
                        .getMeasuredWidth()) / 2;
            } else {
                left = minLeft + eachItemMaxWidth * i;
            }
            /*if (left > (getWidth() - mRealHeight) / 2){
                break;
            }*/
            right = left + child.getMeasuredWidth();
            if (child.getMeasuredHeight() < eachItemMaxHeight) {
                top = (eachItemMaxHeight - child.getMeasuredHeight()) / 2;
            } else {
                top = paddingTop;
            }
            bottom = top + child.getMeasuredHeight();
            if (getHeight() - paddingBottom < bottom) {
                bottom = getHeight() - paddingBottom;
            }
            child.layout(left, top, right, bottom);
        }
    }

    public void setTabWidth(float width) {
        mRectF.set((getWidth() - width) / 2f, 0, (getWidth() + width) / 2f, getHeight());
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawRoundRect(mRectF, mRectF.height() / 2f, mRectF.width() / 2f, mPaint);
        super.dispatchDraw(canvas);
    }

    public void addTabLeft(View tabItem) {
        mLeftTabItems.add(tabItem);
        tabItem.setAlpha(0);
        addView(tabItem, 0);
    }

    public void addTabRight(View tabItem) {
        mRightTabItems.add(tabItem);
        tabItem.setAlpha(0);
        addView(tabItem, 0);
    }

    class UnFoldInterpolator implements TimeInterpolator {
        float mMaxRate = 1.05f;

        public UnFoldInterpolator(float maxRate) {
            mMaxRate = maxRate;
        }

        @Override
        public float getInterpolation(float input) {
            if (input <= 0.5f) {
                return input * 2f;
            } else if (input <= 0.75f) {
                return 1f + (input - 0.5f) * ((mMaxRate - 1f) / 0.25f);
            } else {
                return mMaxRate - (input - 0.75f) / 2.5f;
            }
        }
    }
}
