package com.rayhahah.androidartstudy.module.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rayhahah.androidartstudy.R;
import com.rayhahah.androidartstudy.widget.CanvasView;
import com.rayhahah.androidartstudy.widget.ProgressView;

public class CanvasActivity extends AppCompatActivity {

    private CanvasView mCanvasView;
    private ProgressView mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        mCanvasView = ((CanvasView) findViewById(R.id.canvas_view));
        mProgressView = ((ProgressView) findViewById(R.id.progress_view));
    }

    /**
     * 正方形裁剪
     *
     * @param view
     */
    public void clipRect(View view) {
        mCanvasView.clipRect();
    }

    public void canvasMove(View view) {
        mCanvasView.canvasMove();
    }

    public void clipPath(View view) {
        mCanvasView.clipPath();
    }

    public void matrixMove(View view) {
        mCanvasView.matrixMove();

    }

    public void cameraMove(View view) {
        mCanvasView.cameraMove();
    }
}
