package com.micdm.smsgraphs.misc;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.micdm.smsgraphs.R;

public class PercentageView extends LinearLayout {

    private double percentage;
    private final Paint paint;

    public PercentageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = getPaint(getColor(context, attrs));
        setWillNotDraw(false);
    }

    private int getColor(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PercentageView, 0, 0);
        try {
            return a.getColor(R.styleable.PercentageView_color, 0);
        } finally {
            a.recycle();
        }
    }

    private Paint getPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        return paint;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, (int) (canvas.getWidth() * percentage), canvas.getHeight(), paint);
    }
}
