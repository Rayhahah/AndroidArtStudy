package com.rayhahah.androidartstudy.module.textview.useful;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

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
 * @time 2017/11/28
 * @tips 这个类是Object的子类
 * @fuction
 */
public class AnimSpanUtil {


    private static int mTarRed;
    private static int mTarGreen;
    private static int mTarBlue;
    private static boolean mIsN2A = true;
    private static int[] mTextColor;
    private static int currentCount = 0;

    /**
     * 实现键盘敲击效果
     *
     * @param mText
     * @param spannableString
     * @param start
     * @param end
     * @param color
     */
    public static void animateTypeWriterSpan(final TextView mText, final SpannableString spannableString, final int start, final int end, int color) {
        mTextColor = int2RGB(color);
        initTypeWriter(start, spannableString, end, mText);
        startValueAnimator(500, ValueAnimator.INFINITE, new ValueAnimatorCallback() {
            @Override
            public void updateValue(float value) {
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.argb(((int) (255 * value)), mTextColor[0], mTextColor[1], mTextColor[2]));
                spannableString.setSpan(colorSpan, currentCount, currentCount + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                mText.setText(spannableString);

            }

            @Override
            public void animationRepeat(Animator animation, ValueAnimator valueAnimator) {
                currentCount++;
                if (currentCount == end) {
//                    initTypeWriter(start, spannableString, end, mText);
                    valueAnimator.cancel();
                } else {
                }
            }

            @Override
            public void animationStart(Animator animation) {

            }
        });


    }

    private static void initTypeWriter(int start, SpannableString spannableString, int end, TextView mText) {
        currentCount = start;
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.argb(((int) (0)), mTextColor[0], mTextColor[1], mTextColor[2]));
        spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mText.setText(spannableString);
    }

    /**
     * 字体颜色渐变动画
     *
     * @param mText
     * @param spannableString
     * @param start
     * @param end
     * @param color
     * @param colorActive
     */
    public static void animateColorSpan(final TextView mText, final SpannableString spannableString, final int start, final int end,
                                        int color, int colorActive) {
        final int[] cn = int2RGB(color);
        final int[] ca = int2RGB(colorActive);
        mTarRed = ca[0] - cn[0];
        mTarGreen = ca[1] - cn[1];
        mTarBlue = ca[2] - cn[2];

        startValueAnimator(1000, ValueAnimator.INFINITE, new ValueAnimatorCallback() {
            @Override
            public void updateValue(float value) {
                int rgbNA = Color.rgb(
                        ((int) (cn[0] + mTarRed * value)),
                        ((int) (cn[1] + mTarGreen * value)),
                        ((int) (cn[2] + mTarBlue * value))
                );
                int rgbAN = Color.rgb(
                        ((int) (ca[0] - mTarRed * value)),
                        ((int) (ca[1] - mTarGreen * value)),
                        ((int) (ca[2] - mTarBlue * value))
                );
                ForegroundColorSpan colorSpan = null;
                if (mIsN2A) {
                    colorSpan = new ForegroundColorSpan(rgbNA);
                } else {
                    colorSpan = new ForegroundColorSpan(rgbAN);
                }
                spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                mText.setText(spannableString);
            }

            @Override
            public void animationRepeat(Animator animation, ValueAnimator valueAnimator) {
                mIsN2A = !mIsN2A;
            }

            @Override
            public void animationStart(Animator animation) {

            }
        });
    }

    /**
     * 提取出颜色的rgb三色通道的值
     * 范围是0-255
     *
     * @param color
     * @return
     */
    private static int[] int2RGB(int color) {
        int[] rgb = new int[3];
        rgb[0] = (color & 0xff0000) >> 16;
        rgb[1] = (color & 0x00ff00) >> 8;
        rgb[2] = (color & 0x0000ff);
        return rgb;
    }

    /**
     * 开启动画引擎
     *
     * @param animatorCallback
     */
    private static void startValueAnimator(int duration, int repeatCount, final ValueAnimatorCallback animatorCallback) {
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(duration);
        valueAnimator.setRepeatCount(repeatCount);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatorCallback.updateValue((float) animation.getAnimatedValue());
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animatorCallback.animationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                animatorCallback.animationRepeat(animation, valueAnimator);
            }
        });
        valueAnimator.start();
    }


    interface ValueAnimatorCallback {
        void updateValue(float value);

        void animationRepeat(Animator animation, ValueAnimator valueAnimator);

        void animationStart(Animator animation);
    }

}
