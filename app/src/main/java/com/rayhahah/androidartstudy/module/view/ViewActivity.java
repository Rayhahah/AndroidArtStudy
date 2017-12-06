package com.rayhahah.androidartstudy.module.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.rayhahah.androidartstudy.R;

public class ViewActivity extends AppCompatActivity {

    private TextView mStudyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        mStudyView = ((TextView) findViewById(R.id.study_view));
        mStudyView.post(new Runnable() {
            /**
             * 这里可以确保获取到宽高
             */
            @Override
            public void run() {

            }
        });
        ViewTreeObserver treeObserver = mStudyView.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            /**
             * 这里可以确保获取到宽高
             * 注意要移除监听
             */
            @Override
            public void onGlobalLayout() {
                mStudyView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }


    /**
     * 可以获取到View 的宽高
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    public void clickToScroll(View view) {
        startActivity(new Intent(this, ScrollActivity.class));
    }

    public void clickToTranslate(View view) {
        startActivity(new Intent(this, CanvasActivity.class));

    }

    public void clickToAnimate(View view) {
        startActivity(new Intent(this, AnimateActivity.class));
    }
}
