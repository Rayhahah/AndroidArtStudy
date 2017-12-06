package com.rayhahah.androidartstudy.widget;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.rayhahah.androidartstudy.util.anim.HsvEvaluator;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 *
 * @author Rayhahah
 * @blog http://rayhahah.com
 * @time 2017/12/5
 * @tips 这个类是Object的子类
 * @fuction
 */
public class ProgressView extends View {

    public static final int STROKE_WIDTH = 20;
    private int color = Color.RED;
    private Paint mPaint;
    private float progress = 0;
    private Paint mTextPaint;

    public static final String PROGRESS = "progress";
    public static final String COLOR = "color";

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(STROKE_WIDTH);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防抖动
        /**
         * 线头形状有三种：BUTT 平头、ROUND 圆头、SQUARE 方头。默认为 BUTT。
         */
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        /**
         *设置拐角的形状：MITER 尖角、 BEVEL 平角和 ROUND 圆角。默认为 MITER。
         */
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setColor(color);

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);//抗锯齿
        mTextPaint.setDither(true);//防抖动
        /**
         * 线头形状有三种：BUTT 平头、ROUND 圆头、SQUARE 方头。默认为 BUTT。
         */
        mTextPaint.setStrokeCap(Paint.Cap.ROUND);
        /**
         *设置拐角的形状：MITER 尖角、 BEVEL 平角和 ROUND 圆角。默认为 MITER。
         */
        mTextPaint.setStrokeJoin(Paint.Join.ROUND);
        mTextPaint.setColor(color);
        mTextPaint.setTextSize(30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(color);
        mTextPaint.setColor(color);
        int width = getWidth();
        int height = getHeight();
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        int radius = Math.min(width, height) / 2 - STROKE_WIDTH / 2;
//        canvas.drawCircle(width / 2, height / 2, radius, mPaint);
        RectF oval = new RectF(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius);
        canvas.drawArc(oval, 90, progress * 3.6f, false, mPaint);

        /**
         * 绘制位子
         */
        String text = ((int) progress) + "%";
        Rect textBouns = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), textBouns);
        canvas.drawText(text, (width - textBouns.width()) / 2, (height + textBouns.height()) / 2, mTextPaint);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        postInvalidate();
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        postInvalidate();
    }

    public void startAnim(int duration, int fromColor, int toColor) {
        /**
         * 是通过输入的变量名，在目标内部去生成getter和setter来设置赋值和改变的
         * "progress" : getProgress(), setProgress(),
         * 并且setProgress()加入invalidate()来表明重绘
         *
         * ArgbEvaluator是Android自带的，是机械的颜色渐变
         *
         * HSV ： 色相、饱和度、明度（Hue、Saturation、Value）
         * HSL : 色相、饱和度、亮度（Hue、Saturation、Lightness）
         * 这两种颜色渐变的规则更加符合人的审美，需要自行实现
         *
         */
        PropertyValuesHolder progressValuesHolder = PropertyValuesHolder.ofFloat(ProgressView.PROGRESS, 0, 100);
//        PropertyValuesHolder colorValuesHolder = PropertyValuesHolder.ofObject(ProgressView.COLOR, new ArgbEvaluator(),
//                Color.parseColor("#FF0000"), Color.parseColor("#db72c6"));
        PropertyValuesHolder colorValuesHolder = PropertyValuesHolder.ofInt(ProgressView.COLOR,
                fromColor,toColor );
        colorValuesHolder.setEvaluator(new HsvEvaluator());
        ObjectAnimator oaProgressColor = ObjectAnimator.ofPropertyValuesHolder(this, progressValuesHolder, colorValuesHolder);
        oaProgressColor.setDuration(duration);
        oaProgressColor.setInterpolator(new AccelerateDecelerateInterpolator());
        oaProgressColor.start();
    }
}
