package com.rayhahah.androidartstudy.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.rayhahah.androidartstudy.R;
import com.rayhahah.androidartstudy.util.CameraUtil;

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
 * @time 2017/12/4
 * @tips 这个类是Object的子类
 * @fuction
 */
public class CanvasView extends View {

    private Paint mPaint;
    private Bitmap mCoverBitmap;
    private int mFlag = 0;

    private static final int FLAG_CLIP_RECT = 0;
    private static final int FLAG_CLIP_PATH = 1;
    private static final int FLAG_CANVAS_MOVE = 2;
    private static final int FLAG_MATRIX_MOVE = 3;
    private static final int FLAG_CAMERA_MOVE = 4;

    public CanvasView(Context context) {
        this(context, null);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mCoverBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cover);
        mCoverBitmap = CameraUtil.zoomBitmap(mCoverBitmap, 300, 300);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.drawColor(Color.parseColor("#bbbbbb"));
        switch (mFlag) {
            case FLAG_CLIP_RECT:
                canvas.save();
                RectF rect = new RectF(0, 0, 200, 200);
                canvas.clipRect(rect);
                canvas.drawBitmap(mCoverBitmap, 0, 0, mPaint);
                canvas.restore();
                break;
            case FLAG_CLIP_PATH:
                canvas.save();
                Path path = new Path();
                path.addCircle(150, 150, 100, Path.Direction.CW);
                canvas.clipPath(path);
                canvas.drawBitmap(mCoverBitmap, 0, 0, mPaint);
                canvas.restore();
                break;
            case FLAG_CANVAS_MOVE:
                canvas.save();
                /**
                 * canvas的变换是反序的
                 */
                //平移
                canvas.translate(300, 300);
                mPaint.setColor(Color.RED);
                canvas.drawRect(200, 200, 300, 300, mPaint);
                //旋转
                canvas.rotate(45);
                canvas.drawBitmap(mCoverBitmap, 0, 0, mPaint);
                canvas.rotate(-45);

                //放大
                canvas.scale(1.5f, 1.5f, 100 + mCoverBitmap.getWidth() / 2, 100 + mCoverBitmap.getHeight() / 2);
                //错切
                canvas.skew(0.5f, 0);
                canvas.drawBitmap(mCoverBitmap, 100, 100, mPaint);

                canvas.restore();
                break;
            case FLAG_MATRIX_MOVE:

                /**
                 * 矩阵
                 * 基本API和canvas类似（canvas内部也是使用矩阵的）
                 * pre：往前插入
                 * post：往后插入
                 */
                Matrix matrix = new Matrix();
                matrix.reset();
//                matrix.preTranslate(100, 100);
//                matrix.postTranslate(100, 100);

                int left = 0;
                int top = 0;
                int right = getWidth();
                int bottom = getHeight();

                float[] pointsSrc = {left, top, right, top, left, bottom, right, bottom};
                float[] pointsDst = {left + 50, top + 50, right + 100, top + 100, left - 50, bottom + 200, right, bottom};
//                float[] pointsDst = {left - 10, top + 50, right + 120, top - 90, left + 20, bottom + 30, right + 20, bottom + 60};
                /**
                 * matrix自定义变换的关键setPolyToPoly
                 * setPolyToPoly ： 通过多点的映射的方式来直接设置变换
                 * 「多点映射」的意思就是把指定的点移动到给出的位置，从而发生形变。
                 *
                 * src 和 dst 是源点集合目标点集；{左上角，右上角，左下角，右下角}
                 * srcIndex 和 dstIndex 是第一个点的偏移；
                 * pointCount 是采集的点的个数（个数不能大于 4，因为大于 4 个点就无法计算变换了）。
                 */
                matrix.setPolyToPoly(pointsSrc, 0, pointsDst, 0, 4);
                canvas.save();
                //在现有规则的情况下补充（实际就是当前canvas内部矩阵和目标矩阵的相称）
                canvas.concat(matrix);
//                直接设置为新的矩阵
//                canvas.setMatrix(matrix);
                canvas.drawBitmap(mCoverBitmap, 0, 0, mPaint);
                canvas.restore();
                break;
            case FLAG_CAMERA_MOVE:
                Camera camera = new Camera();
                float centerX = 200 + mCoverBitmap.getWidth() / 2;
                float centerY = 200 + mCoverBitmap.getHeight() / 2;
                /**
                 * 默认是（0，0，-8） 单位是英寸
                 * 8 x 72 = 576，所以它的默认位置是 (0, 0, -576)（像素）。
                 * Z数值越大，图像就越大
                 *
                 * 要在save之前
                 */
                camera.setLocation(0, 0, -3);
                canvas.save();


                /**
                 * canvas的变化是倒序的！！！！
                 * 所以translate(-centerX, -centerY)是先执行
                 * translate(centerX, centerY)是在camera.applyToCanvas(canvas)以后执行的！！
                 */
                camera.rotateX(50); // 旋转 Camera 的三维空间
                canvas.translate(centerX, centerY); // 旋转之后把投影移动回来
                camera.applyToCanvas(canvas); // 把旋转投影到 Canvas
                canvas.translate(-centerX, -centerY); // 旋转之前把绘制内容移动到轴心（原点）
                canvas.drawBitmap(mCoverBitmap, 200, 200, mPaint);
                canvas.restore();

                break;
            default:
                break;
        }
    }

    public void clipRect() {
        mFlag = FLAG_CLIP_RECT;
        postInvalidate();
    }

    public void clipPath() {
        mFlag = FLAG_CLIP_PATH;
        postInvalidate();
    }

    public void canvasMove() {
        mFlag = FLAG_CANVAS_MOVE;
        postInvalidate();
    }

    public void matrixMove() {
        mFlag = FLAG_MATRIX_MOVE;
        postInvalidate();
    }

    public void cameraMove() {
        mFlag = FLAG_CAMERA_MOVE;
        postInvalidate();
    }

    /**
     * 在 super.dispatchDraw() 的下面写上你的绘制代码，这段绘制代码就会发生在子 View 的绘制之后，从而让绘制内容盖住子 View 了。
     * <p>
     * 把绘制代码写在 super.dispatchDraw() 的上面 , 这段绘制就会在 onDraw() 之后、super.dispatchDraw() 之前发生，也就是绘制内容会出现在主体内容和子 View 之间。
     *
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

    }

    /**
     * 这个方法是 API 23 才引入的（以前只有FrameLayout才拥有）
     * super.onDrawForeground() 的下面，绘制代码会在滑动边缘渐变、滑动条和前景之后被执行，那么绘制内容将会盖住滑动边缘渐变、滑动条和前景。
     * super.onDrawForeground() 的上面，绘制内容就会在 dispatchDraw() 和  super.onDrawForeground() 之间执行，那么绘制内容会盖住子 View，但被滑动边缘渐变、滑动条以及前景盖住：
     *
     * @param canvas
     */
    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
