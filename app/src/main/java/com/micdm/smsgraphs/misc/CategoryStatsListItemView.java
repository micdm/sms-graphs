package com.micdm.smsgraphs.misc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.micdm.smsgraphs.R;

public class CategoryStatsListItemView extends LinearLayout {

    private double percentage;
    private final Paint paint;

    public CategoryStatsListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = getPaint();
        setWillNotDraw(false);
    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.stats_percentage_background));
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
