package com.example.textviewdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.textviewdemo.bean.ParagrapBean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 阅读自定义 ReadView
 */
public class ReadView extends View {
    /**
     * 要标记的等级词坐标集    start#end
     */
    private List<String> mWords;
    /**
     * 要标记的不读坐标集    start#end
     */
    private List<String> mNoReads;
    /**
     * 单独词(等级词)  颜色  多个span 需要生成
     */
    private int mColorRead = R.color.colorPrimary;
    /**
     * 单独词(等级词)  颜色  多个span 需要生成
     */
    private int mColorWords = R.color.colorWords;
    /**
     * 单独词(不读)  颜色  多个span 需要生成
     */
    private int mColorNoRead = R.color.colorNoReads;
    /**
     * 内容
     */
    private List<ParagrapBean> mParagrapBeans;
    /**
     * 一般阅读着色
     */
    private ForegroundColorSpan foregroundColorSpan ;
    /**
     * 等级词着色
     */
    private ForegroundColorSpan foregroundColorSpanWords ;
    /**
     * 不读着色
     */
    private ForegroundColorSpan foregroundColorSpanNoReads ;
    /**
     * 加粗样式
     */
    private StyleSpan styleSpanBold = new StyleSpan(Typeface.BOLD);
    /**
     * 斜体
     */
    private StyleSpan styleSpanItalic = new StyleSpan(Typeface.ITALIC);
    /**
     * ReadView 宽
     */
    private int mReadViewWidth;
    /**
     * 阅读模式  0阅读模式  1一般着色阅读  复述
     */
    private int nReadType;
    /**
     * 段 的 position  从0开始  与 mParagrapBeans  相对应
     */
    private int mPosition;
    /**
     * 要处理的  startIndex 开始下标
     */
    private int mStartIndex;
    /**
     * 要处理的  endIndex 结束下标
     */
    private int mEndIndex;

    /**
     * 用作字体 改变
     */
    private SpannableStringBuilder mSpannableStringBuilder;
    /**
     * 绘字笔
     */
    private TextPaint mTextPaint;
    /**
     *
     */
    private Bitmap mBitmap;
    /**
     * 复述遮盖
     */
    private Drawable mDrawable;
    /**
     * 换行  测高
     */
    private StaticLayout mStaticLayout;
    /**
     * list   item 里  StaticLayout 集合
     */
    private SparseArray<StaticLayout> mStaticLayoutArray ;
    /**
     * list   item 里  文本相对ReadView  button 值 集合   y 坐标
     */
    private SparseArray<Integer> mContentHeightArray ;
    /**
     * list   item 里  文本+图片相对ReadView  button 值 集合   y 坐标
     */
    private SparseArray<Integer> mContentAndImageHeightArray ;
    /**
     * 点击 y
     */
    private float mDownY;
    /**
     * 点击 x
     */
    private float mDownX;

    public ReadView(Context context) {
        super(context);
    }

    public ReadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ReadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        init();
        mReadViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(mReadViewWidth, getMeasureHeight(mReadViewWidth));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 初始化
     */
    private void init() {
        setFocusable(true);
        mSpannableStringBuilder = new SpannableStringBuilder();
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(60);
        mTextPaint.setColor(ContextCompat.getColor(getContext(),mColorWords));

        foregroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(getContext(), mColorRead));
        foregroundColorSpanWords = new ForegroundColorSpan(ContextCompat.getColor(getContext(), mColorWords));
        foregroundColorSpanNoReads = new ForegroundColorSpan(ContextCompat.getColor(getContext(), mColorNoRead));

