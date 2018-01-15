package com.rayhahah.androidartstudy.module.window;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.rayhahah.androidartstudy.R;

public class WindowActivity extends AppCompatActivity {


    private Button mButton;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private WindowManager mWindowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(WindowActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 10);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                    Toast.makeText(WindowActivity.this, "not granted", Toast.LENGTH_SHORT);
                }
            }
        }
    }

    public void showWindow(View view) {
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mButton = new Button(this);
        mButton.setText("WindowButton");
        mWindowLayoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                , 0, 0, PixelFormat.TRANSPARENT);
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        mWindowLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mWindowLayoutParams.x = 100;
        mWindowLayoutParams.y = 300;
        mWindowLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        mWindowManager.addView(mButton, mWindowLayoutParams);
    }
}
