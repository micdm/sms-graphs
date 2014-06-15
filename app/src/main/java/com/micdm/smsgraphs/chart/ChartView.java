package com.micdm.smsgraphs.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.math.BigDecimal;
import java.util.List;

public class ChartView extends View {

    private Chart chart;

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setChart(Chart chart) {
        this.chart = chart;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        List<ChartElementGroup> groups = chart.getGroups();
        float radius = Math.min(canvas.getClipBounds().width(), canvas.getClipBounds().height());
        for (int i = 0; i < groups.size(); i += 1) {
            drawChartElementGroup(canvas, groups.get(i), radius / (i + 1));
        }
    }

    private void drawChartElementGroup(Canvas canvas, ChartElementGroup group, float radius) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        float angle = 0;
        BigDecimal total = group.getTotalValue();
        for (ChartElement element: group.getElements()) {
            float sweep = element.getValue().divide(total, 5, BigDecimal.ROUND_HALF_UP).floatValue() * 360;
            paint.setColor(0xFF000000 + (int)(Math.random() * 0xFFFFFF));
            canvas.drawArc(new RectF(0, 0, radius, radius), angle, sweep - 0.5f, true, paint);
            angle += sweep;
        }
    }
}
