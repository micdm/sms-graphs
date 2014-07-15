package com.micdm.smsgraphs.misc;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.micdm.smsgraphs.R;

public class PercentageView extends LinearLayout {

    private final RectF _coords = new RectF();
    private double _percentage;
    private final Paint _paint;

    public PercentageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _paint = getPaint(getColor(context, attrs));
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
        _percentage = percentage;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radius = getResources().getDimension(R.dimen.stats_percentage_corner_radius);
        _coords.left = -radius;
        _coords.right = (float) (canvas.getWidth() * _percentage);
        _coords.bottom = canvas.getHeight();
        canvas.drawRoundRect(_coords, radius, radius, _paint);
    }
}
