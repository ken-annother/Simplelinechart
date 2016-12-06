package com.ugoutech.linechart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.ugoutech.linechart.utils.Utils;


/**
 * @描述: 标记视图
 * @项目名: charts
 * @包名: com.ugoutech.linechart.view
 * @类名:
 * @作者: soongkun
 * @创建时间: 2016/4/29 13:16
 */

public class MarkView {

    //往下绘制
    public static final int DOWN = 1;

    //往上绘制
    public static final int UP = 2;


    private String mLabeltext = "";


    private Paint rectPaint;
    private Paint textPaint;


    private float heightPaddingRation = 1.0f;
    private float widthPaddingRation = 0.6f;

    private Rect realRect;
    private Rect minRect;

    //朝向默认向下
    private int mDirection = UP;
    private int sWp;
    private int sHp;

    public MarkView(Context context) {
        init(context);
    }


    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(0xFFFF7700);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(Utils.convertSpToPixel(context, 12f));
        textPaint.setColor(0xFFFFFFFF);

        minRect = new Rect();
    }

    /**
     * 设置标签的内容
     *
     * @param label
     */
    public void setLabel(String label) {
        mLabeltext = label;
        textPaint.getTextBounds(label, 0, label.length(), minRect);

        sWp = (int) (minRect.width() * widthPaddingRation / 2);
        sHp = (int) (minRect.height() * heightPaddingRation / 2);
        realRect = new Rect(minRect.left - sWp, minRect.top - sHp, minRect.right + sWp, minRect.bottom + sHp);

    }


    /**
     * 在指定的位置上画图
     *
     * @param canvas
     * @param posX
     * @param posY
     */
    public void draw(Canvas canvas, float posX, float posY) {
        canvas.save();
        canvas.translate(posX, posY);

        //画矩形框
        drawDialogRect(canvas);

        canvas.drawText(mLabeltext, sWp, realRect.height() - sHp, textPaint);

        canvas.restore();
    }

    /**
     * 画矩形框
     *
     * @param canvas
     */
    private void drawDialogRect(Canvas canvas) {
        canvas.drawRect(0, 0, realRect.width(), realRect.height(), rectPaint);
    }


    /**
     * 设置标记视图的朝向
     *
     * @param direction
     */
    public void setDirection(int direction) {
        this.mDirection = direction;
    }


    /**
     * 重画
     */
    public void refreshContent() {

    }

    public int getWidth() {
        return realRect.width();
    }

    public int getHeight() {
        return realRect.height();
    }
}
