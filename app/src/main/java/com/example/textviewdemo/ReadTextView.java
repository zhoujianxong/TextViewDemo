package com.example.textviewdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import java.util.List;

public class ReadTextView extends AppCompatTextView {
    private TextPaint mTextPaint;//一般文字笔
    private int mReadColor = R.color.colorTextDefault;//默认字体
    private int mOuterColor = R.color.colorAccent;//外边颜色
    private int mInnerColor = R.color.colorPrimary;//内边颜色
    private int mColorWords = R.color.colorPrimary;//单独词(等级词)  颜色  多个span 需要生成
    private int mColorNoRead = R.color.colorPrimary;//单独词(不读)  颜色  多个span 需要生成
    private ForegroundColorSpan mForegroundColorSpanStroke;//描边变化 span
    private int mStart;//改变的开始
    private int mEnd;//改变的结束
    private List<String> mWords;//要标记的等级词坐标集    start#end
    private List<String> mNoReads;//要标记的不读坐标集    start#end
    private final static int STROKE_WIDTH = 7;//描边宽度
    private SpannableStringBuilder mSpannableStringBuilder;//s
    private final float LETTER_SPACING = 0.13f;//字间距
    private float mDownY;//点击 y
    private float mDownX;//点击 x
    private String mContent;//内容
    private int mReadType;//0 一般着色  1复述

    public ReadTextView(Context context) {
        super(context);
    }

