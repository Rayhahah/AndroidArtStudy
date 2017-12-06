package com.rayhahah.androidartstudy.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ComposeShader;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.EmbossMaskFilter;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SumPathEffect;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.rayhahah.androidartstudy.R;

import java.util.Locale;

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
 * @time 2017/11/25
 * @tips 这个类是Object的子类
 * @fuction
 */
public class StudyView extends View {
    public static final String TEXT_HELLO = "hello";
    private int mTextSize;
    private int mTextColor;
    private String mTitle;
    private int mImageScale;
    private Bitmap mImage;
    private Paint mPaint;
    private Bitmap mCoverBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cover);

    public StudyView(Context context) {
        super(context);
    }

    public StudyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StudyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyleAttr, 0);

        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);

            switch (attr) {
                case R.styleable.CustomImageView_image:
                    mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomImageView_imageScaleType:
                    mImageScale = a.getInt(attr, 0);
                    break;
                case R.styleable.CustomImageView_titleText:
                    mTitle = a.getString(attr);
                    break;
                case R.styleable.CustomImageView_titleTextColor:
                    mTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomImageView_titleTextSize:
                    mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                            16, getResources().getDisplayMetrics()));
                    break;
                default:
                    break;
            }
        }
        a.recycle();
        initPaint();
    }

    /**
     * paint的使用大全
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(20);
        mPaint.setAntiAlias(true);//动态开关抗锯齿。
        mPaint.setDither(true);//开启绘制加抖动
        mPaint.setFilterBitmap(true);//是否使用双线性过滤来绘制 Bitmap (会让图像边的圆滑，和抗锯齿差不多，就是加入马赛克的原理)
        /**
         * 线头形状有三种：BUTT 平头、ROUND 圆头、SQUARE 方头。默认为 BUTT。
         */
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        /**
         *设置拐角的形状：MITER 尖角、 BEVEL 平角和 ROUND 圆角。默认为 MITER。
         */
        mPaint.setStrokeJoin(Paint.Join.MITER);


        CornerPathEffect cornerPathEffect = new CornerPathEffect(5);//所有拐角变成圆角
        mPaint.setPathEffect(cornerPathEffect);

        DiscretePathEffect discretePathEffect = new DiscretePathEffect(10, 10);//乱七八糟的补充，基本很少用
        mPaint.setPathEffect(discretePathEffect);

        /**
         *  第一个参数 intervals 是一个数组，它指定了虚线的格式：数组中元素必须为偶数（最少是 2 个），按照「画线长度、空白长度、画线长度、空白长度」
         *  第二个参数 phase 是虚线的偏移量
         */
        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{20, 10, 5, 10}, 0);
        mPaint.setPathEffect(dashPathEffect);


        /**
         * cw : 顺时针
         * ccw：逆时针
         */
        Path shape = new Path();
        shape.addCircle(0, 0, 10, Path.Direction.CW);

        /**
         *  advance 是两个相邻的 shape 段之间的间隔，不过注意，这个间隔是两个 shape 段的起点的间隔，而不是前一个的终点和后一个的起点的距离
         *  PathDashPathEffect.Style ，是一个 enum ，具体有三个值：
         *  TRANSLATE：位移
         *  ROTATE：旋转
         *  MORPH：变体
         */
        PathDashPathEffect pathDashPathEffect = new PathDashPathEffect(shape, 40, 0,
                PathDashPathEffect.Style.TRANSLATE);
        mPaint.setPathEffect(pathDashPathEffect);

        //很简单，两个PathEffect的相加而已
        SumPathEffect sumPathEffect = new SumPathEffect(dashPathEffect, pathDashPathEffect);
        mPaint.setPathEffect(sumPathEffect);

        /**
         *在之后的绘制内容下方， 添加一层阴影
         * 在硬件加速开启的情况下， setShadowLayer() 只支持文字的绘制，文字之外的绘制必须关闭硬件加速才能正常绘制阴影。
         */
        mPaint.setShadowLayer(5, 0, 0, Color.RED);

        /**
         * 在之后绘制内容的上方，添加一层阴影
         *
         * BlurMaskFilter模糊效果：
         * NORMAL: 内外都模糊绘制
         * SOLID: 内部正常绘制，外部模糊
         * INNER: 内部模糊，外部不绘制
         * OUTER: 内部不绘制，外部模糊（什么鬼？）
         *
         *  EmbossMaskFilter浮雕效果
         *  direction 是一个 3 个元素的数组，指定了光源的方向；
         *  ambient 是环境光的强度，数值范围是 0 到 1；
         *  specular 是炫光的系数；
         *  blurRadius 是应用光线的范围。
         *
         */

        BlurMaskFilter blurMaskFilter = new BlurMaskFilter(50, BlurMaskFilter.Blur.NORMAL);
        mPaint.setMaskFilter(blurMaskFilter);

        EmbossMaskFilter embossMaskFilter = new EmbossMaskFilter(new float[]{0, 1, 1}, 0.2f, 8, 10);
        mPaint.setMaskFilter(embossMaskFilter);


    }

    /**
     * 测量控件的大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec measure()以后就可以通过getMeasuredHeight()和getMeasuredWidth()获取长宽
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = 0;
        int height = 0;
        //match_parent模式和设定了具体的数值，就直接等于就可以了
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            //wrap_content的情况下
            //具体是依据什么去计算每个控件都不一致
            //基本都要算上padding的值

        }
        //match_parent模式和设定了具体的数值，就直接等于就可以了
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            //wrap_content的情况下
            //具体是依据什么去计算每个控件都不一致
            //基本都要算上padding的值
        }

        /**
         * 必须在最后调用，来把计算值MeasureSpec告诉父控件，本控件的测量规格与测量值
         * 这样一来，父控件才能获取到子控件的大小
         */
        setMeasuredDimension(width, height);
    }


    /**
     * 决定了View的四个顶点和实际View的宽高
     *
     * @param changed 是否发生改变
     *                完成之后，可以通过`getWidth()`和`getHeight()`方法拿到View的最终宽高
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 当大小发生改变的时候调用
     *
     * @param w    新宽度
     * @param h    新高度
     * @param oldw 原宽度
     * @param oldh 原高度
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        basicDraw(canvas);
        basicEffect(canvas);
        textDraw(canvas);
        translateDraw(canvas);
    }

    /**
     * 图像的变换
     */
    private void translateDraw(Canvas canvas) {

        /**
         * 对于图像的变换一定要
         * save()和restore()及时恢复绘制范围
         */
        canvas.save();
        RectF rect = new RectF(0, 0, 50, 50);
        //裁剪
        canvas.clipRect(rect);
        canvas.drawBitmap(mCoverBitmap, 0, 0, mPaint);
        canvas.restore();


        canvas.save();
        Path path = new Path();
        path.addCircle(25, 25, 25, Path.Direction.CW);
        canvas.clipPath(path);
        canvas.drawBitmap(mCoverBitmap, 0, 0, mPaint);
        canvas.restore();
    }

    /**
     * 文字的绘制
     *
     * @param canvas
     */
    private void textDraw(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(100, 100);
        path.rLineTo(100, 0);
        //注意x,y是文字的基线的位置，也就是文字会偏上
        canvas.drawText(TEXT_HELLO, 0, TEXT_HELLO.length(), 100, 100, mPaint);
        //沿着路径的问题绘制
        canvas.drawTextOnPath(TEXT_HELLO, path, 10, 10, mPaint);

        /**
         * 自带还行的Text绘制，也可以手动使用\n来进行换行
         * width 是文字区域的宽度，文字到达这个宽度后就会自动换行；
         * align 是文字的对齐方向；
         * spacingmult 是行间距的倍数，通常情况下填 1 就好；
         * spacingadd 是行间距的额外增加值，通常情况下填 0 就好；
         * includeadd 是指是否在文字上下添加额外的空间，来避免某些过高的字符的绘制出现越界。
         */
        String text1 = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
        TextPaint textPaint = new TextPaint();
        StaticLayout staticLayout1 = new StaticLayout(text1, textPaint, 600,
                Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        String text2 = "a\nbc\ndefghi\njklm\nnopqrst\nuvwx\nyz";
        StaticLayout staticLayout2 = new StaticLayout(text2, textPaint, 600,
                Layout.Alignment.ALIGN_NORMAL, 1, 0, true);

        staticLayout1.draw(canvas);
        staticLayout2.draw(canvas);
        mPaint.setTextSize(50);
        mPaint.setTypeface(Typeface.DEFAULT);//设置字体
//        mPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Satisfy-Regular.ttf"))
        mPaint.setFakeBoldText(true);//是否加粗
        mPaint.setStrikeThruText(true);//是否启用删除线
        mPaint.setUnderlineText(true);//是否使用下划线
        mPaint.setTextSkewX(20);//字体错切的角度，也就是字体斜体
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPaint.setLetterSpacing(10f);//设置字体之间的间距
        }
        mPaint.setTextAlign(Paint.Align.CENTER);//设置字体行内对其方式
        mPaint.setTextLocale(Locale.CHINA);//设置文字的地域
        mPaint.setHinting(Paint.HINTING_ON);//开启对矢量字体的修正，（现在屏幕分辨路很高，基本不需要使用）

        /**
         * 测量字体-----------------------------------
         */

        mPaint.getFontSpacing();//获取字体的行距，上下两行字体基准线之间的距离
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();//获取字体的详细位置参数，一般情况的换行而言，使用getFontSpacing更好
        Rect textBounds = new Rect();
        mPaint.getTextBounds(TEXT_HELLO, 0, TEXT_HELLO.length(), textBounds);//测量文字显示的范围，并且输出到rect当中

        float textWidth = mPaint.measureText(TEXT_HELLO);//测量文字所占用的宽度
        float[] textWidths = new float[]{};
        mPaint.getTextWidths(TEXT_HELLO, textWidths);//和上面一样

        /**
         *  breakText() 是在给出宽度上限的前提下测量文字的宽度。如果文字的宽度超出了上限，那么在临近超限的位置截断文字。
         *   text 是要测量的文字；
         *   measureForwards 表示文字的测量方向，true 表示由左往右测量；
         *   maxWidth 是给出的宽度上限；
         *   measuredWidth 是用于接受数据，而不是用于提供数据的：方法测量完成后会把截取的文字宽度（如果宽度没有超限，则为文字总宽度）赋值给 measuredWidth[0]。
         */
        float[] breakTextWidth = new float[]{};
        mPaint.breakText(TEXT_HELLO, 0, TEXT_HELLO.length(), true, 100, breakTextWidth);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            /**
             *获取光标位置
             * 对于一段文字，计算出某个字符处光标的 x 坐标。
             * start end 是文字的起始和结束坐标；
             * contextStart contextEnd 是上下文的起始和结束坐标；
             * isRtl 是文字的方向；
             * offset 是字数的偏移，即计算第几个字符处的光标。
             */
            float runAdvance = mPaint.getRunAdvance(TEXT_HELLO, 0, TEXT_HELLO.length(), 0, TEXT_HELLO.length(), true, 0);

            /**
             * 给出一个位置的像素值，计算出文字中最接近这个位置的字符偏移量（即第几个字符最接近这个坐标）。
             * advance 是给出的位置的像素值。填入参数，对应的字符偏移量将作为返回值返回。
             */
            Float advance = new Float(10);
            mPaint.getOffsetForAdvance(TEXT_HELLO, 0, TEXT_HELLO.length(), 0, TEXT_HELLO.length(), true, advance);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            boolean hasGlyph = mPaint.hasGlyph("hello");//判断是否包含对应的字符
        }


    }

    /**
     * 基础图形的绘制和使用
     *
     * @param canvas
     */
    private void basicDraw(Canvas canvas) {
        Rect rect = new Rect(0, 0, 100, 100);
        //画矩形
        canvas.drawRect(rect, mPaint);

        canvas.drawPoint(10, 10, mPaint);
        float[] points = {0, 0, 50, 50, 50, 100, 100, 50, 100, 100, 150, 50, 150, 100};
        // 绘制四个点：(50, 50) (50, 100) (100, 50) (100, 100)
        canvas.drawPoints(points, 2 /* 跳过两个数，即前两个 0 */,
                8 /* 一共绘制 8 个数（4 个点）*/, mPaint);
        //绘制椭圆
        RectF oval = new RectF(0, 0, 100, 100);
        canvas.drawOval(oval, mPaint);

        //画直线
        canvas.drawLine(0, 0, 100, 100, mPaint);
        float[] lines = {20, 20, 120, 20, 70, 20, 70, 120, 20, 120, 120, 120, 150, 20, 250, 20, 150, 20, 150, 120, 250, 20, 250, 120, 150, 120, 250, 120};
        canvas.drawLines(lines, 2 /* 跳过两个数，即前两个 0 */,
                8 /* 一共绘制 8 个数（2条线）*/, mPaint);

        //画圆角矩形
        RectF rectF = new RectF(0, 0, 100, 100);
        canvas.drawRoundRect(rectF, 10/* x轴圆角大小*/, 10/*y轴圆角大小 */, mPaint);

        //绘制弧形或者扇形
        RectF arc = new RectF(0, 0, 100, 100);
        canvas.drawArc(arc, 90/*弧形的起始角度*/, 90/*弧形划过的角度*/, true, mPaint);

        Path path = new Path();
//        path.addXXX这个和上面的基本差不多不再演示了
        path.moveTo(100, 200);
        path.lineTo(200, 300);
        //相对当前坐标的偏移
        path.rLineTo(100, 0);
        //x1,y1表示控制点，x2,y2表示目标位置
        path.quadTo(100, 100, 400, 400);
        //表示相对位置
        path.rQuadTo(100, 100, 400, 400);
//        三次贝塞尔曲线
//        path.cubicTo();

        RectF arcTo = new RectF(0, 0, 100, 100);
        path.arcTo(arcTo, 90, 90, true);

        //闭合路径，和lineTo（起始点）效果一致
        path.close();

//        Path.FillType.EVEN_ODD ：交叉填充模式，奇偶原则， 第一点射出一条直线，相交边是奇数就是内部，偶数就是外部
//        Path.FillType.WINDING(默认) ：非零环绕数原则， 同样放出一条射线，碰到顺时针就+1，逆时针就-1，结果是0就是外部，不是0就是内部
//        Path.FillType.INVERSE_EVEN_ODD ： 反色版本
//        Path.FillType.INVERSE_WINDING ： 反色版本
        //设置填充方式
        path.setFillType(Path.FillType.WINDING);

        canvas.drawPath(path, mPaint);

        //截图图片的大小
        Rect src = new Rect(0, 0, 100, 100);
        //摆放的位置，类似与drawRect
        RectF dst = new RectF(0, 0, 100, 100);
        canvas.drawBitmap(mCoverBitmap, src, dst, mPaint);

        //注意x,y是文字的基线的位置，也就是文字会偏上
        canvas.drawText(TEXT_HELLO, 0, TEXT_HELLO.length(), 100, 100, mPaint);
        //沿着路径的问题绘制
        canvas.drawTextOnPath(TEXT_HELLO, path, 10, 10, mPaint);
        mPaint.setTextSize(50);
    }


    /**
     * 基础叠加效果的使用
     *
     * @param canvas
     */
    private void basicEffect(Canvas canvas) {
        /**
         * 线性渐变
         * x0 y0 x1 y1：渐变的两个端点的位置
         * color0 color1 是端点的颜色
         * tile：端点范围之外的着色规则，类型是 TileMode。
         * TileMode 一共有 3 个值可选：
         * CLAMP （夹子模式？？？算了这个词我不会翻）会在端点之外延续端点处的颜色；
         * MIRROR 是镜像模式；
         * REPEAT 是重复模式。具体的看一下例子就明白。
         */
        LinearGradient linearGradient = new LinearGradient(0, 0, 100, 100, Color.BLUE, Color.RED, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);

        /**
         * 辐射渐变
         * 圆形的中心渐变，参数与上类似
         */
        RadialGradient radialGradient = new RadialGradient(100, 100, 200, Color.BLUE, Color.RED, Shader.TileMode.CLAMP);
        mPaint.setShader(radialGradient);

        /**
         * 就是用 Bitmap 的像素来作为图形或文字的填充
         * bitmap：用来做模板的 Bitmap 对象
         * tileX：横向的 TileMode
         * tileY：纵向的 TileMode。
         */
        BitmapShader bitmapShader = new BitmapShader(mCoverBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(bitmapShader);

        /**
         * 混合着色器
         * ComposeShader() 在硬件加速下是不支持两个相同类型的 Shader 的，所以这里也需要关闭硬件加速才能看到效果。
         *
         */

        BitmapShader bitmapShaderA = new BitmapShader(mCoverBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        BitmapShader bitmapShaderB = new BitmapShader(mCoverBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        ComposeShader composeShader = new ComposeShader(bitmapShaderA, bitmapShaderB, PorterDuff.Mode.DST_IN);
        mPaint.setShader(composeShader);

        /**
         *  mul 和 add 都是和颜色值格式相同的 int 值
         *  mul 用来和目标像素相乘，add 用来和目标像素相加
         *  R' = R * mul.R / 0xff + add.R
         *  G' = G * mul.G / 0xff + add.G
         *  B' = B * mul.B / 0xff + add.B
         */
        LightingColorFilter lightingColorFilter = new LightingColorFilter(0x00ffff, 0x000000);
        mPaint.setColorFilter(lightingColorFilter);

        /**
         * 这个和上面的ComposeShader类似，不过是对颜色的叠加而已
         */
        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(0xffffff, PorterDuff.Mode.DST_IN);
        mPaint.setColorFilter(porterDuffColorFilter);

        /**
         * 当图像遇上颜色
         * 颜色矩阵变化，会这个就够了
         * 一个4*5的颜色矩阵
         * [ a, b, c, d, e,
         * f, g, h, i, j,
         * k, l, m, n, o,
         * p, q, r, s, t ]
         *
         * 计算法则
         * R’ = a*R + b*G + c*B + d*A + e;
         * G’ = f*R + g*G + h*B + i*A + j;
         * B’ = k*R + l*G + m*B + n*A + o;
         * A’ = p*R + q*G + r*B + s*A + t;
         *
         * https://github.com/chengdazhi/StyleImageView拿去耍
         */
        ColorMatrix colorMatrix = new ColorMatrix();
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        mPaint.setColorFilter(colorMatrixColorFilter);

        /**
         * 当颜色遇上 View
         *  Xfermode 指的是你要绘制的内容和 Canvas 的目标位置的内容应该怎样结合计算出最终的颜色
         *  以绘制的内容作为源图像，以 View 中已有的内容作为目标图像，选取一个  PorterDuff.Mode 作为绘制内容的颜色处理方案
         *
         * 注意需要使用 离屏缓冲（Off-screen Buffer）
         * 否则透明区域会以黑色作为最终呈现
         * saveLayer和restoreToCount
         *
         *
         * 同时要注意透明区域必须要完全覆盖到两个图像，否则会影响Xfermode生效
         */

        int layerId = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);

        Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        canvas.drawBitmap(mCoverBitmap, 0, 0, mPaint); // 画方
        mPaint.setXfermode(xfermode); // 设置 Xfermode
        canvas.drawBitmap(mCoverBitmap, 0, 0, mPaint); // 画圆
        mPaint.setXfermode(null); // 用完及时清除 Xfermode

        canvas.restoreToCount(layerId);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
