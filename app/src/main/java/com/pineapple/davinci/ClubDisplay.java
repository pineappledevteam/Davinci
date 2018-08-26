package com.pineapple.davinci;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
        */
public class ClubDisplay extends View {

    private float mFontSize;
    private GradientDrawable mGradient;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private String mClubName;
    private int[] mGradientColors = new int[2];



    public ClubDisplay(Context context, String name) {
        super(context);
        this.mClubName = name;
        init(null, 0);
    }

    public ClubDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ClubDisplay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ClubDisplay, defStyle, 0);

        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mFontSize = a.getDimension(
                R.styleable.ClubDisplay_fontSize,
                mFontSize);
        mGradientColors[0] = a.getColor(
                R.styleable.ClubDisplay_startColor,
                mGradientColors[0]);
        mGradientColors[1] = a.getColor(
                R.styleable.ClubDisplay_endColor,
                mGradientColors[1]);

        mGradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,mGradientColors);
        mGradient.setCornerRadius(15);
        mGradient.setCallback(this);


        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mFontSize);
        mTextPaint.setColor(Color.WHITE);
        mTextWidth = mTextPaint.measureText(mClubName);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
        canvas.drawText(mClubName,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);

        // Draw the example drawable on top of the text.
        if (mGradient != null) {
            mGradient.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mGradient.draw(canvas);
        }
        p.setShadowLayer(15,2,2,Color.parseColor("#20000000"));
        canvas.drawPaint(p);
    }

    Paint p = new Paint();
    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getClubName() {
        return mClubName;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param clubName The example string attribute value to use.
     */
    public void setClubName(String clubName) {
        mClubName = clubName;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the start color attribute value.
     *
     * @return The start color attribute value.
     */
    public int getStartColor() {
        return mGradientColors[0];
    }

    /**
     * Sets the view's gradient start color attribute value.
     *
     * @param startColor The gradient start color attribute value to use.
     */
    public void setStartColor(int startColor) {
        mGradientColors[0] = startColor;
        mGradient.setColors(mGradientColors);
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the end color attribute value.
     *
     * @return The end color attribute value.
     */
    public int getEndColor() {
        return mGradientColors[1];
    }

    /**
     * Sets the view's gradient end color attribute value.
     *
     * @param endColor The gradient end color attribute value to use.
     */
    public void setEndColor(int endColor) {
        mGradientColors[1] = endColor;
        mGradient.setColors(mGradientColors);
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the start and end color attribute value.
     *
     * @return The array of gradient colors attribute value.
     */
    public int[] getGradientColors() {
        return mGradientColors;
    }

    /**
     * Sets the view's gradient colors attribute value.
     *
     * @param colors The gradient colors attribute value to use.
     */
    public void setGradientColors(int[] colors) {
        mGradientColors = colors;
        mGradient.setColors(mGradientColors);
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getFontSize() {
        return mFontSize;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param fontSize The example dimension attribute value to use.
     */
    public void setFontSize(float fontSize) {
        mFontSize = fontSize;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the gradient drawable attribute value.
     *
     * @return The gradient drawable attribute value.
     */
    public GradientDrawable getGradientDrawable() {
        return mGradient;
    }

    /**
     * Sets the view's gradient drawable attribute value.
     *
     * @param gradient The gradient drawable attribute value to use.
     */
    public void setGradientDrawable(GradientDrawable gradient) {
        mGradient = gradient;
    }
}