        mStaticLayoutArray = new SparseArray<>();
        mContentHeightArray = new SparseArray<>();
        mContentAndImageHeightArray = new SparseArray<>();
        setClick();
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
        for (int i = 0; i < mParagrapBeans.size(); i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//Android 6.0以后
                ParagrapBean paragrapBean = mParagrapBeans.get(i);
                String resContent = paragrapBean.getContent();
                mSpannableStringBuilder.clear();
                mSpannableStringBuilder.append(resContent);
                mStaticLayout = StaticLayout.Builder
                        .obtain(mSpannableStringBuilder, 0, mSpannableStringBuilder.length(), mTextPaint, widthSize)
                        .build();
                //垒加文本高度
                height = height + mStaticLayout.getHeight();
                mBitmap = paragrapBean.getBitmap();
                if (mBitmap != null) {
                    //垒加 图片高度
                    height = height + mBitmap.getHeight();
                }
                mBitmap = null;
            }
        }
        Log.v("TAG", "测距 h = " + height);
        return height;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mParagrapBeans == null) {
            return;
        }
        mStaticLayoutArray.clear();
        mContentHeightArray.clear();
        mContentAndImageHeightArray.clear();
        int contentHeight = 0;
        for (int i = 0; i < mParagrapBeans.size(); i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//Android 6.0以后
                ParagrapBean paragrapBean = mParagrapBeans.get(i);
//                //清空 mSpannableStringBuilder
//                mSpannableStringBuilder.clear();
//                //重新拼接 mSpannableStringBuilder
//                mSpannableStringBuilder.append(paragrapBean.getContent());
//
//                if (!isListNull(mWords)) {
//                    //等级词  优先级低
//                    for (int k = 0; k < mWords.size(); k++) {
//                        String indexStartEnd = mWords.get(k);
//                        if (!isEmpty(indexStartEnd)) {
//                            int start = Integer.parseInt(indexStartEnd.split("#")[0]);
//                            int end = Integer.parseInt(indexStartEnd.split("#")[1]);
//                            //设置等级词  粗体
//                            if (isNotSetSpan(start, end, mSpannableStringBuilder.length())) {
//                                mSpannableStringBuilder.setSpan(styleSpanBold, start, end, SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE);
//                                //设置等级词颜色
//                                mSpannableStringBuilder.setSpan(foregroundColorSpanWords,
//                                        start, end, SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE);
//                            }
//                        }
//                    }
//                }
//                //当前选中的 段mPosition优先级中
//                if (mPosition == i) {
//                    switch (nReadType) {
//                        case 0:
//                            //一般阅读着色
////                            ForegroundColorSpan[] ss = mSpannableStringBuilder.getSpans(mStartIndex, mEndIndex, ForegroundColorSpan.class);
////                            //有重复的样式 处理调
////                            for (ForegroundColorSpan s : ss) {
////                                mSpannableStringBuilder.removeSpan(s);
////                            }
////                            //设置阅读着色
////                            if (isNotSetSpan(mStartIndex, mEndIndex, mSpannableStringBuilder.length())) {
////                                mSpannableStringBuilder.setSpan(foregroundColorSpan, mStartIndex, mEndIndex, SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE);
////                            }
//                            break;
//                        case 1:
//                            //服饰
                            for (int s = mStartIndex; s < mEndIndex; s++) {
                                if (s % 2 > 0) {
                                    mDrawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_primary_read_retell_mask2);
                                } else {
                                    mDrawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_primary_read_retell_mask);
                                }
                                if (mDrawable != null) {
                                    //组件  left x轴起点  top y轴起点   right长  button高
//                                    drawable.setBounds(0, 0, (int) mTextPaint.getTextSize(), (int) mTextPaint.getTextSize());
                                    int wh=mStaticLayout.getHeight()/mStaticLayout.getLineCount();
                                    mDrawable.setBounds(0, 0, wh, wh);
                                    if (isNotSetSpan(s, s + 1, mSpannableStringBuilder.length())) {
                                        mSpannableStringBuilder.setSpan(new ImageSpan(mDrawable), s, s + 1, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
                                    }
                                }
                            }
