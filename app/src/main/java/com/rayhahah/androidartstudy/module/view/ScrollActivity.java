package com.rayhahah.androidartstudy.module.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rayhahah.androidartstudy.R;
import com.rayhahah.androidartstudy.util.ScreenUtil;
import com.rayhahah.androidartstudy.widget.CircleView;

public class ScrollActivity extends AppCompatActivity {

    private CircleView mCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        mCircle = ((CircleView) findViewById(R.id.circle_view));
    }

    public void clickScroller(View view) {
        int screenWidth = ScreenUtil.getScreenWidth(this);
        int screenHeight = ScreenUtil.getScreenHeight(this);
        mCircle.smoothScrollTo(((int) (Math.random() * screenWidth)), (int) (Math.random() * screenHeight), 1000);
    }

    public void clickScrolTo(View view) {
//        mCircle.scrollTo();
//        mCircle.scrollBy();

    }

    public void clickAnim(View view) {
    }

    public void clickLayoutParams(View view) {
    }
}
