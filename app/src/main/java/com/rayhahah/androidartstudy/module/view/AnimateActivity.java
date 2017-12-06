package com.rayhahah.androidartstudy.module.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.rayhahah.androidartstudy.R;
import com.rayhahah.androidartstudy.widget.FlipboardView;
import com.rayhahah.androidartstudy.widget.ProgressView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AnimateActivity extends AppCompatActivity {

    private ImageView ivCover;
    private ProgressView progressView;
    private FlipboardView mFlipboardView;
    private FrameLayout flCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate);
        initView();
    }

    private void initView() {
        ivCover = (ImageView) findViewById(R.id.iv_cover);
        progressView = (ProgressView) findViewById(R.id.progress_view);
        mFlipboardView = (FlipboardView) findViewById(R.id.map_view);
        flCover = (FrameLayout) findViewById(R.id.fl_cover);
    }


    public void cilckViewPropertyAnimator(View view) {
        flCover.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.GONE);
        mFlipboardView.setVisibility(View.GONE);
        /**
         * 比ObjectAnimator更加高效，因为内部不是使用invalidate()来重绘的
         */
        ivCover.animate()
                .translationX(300)
                .translationY(500)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(2000);

    }

    public void cilckObjectAnimator(View view) {
        flCover.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);
        mFlipboardView.setVisibility(View.GONE);
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
         * 具体看方法内部
         */
        progressView.startAnim(4000, Color.parseColor("#FFFF00"), Color.parseColor("#db72c6"));
    }

    private Handler handler = new Handler();

    public void cilckFlipborad(View view) {
        flCover.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        mFlipboardView.setVisibility(View.VISIBLE);

        mFlipboardView.startAnim(4000,Color.parseColor("#FFFF00"), Color.parseColor("#db72c6"), new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mFlipboardView.reset();
                            }
                        });
                    }
                }, 500);
            }
        });
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Anim {
        String ALPHA = "alpha";
        String TRANSLATION_X = "translationX";
        String TRANSLATION_Y = "translationY";
        String X = "x";
        String Y = "Y";
        String ROTATION = "rotation";
        String ROTATION_X = "rotationX";
        String ROTATION_Y = "rotationY";
        String SCALE_X = "scaleX";
        String SCALE_Y = "scaleY";
    }

}