//                            break;
//                        default:
//                            break;
//                    }
//                }
//                if (!isListNull(mNoReads)) {
//                    //不读词 优先级高
//                    for (int g = 0; g < mNoReads.size(); g++) {
//                        String indexStartEnd = mNoReads.get(g);
//                        if (!isEmpty(indexStartEnd)) {
//                            int start = Integer.parseInt(indexStartEnd.split("-")[0]);
//                            int end = Integer.parseInt(indexStartEnd.split("-")[1]);
//                            //清除掉 start到end重复的StyleSpan样式
//                            StyleSpan[] styleSpans = mSpannableStringBuilder.getSpans(start, end, StyleSpan.class);
//                            for (StyleSpan s : styleSpans) {
//                                mSpannableStringBuilder.removeSpan(s);
//                            }
//                            if (isNotSetSpan(start, end, mSpannableStringBuilder.length())) {
//                                //设置不读文字斜体
//                                mSpannableStringBuilder.setSpan(styleSpanItalic, start, end, SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE);
//                                //设置不读文字颜色
//                                mSpannableStringBuilder.setSpan(foregroundColorSpanNoReads,
//                                        start, end, SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE);
//                            }
//
//                        }
//                    }
//                }
//                //将准备好的字符串 绘制
//                mStaticLayout = StaticLayout.Builder
//                        .obtain(mSpannableStringBuilder, 0, mSpannableStringBuilder.length(), mTextPaint, mReadViewWidth)
//                        .build();
//                //绘制文字  带span
//                mStaticLayout.draw(canvas);

                mStaticLayout = StaticLayout.Builder
                        .obtain(paragrapBean.getContent(), 0, paragrapBean.getContent().length(), mTextPaint, mReadViewWidth)
                        .build();
                //绘制文字  带span
                mStaticLayout.draw(canvas);

                //阅读  文字着色  抛弃使用  setSpan
                if (i==mPosition){
                    String paragrapContent=paragrapBean.getContent();
                    float x=getReadStartX(mStaticLayout,paragrapBean.getContent(),mStartIndex);
                    float y=getReadStartY(mStaticLayout,contentHeight,mStartIndex);
                    int onRow=getOnRow(mStaticLayout,mStartIndex);
                    if (onRow>=0&&x>=0&&y>=0){
                        int onRowLineStart=mStaticLayout.getLineStart(onRow);
                        int onRowLineEnd=mStaticLayout.getLineEnd(onRow);
                        if (mStartIndex>onRowLineStart){
                            String firstContent=paragrapContent.substring(mStartIndex,onRowLineEnd);
                            canvas.translate(x, y);
                            mTextPaint.setColor(ContextCompat.getColor(getContext(),mColorRead));
                            StaticLayout.Builder
                                    .obtain(firstContent, 0, firstContent.length(), mTextPaint, mReadViewWidth)
                                    .build().draw(canvas);
                            mTextPaint.setColor(ContextCompat.getColor(getContext(),mColorWords));
                            canvas.translate(-x, -y);

                            if (mEndIndex>onRowLineEnd){
                                String overContent=paragrapContent.substring(onRowLineEnd,mEndIndex);
                                int overY=mStaticLayout.getLineTop(onRow+1)+mStaticLayout.getTopPadding();
                                canvas.translate(0, overY);
                                mTextPaint.setColor(ContextCompat.getColor(getContext(),mColorRead));
                                StaticLayout.Builder
                                        .obtain(overContent, 0, overContent.length(), mTextPaint, mReadViewWidth)
                                        .build().draw(canvas);
                                mTextPaint.setColor(ContextCompat.getColor(getContext(),mColorWords));
                                canvas.translate(0, -overY);
                            }
                        }else {
                           String content=paragrapContent.substring(mStartIndex,mEndIndex);
                            canvas.translate(0, y);
                            mTextPaint.setColor(ContextCompat.getColor(getContext(),mColorRead));
                            StaticLayout.Builder
                                    .obtain(content, 0, content.length(), mTextPaint, mReadViewWidth)
                                    .build().draw(canvas);
                            mTextPaint.setColor(ContextCompat.getColor(getContext(),mColorWords));
                            canvas.translate(0, -y);
                        }
                    }
                }


                //内容高度记录
                contentHeight = contentHeight + mStaticLayout.getHeight();
                //记录每项  staticLayout  在画板中的y-button 坐标
                mContentHeightArray.put(i, contentHeight);
                //记录每项 staticLayout
                mStaticLayoutArray.put(i, mStaticLayout);
                //获取 bitmap  暂时先这样做  后面要做复用  防止 oom
                mBitmap = paragrapBean.getBitmap();
                //校验是否有图片  有就绘制
                if (mBitmap != null) {
                    //绘制图片居中
                    canvas.translate(mReadViewWidth / 2 - mBitmap.getWidth() / 2, mStaticLayout.getHeight());
                    //绘制图片
                    canvas.drawBitmap(mBitmap, 0, 0, mTextPaint);
                    //内容高度记录
                    contentHeight = contentHeight + mBitmap.getHeight();
                    if (i + 1 < mParagrapBeans.size()) {
                        //跟新下一条的位置  恢复居中
                        canvas.translate(-(mReadViewWidth / 2 - mBitmap.getWidth() / 2), mBitmap.getHeight());
                    }
                } else {
                    //当没有图片时要  把画板坐标往下移
                    if (i + 1 < mParagrapBeans.size()) {
                        canvas.translate(0, mStaticLayout.getHeight());
                    }
                }
                //累计  记录累加的  y 值
                mContentAndImageHeightArray.put(i, contentHeight);
                mBitmap = null;
            }
        }
    }

    /**
     * 返回  start  所在的行
     * @param staticLayout
     * @param start
     * @return
     */
    private int getOnRow(StaticLayout staticLayout,int start){
        if (staticLayout==null){
            return -1;
        }
        for (int i = 0; i <staticLayout.getLineCount(); i++) {
            int lineStart=staticLayout.getLineStart(i);
            int lineEnd=staticLayout.getLineEnd(i);
            if (start>=lineStart){
                if (start<lineEnd){
                    //找到该行
                    return i;
                }
            }
        }
        return -1;
    }


    private float getReadStartX(StaticLayout staticLayout,String paragrapContent,int start){
        if (staticLayout==null){
            return -1;
        }
        int onRow=getOnRow(staticLayout,start);
        if (onRow>=0){
            int lineStart=staticLayout.getLineStart(onRow);
            if (start>lineStart){
                return mTextPaint.measureText(paragrapContent,lineStart,start);
            }else {
                return 0;
            }
        }
        return -1;
    }

    private float getReadStartY(StaticLayout staticLayout,int nowStaticLayoutHeight,int start){
        if (staticLayout==null){
            return -1;
        }
        int onRow=getOnRow(staticLayout,start);
        if (onRow>0){
            return staticLayout.getLineTop(onRow)+staticLayout.getTopPadding();
        }else if (onRow==0){
            return 0;
        }
        return -1;
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

    /**
     * 设置点击事件
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setClick() {
        setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = event.getX();
                    mDownY = event.getY();
                    Log.v("TAG", "OnTouch x= " + mDownX + "  y= " + mDownY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
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
        int selectPosition = getClickPosition();
        Log.v("TAG", "点击长按 选择position  " + selectPosition);
        setReadColor(selectPosition, 0, mParagrapBeans.get(selectPosition).getContent().length(), 0);
    }

    /**
     * 单点处理
     */
    private synchronized void toClick() {
        int selectPosition = getClickPosition();
        Log.v("TAG", "点击单点 选择position  " + selectPosition);
        if (selectPosition >= 0) {
            int startIndex = getClickIndex(selectPosition);
            if (startIndex >= 0) {
                setReadColor(selectPosition, startIndex, startIndex + 1, 0);
            }
        }
    }

    /**
     * 是否是长按   true 为长按
     */
    private boolean isOnLongClick = false;
    /**
     * handler  用来处理  单点击  和长按
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                isOnLongClick = false;

            } else if (msg.what == 1) {
                isOnLongClick = true;

            }

        }
    };


    /**
     * 设置内容
     *
     * @param mParagrapBeans 段集合
     */
    public void setmParagrapBeans(List<ParagrapBean> mParagrapBeans) {
        this.mParagrapBeans = mParagrapBeans;
    }


    /**
     * 阅读着色
     *
     * @param position 当前阅读段 position
     * @param start    着色开始下标
     * @param end      着色结束下标
     * @param readType 阅读模式  0  一般着色  1  复述
     */
    public synchronized void setReadColor(int position, int start, int end, int readType) {
        this.mPosition = position;
        this.mStartIndex = start;
        this.mEndIndex = end;
        this.nReadType = readType;
        invalidate();
    }

    /**
     * 设置等级词
     *
     * @param indexWordsStartEnd   等级词集合  words.add("6#10");   start#end
     * @param indexNoReadsStartEnd 不读词集合 noReads.add("11-19");  start-end
     */
    public void setWordsAndNoReads(List<String> indexWordsStartEnd, List<String> indexNoReadsStartEnd) {
        this.mWords = indexWordsStartEnd;
        this.mNoReads = indexNoReadsStartEnd;
    }

    /**
     * 返回点击定位
     *
     * @return
     */
    private int getClickPosition() {
        for (int i = 0; i < mContentHeightArray.size(); i++) {
            int itemY = mContentHeightArray.get(i);
            StaticLayout staticLayout = mStaticLayoutArray.get(i);
            if (mDownY > itemY - staticLayout.getHeight() && mDownY < itemY) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 点击  index 获取  占paragrapContent
     *
     * @param selectPositon
     * @return
     */
    private int getClickIndex(int selectPositon) {
        ParagrapBean paragrapBean = mParagrapBeans.get(selectPositon);
        StaticLayout staticLayout = mStaticLayoutArray.get(selectPositon);
        int nowh = staticLayout.getHeight() - (mContentHeightArray.get(selectPositon) - (int) mDownY);
        for (int i = 0; i < staticLayout.getLineCount(); i++) {
            int top = staticLayout.getLineTop(i);
            int button = staticLayout.getLineBottom(i);
            if (nowh > top && nowh < button) {
                //找到改行
                int size = staticLayout.getLineEnd(i) - staticLayout.getLineStart(i);
                float width = mTextPaint.measureText(paragrapBean.getContent(), staticLayout.getLineStart(i), staticLayout.getLineEnd(i));
                float itemSize = width / size;
                float index = staticLayout.getLineStart(i) + mDownX / itemSize;
                return (int) index;
            }
        }
        staticLayout.getLineCount();
        return -1;
    }

    /**
     * 分割单词   记录每个单词的下标
     *
     * @param paragrapContent 要分割的字符串
     * @return 返回所有单词下标 的集合
     */
    private Integer[] getWordsIndex(String paragrapContent) {
        //所有满足不是字母的   记录位置
        List<Integer> indices = new ArrayList<>();
        Pattern pattern = compile("[-a-zA-Z']+");
        int length = paragrapContent.length();
        for (int i = 0; i < length; i++) {
            char c = paragrapContent.charAt(i);
            if (!pattern.matcher(Character.toString(c))
                    .matches()) {
                indices.add(i);
            }
            if (i == length - 1) {
                indices.add(length);
            }
        }
        return indices.toArray(new Integer[0]);
    }

    /**
     * 获取当前 word res 里的位置
     *
     * @param res    显示内容
     * @param words  等级词
     * @param wordss 复合单词
     * @return 返回要着色的等级词 开始&结束  list(start#end)
     */
    public List<String> getPositionSubscript(String res, List<String> words, List<String> wordss, String language) {
        //ALog.d("等级词 " + words);
        List<String> listStartEnd = new ArrayList<>();
        if (res == null || language == null || words == null) {
            return listStartEnd;
        }
        if (words.size() > 0) {
            if (Constant.EN.equals(language)) {
                //英文
                // listStartEnd.addAll(getWordIndex(res, words));
                listStartEnd.addAll(getWordIndexs(res, words, language));
                listStartEnd.addAll(getWordIndexs(res, wordss, language));
            } else {
                //中文
                for (int i = 0; i < words.size(); i++) {
                    String word = words.get(i);
                    int startIndex = res.indexOf(word);
                    int lastIndex = res.lastIndexOf(word);
                    while (startIndex != -1) {
                        listStartEnd.add(startIndex + "#" + (startIndex + word.length()));
                        if (startIndex == lastIndex) {
                            break;
                        } else {
                            startIndex = res.indexOf(word, startIndex + word.length());
                        }
                    }
                }
            }
        }
        return listStartEnd;
    }


    /**
     * 获取单词的下标 包括匹配复合单词的
     *
     * @param res    字符串
     * @param wordss 复合单词list
     * @return 含开始和结束下标的 list
     */
    private List<String> getWordIndexs(String res, List<String> wordss, String language) {
        List<String> listStartEnd = new ArrayList<>();
        if (isEmpty(res) || isListNull(wordss)) {
            return listStartEnd;
        }
        //  List<String> resWordList = getStringList(res);
        int s = 0;
        for (int i = 0; i < wordss.size(); i++) {
            //word 复合单词
            String word = wordss.get(i);
            if (word != null) {
                List<Integer> indexss = searchAllIndex(res.toLowerCase(), word.toLowerCase());//不区分大小写
                for (int in : indexss) {
                    int lasPostion = in + word.length();
                    if (Constant.EN.equals(language)) {
                        if (res.length() > lasPostion) {
                            char ca = res.charAt(lasPostion);
                            //如句子: Why do you need a spoon?Because I want to eat my food.  等级词为a,   a会在句子里出现多次
                            //eat 里的 a 后面一位索引t仍然为英文字母， 则不算等级词。 a spoon  这个a 算等级词
                            //如果匹配到当前位置的后面一个字符，仍然为英文字母，
                            if (Character.isLowerCase(ca) || Character.isUpperCase(ca)) {  // 判断ch是否是字母字符，如'a'，'B'，是返回true
                                //ALog.i(String.format("句子【%s】等级词【%s】 (%s,%s)-但不算等级词",res,word,in,in + word.length()));
                                continue;
                            }
                        }
                    }
                    listStartEnd.add(in + "#" + lasPostion);
                    //ALog.d(String.format("句子【%s】等级词【%s】 (%s,%s)",res,word,in,lasPostion));
                }
            }
        }
        return listStartEnd;
    }

    /**
     * 字符串中查找另一个字符出现的全部索引位置
     *
     * @param str 字符串
     * @param key 要查找的字符
     */
    public static List<Integer> searchAllIndex(String str, String key) {
        List<Integer> integers = new ArrayList<>();
        int i = str.indexOf(key);//第一个出现的索引位置
        while (i != -1) {
            //ALog.v(i + "\t");
            integers.add(i);
            i = str.indexOf(key, i + 1);//从这个索引往后开始第一个出现的位置
        }
        return integers;
    }


    /**
     * @param s String res
     * @return b
     */
    public boolean isEmpty(String s) {
        return s == null || s.length() == 0 || s.equals("null");
    }

    /**
     * @param list List<T>
     * @param <T>  b
     * @return
     */
    public <T> boolean isListNull(List<T> list) {
        if (list != null && !list.isEmpty()) {
            return false;
        }
        return true;
    }

    public SparseArray<StaticLayout> getmStaticLayoutArray() {
        return mStaticLayoutArray;
    }

    public SparseArray<Integer> getmContentHeightArray() {
        return mContentHeightArray;
    }

    public SparseArray<Integer> getmContentAndImageHeightArray() {
        return mContentAndImageHeightArray;
    }
}
