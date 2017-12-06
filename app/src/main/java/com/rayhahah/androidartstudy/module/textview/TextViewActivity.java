package com.rayhahah.androidartstudy.module.textview;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rayhahah.androidartstudy.R;
import com.rayhahah.androidartstudy.module.textview.line.MyImageSpan;
import com.rayhahah.androidartstudy.module.textview.line.MyLineHeightSpan;
import com.rayhahah.androidartstudy.module.textview.useful.AnimSpanUtil;
import com.rayhahah.androidartstudy.module.textview.useful.MyFrameSpan;
import com.rayhahah.androidartstudy.module.textview.useful.VerticalImageSpan;
import com.rayhahah.androidartstudy.module.textview.useful.replace.ReplaceSpan;
import com.rayhahah.androidartstudy.module.textview.useful.replace.SpansManager;
import com.rayhahah.androidartstudy.util.RLog;

public class TextViewActivity extends AppCompatActivity implements ReplaceSpan.OnClickListener {

    private TextView mTvNormal;
    private TextView mTvHref;
    private TextView mTvBasicSpan;
    private TextView mTvCustomSpan;
    private TextView mReplaceContent;
    private EditText mReplaceInput;
    private SpansManager mSpansManager;
    private String mTestStr = "我是个____学生,我有一个梦想，我要成为像____，____一样的人.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);
        mTvNormal = ((TextView) findViewById(R.id.tv_html_normal));
        mTvHref = ((TextView) findViewById(R.id.tv_html_href));
        mTvBasicSpan = ((TextView) findViewById(R.id.tv_basic_span));
        mTvCustomSpan = ((TextView) findViewById(R.id.tv_custom_span));

        mReplaceContent = ((TextView) findViewById(R.id.tv_replace_content));
        mReplaceInput = ((EditText) findViewById(R.id.et_replace_input));

        mTvNormal.setText(Html.fromHtml(getString(R.string.text_html_normal)));

        String html = getString(R.string.text_html_href);
        // 让链接可点击
        mTvHref.setMovementMethod(
                LinkMovementMethod.getInstance());
// ResouroceImageGetter用来处理TextView中的图片
        //ImageGetter本身是通过识别<img/> 标签来获取其内部的src数据
        mTvHref.setText(Html.fromHtml(html, new ResourceImageGetter(this), null));
        spanBasic();
        spanCustom();
        spanReplace();
    }

    /**
     * 填空题的Span
     */
    private void spanReplace() {
        mSpansManager = new SpansManager(this,mReplaceContent,mReplaceInput);
        mSpansManager.doFillBlank(mTestStr);
    }

    @Override
    public void OnClick(TextView v, int id, ReplaceSpan span) {
        mSpansManager.setData(mReplaceInput.getText().toString(),null, mSpansManager.mOldSpan);
        mSpansManager.mOldSpan = id;
        //如果当前span身上有值，先赋值给et身上
        mReplaceInput.setText(TextUtils.isEmpty(span.mText)?"":span.mText);
        mReplaceInput.setSelection(span.mText.length());
        span.mText = "";
        //通过rf计算出et当前应该显示的位置
        RectF rf = mSpansManager.drawSpanRect(span);
        //设置EditText填空题中的相对位置
        mSpansManager.setEtXY(rf);
        mSpansManager.setSpanChecked(id);
    }

    /**
     * 自定义字符级的Span
     */
    private void spanCustom() {
        String str = getString(R.string.long_string_article);
        SpannableString spannableString = new SpannableString(str);
        Drawable drawable = getResources().getDrawable(R.drawable.cover);
        drawable.setBounds(0, 0, 100, 100);
        spannableString.setSpan(new VerticalImageSpan(drawable), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new MyFrameSpan(Color.BLUE, 5), 10, 30, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        AnimSpanUtil.animateTypeWriterSpan(mTvCustomSpan, spannableString, 55, 70, mTvCustomSpan.getCurrentTextColor());
        AnimSpanUtil.animateColorSpan(mTvCustomSpan, spannableString, 40, 50, Color.BLACK, Color.RED);
        mTvCustomSpan.setText(spannableString);
    }

    /**
     * 基础的span使用以及了解
     */
    private void spanBasic() {
        String longString = getString(R.string.long_string_article);
        SpannableString spannableString = new SpannableString(longString);
//        spannableString.setSpan(new BulletSpan(), 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.cover);
        drawable.setBounds(0, 0, 200, 200);
        spannableString.setSpan(new MyImageSpan(drawable), 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new MyLineHeightSpan(), 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new BackgroundColorSpan(Color.RED), 10, 20, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 20, 30, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StrikethroughSpan(), 30, 40, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), 40, 50, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new MaskFilterSpan(new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL)), 50, 60, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                RLog.e("u click me");
                RLog.e(widget);
            }
        }, 60, 70, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new SubscriptSpan(), 70, 80, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new SuperscriptSpan(), 80, 90, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(25, true), 90, 100, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(2), 100, 110, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);


        mTvBasicSpan.setText(spannableString);
    }


    class ResourceImageGetter implements Html.ImageGetter {
        Context mContext;

        public ResourceImageGetter(Context context) {
            mContext = context;
        }

        @Override
        public Drawable getDrawable(String source) {
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.cover);
//            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.setBounds(0, 0, 100, 100
            );
            return drawable;
        }
    }
}