    public ReadTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReadTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        init();
        int mReadViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(mReadViewWidth, getMeasureHeight(mReadViewWidth));
    }

    /**
     * 测距 实际测量显示的 高度
     *
     * @param widthSize 实际宽度
     * @return 实测的  height
     */
    private int getMeasureHeight(int widthSize) {
        int height = 0;
        //通过 StaticLayout 拿去实际的  height
        SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder();
        mSpannableStringBuilder.clear();
        mSpannableStringBuilder.append(mContent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//Android 6.0以后
            mStaticLayout = StaticLayout.Builder
                    .obtain(mSpannableStringBuilder, 0, mSpannableStringBuilder.length(), mTextPaint, widthSize)
                    .build();
            height = mStaticLayout.getHeight();
        }
        Log.v("TAG", "测距 h = " + height);
        return height;
    }

    private void init() {
        //文本笔
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(getTextSize());
        mSpannableStringBuilder = new SpannableStringBuilder();
        setClick();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mContent == null) {
            return;
        }
        setStroke(canvas);//描边
    }

    /**
     * 描边
     *
     * @param canvas
     */
    private void setStroke(Canvas canvas) {
        canvas.save();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//Android 6.0以后
            setLetterSpacing(LETTER_SPACING);
            //先画外描边
            mTextPaint.setColor(ContextCompat.getColor(getContext(), mOuterColor));
            mTextPaint.setStrokeWidth(STROKE_WIDTH);
            mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mTextPaint.setFakeBoldText(false);

            mSpannableStringBuilder.clearSpans();
            mSpannableStringBuilder.clear();
            mSpannableStringBuilder.append(mContent);

            if (!isListNull(mNoReads)) {
                //描边 不读词 优先级高
                for (int i = 0; i < mNoReads.size(); i++) {
                    String indexStartEnd = mNoReads.get(i);
                    if (!isEmpty(indexStartEnd)) {
                        int start = Integer.parseInt(indexStartEnd.split("-")[0]);
                        int end = Integer.parseInt(indexStartEnd.split("-")[1]);
                        //有重复的样式 除掉
                        StyleSpan[] styleSpans = mSpannableStringBuilder.getSpans(start, end, StyleSpan.class);
                        for (StyleSpan s : styleSpans) {
                            mSpannableStringBuilder.removeSpan(s);
                        }
                        if (isNotSetSpan(start, end, mSpannableStringBuilder.length())) {
                            mSpannableStringBuilder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
            }

            //外描边  先画摆好
            StaticLayout.Builder
                    .obtain(mSpannableStringBuilder, 0, mSpannableStringBuilder.length(), mTextPaint, getWidth())
                    .build().draw(canvas);
            //在吧画笔初始化成内描边
            mTextPaint.setColor(ContextCompat.getColor(getContext(), mInnerColor));
            mTextPaint.setStyle(Paint.Style.FILL);
            mTextPaint.setFakeBoldText(false);
            //清空 保证清洁
            mSpannableStringBuilder.clearSpans();
            mSpannableStringBuilder.clear();
            mSpannableStringBuilder.append(mContent);
            //准备等级词的
            if (!isListNull(mWords)) {
                //描边 等级词  优先级低
                for (int i = 0; i < mWords.size(); i++) {
                    String indexStartEnd = mWords.get(i);
                    if (!isEmpty(indexStartEnd)) {
                        int start = Integer.parseInt(indexStartEnd.split("#")[0]);
                        int end = Integer.parseInt(indexStartEnd.split("#")[1]);
                        if (isNotSetSpan(start, end, mSpannableStringBuilder.length())) {
                            mSpannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), mColorWords)),
                                    start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
            }


            if (mReadType==1){
                //复述

            }else {
                //设置着色句  优先级中
                StyleSpan[] ss = mSpannableStringBuilder.getSpans(mStart, mEnd, StyleSpan.class);
                //有重复的样式 处理调
                for (StyleSpan s : ss) {
                    mSpannableStringBuilder.removeSpan(s);
                }
                //描边的一般着色
                if (isNotSetSpan(mStart, mEnd, mSpannableStringBuilder.length())) {
                    mSpannableStringBuilder.setSpan(mForegroundColorSpanStroke, mStart, mEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }



            if (!isListNull(mNoReads)) {
                //描边 不读词 优先级高
                for (int i = 0; i < mNoReads.size(); i++) {
                    String indexStartEnd = mNoReads.get(i);
                    if (!isEmpty(indexStartEnd)) {
                        int start = Integer.parseInt(indexStartEnd.split("-")[0]);
                        int end = Integer.parseInt(indexStartEnd.split("-")[1]);
                        //有重复的样式 除掉
                        StyleSpan[] styleSpans = mSpannableStringBuilder.getSpans(start, end, StyleSpan.class);
                        for (StyleSpan s : styleSpans) {
                            mSpannableStringBuilder.removeSpan(s);
                        }
                        if (isNotSetSpan(start, end, mSpannableStringBuilder.length())) {
                            mSpannableStringBuilder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                            mSpannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), mColorNoRead)),
                                    start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
            }
            mStaticLayout = StaticLayout.Builder
                    .obtain(mSpannableStringBuilder, 0, mSpannableStringBuilder.length(), mTextPaint, getWidth())
                    .build();
            mStaticLayout.draw(canvas);
        }
        canvas.restore();
    }


    /**
     * 设置等级词
     *
     * @param indexWordsStartEnd
     * @param indexNoReadsStartEnd
     */
    public void setWordsAndNoReads(List<String> indexWordsStartEnd, List<String> indexNoReadsStartEnd) {
        this.mWords = indexWordsStartEnd;
        this.mNoReads = indexNoReadsStartEnd;
    }

    /**
     * 设置描边颜色
     *
     * @param innerColor 内边颜色
     * @param outerColor 外边颜色
     */
    public void setStrokeColor(int innerColor, int outerColor) {
        this.mInnerColor = innerColor;
        this.mOuterColor = outerColor;
    }

    /**
     * 设置描边  改变色
     *
     * @param start
     * @param end
     */
    public void setStroke(int color, int start, int end) {
        this.mReadColor=color;
        this.mForegroundColorSpanStroke = new ForegroundColorSpan(ContextCompat.getColor(getContext(), mReadColor));
        this.mStart = start;
        this.mEnd = end;
        invalidate();
    }


    /**
     * 设置点击事件
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setClick() {
        setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mDownX = event.getX();
                mDownY = event.getY();
            }
            return false;
        });
        setOnLongClickListener(view -> {
            toLongClick();
            return true;
        });
        //单点 方法走流程  ClickableSpan()-->onClick();
        setOnClickListener(view -> {
            toClick();
        });
    }
    /**
     * 长按处理
     */
    private void toLongClick() {
        Log.v("TAG", "长按  index = " + getClickIndex() + " " + mContent.charAt(getClickIndex()));
    }

    /**
     * 单点处理
     */
    private synchronized void toClick() {
        Log.v("TAG", "单点  index = " + getClickIndex() + " " + mContent.charAt(getClickIndex()));
        setStroke(mReadColor, getClickIndex(), getClickIndex() + 1);
    }

    /**
     * 换行  测高
     */
    private StaticLayout mStaticLayout;

    /**
     * 返回  点击  index
     *
     * @return
     */
    private int getClickIndex() {
        float nowh = mDownY;
        for (int i = 0; i < mStaticLayout.getLineCount(); i++) {
            int top = mStaticLayout.getLineTop(i);
            int button = mStaticLayout.getLineBottom(i);
            if (nowh > top && nowh < button) {
                //找到改行
                int size = mStaticLayout.getLineEnd(i) - mStaticLayout.getLineStart(i);
                float width = mTextPaint.measureText(mContent, mStaticLayout.getLineStart(i), mStaticLayout.getLineEnd(i));
                float itemSize = width / size;
                float index = mStaticLayout.getLineStart(i) + mDownX / itemSize;
                return (int) index;
            }
        }
        return -1;
    }

    /**
     * 设置内容
     *
     * @param mContent
     */
    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public boolean isEmpty(String s) {
        return s == null || s.length() == 0 || s.equals("null");
    }

    public <T> boolean isListNull(List<T> list) {
        if (list != null && !list.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否 可设置 span
     *
     * @param start
     * @param end
     * @param length
     * @return
     */
    private boolean isNotSetSpan(int start, int end, int length) {
        if (end >= start) {
            if (end <= length) {
                return true;
            }
        }
        return false;
    }


}
