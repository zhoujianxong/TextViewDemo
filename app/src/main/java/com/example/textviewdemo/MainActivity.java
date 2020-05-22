package com.example.textviewdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.example.textviewdemo.bean.ParagrapBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ReadView readView;
    private ScrollView scrollView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        final ReadTextView textView = findViewById(R.id.tv);
//        readView = findViewById(R.id.tv);


        scrollView = findViewById(R.id.scrollView);
//        readView = new ReadView(this);
//        imageView = findViewById(R.id.image);
        ReadTextView readTextView = new ReadTextView(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //描边变化
            List<String> words = new ArrayList<>();
            words.add("6#10");
            words.add("20#29");
//            words.add("149#158");
            List<String> noReads = new ArrayList<>();
            noReads.add("11-19");
            noReads.add("29-32");
//            noReads.add("149-158");
            readTextView.setWordsAndNoReads(words, noReads);//等级词变化
            readTextView.setStrokeColor(R.color.colorPrimary,R.color.colorAccent);
            readTextView.setmContent(getString(R.string.str));
            readTextView.setStroke(R.color.colorRed,50,80);
            scrollView.addView(readTextView);
//            textView.setStroke(new ForegroundColorSpan(getColor(R.color.colorRed)),140,200);









//            //一般着色
//            List<String> words = new ArrayList<>();
//            words.add("6#10");
//            words.add("20#29");
//            words.add("149#158");
//            List<String> noReads = new ArrayList<>();
//            noReads.add("11-19");
//            noReads.add("29-32");
//            noReads.add("149-158");
//            textView.setWordsAndNoReads(R.color.colorWords, R.color.colorNoReads, words, noReads);//等级词变化
//            textView.setForegroundColorSpan(R.color.colorAccent, 0, 25);//一般文字变化


//            List<String> urls = new ArrayList<>();
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202904991&di=4f07658af68a70a112e84c59ce50246d&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201310%2F19%2F235356fyjkkugokokczyo0.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179467&di=d8c8b10918bdef9c4476c0e33093aa69&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fq_mini%2Cc_zoom%2Cw_640%2Fimages%2F20170929%2Fe28345e8e98b4d7e80cea28586415e47.jpeg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589273710337&di=fa55a0c20b59d1599d93baacbd9df2e2&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201412%2F27%2F134436spkckvz6b9blkivy.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179467&di=fd0ba267e201222e415c5b8c741f15dc&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20161017%2F2986083eed7749618e4f3bf521aafeaf.png");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179467&di=361591c26ac529dfc3e1ecd4269d2580&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170816%2F45970eaa366f4ba99b754449879101ad.jpg");
//
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202566179&di=9ae804fe902dd8fd1c6d5768d316f27a&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20161228%2F6afacdd888944694a59098e313a46a2a_th.jpeg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179466&di=18c4e168b3c6f1021205408a89b1a2ac&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F3a17ba3ee6b06f4c1b532bb795e688321e1c7685376c-VNpYj3_fw658");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179465&di=7c28231fe46673dc0275dfcd24adcc39&imgtype=0&src=http%3A%2F%2Fhiphotos.baidu.com%2Ffeed%2Fpic%2Fitem%2F6f061d950a7b0208a4fb3cd16fd9f2d3572cc8d4.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202954668&di=1678e498c22909f2167b13c458d4ec31&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1212%2F10%2Fc1%2F16491670_1355126816487.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179465&di=ae92b0157e2e8e798ac1ae6f6f6f5823&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fbebe5455a5424e1677e37e991c5101f4400d95dfb04d-t28Sxg_fw658");
//
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202904991&di=4f07658af68a70a112e84c59ce50246d&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201310%2F19%2F235356fyjkkugokokczyo0.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179467&di=d8c8b10918bdef9c4476c0e33093aa69&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fq_mini%2Cc_zoom%2Cw_640%2Fimages%2F20170929%2Fe28345e8e98b4d7e80cea28586415e47.jpeg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179467&di=09eb00b99c98e3b0f9caaf71e48e208d&imgtype=0&src=http%3A%2F%2Fimg.tukexw.com%2Fimg%2F3f47d31b89dbf943.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179467&di=fd0ba267e201222e415c5b8c741f15dc&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20161017%2F2986083eed7749618e4f3bf521aafeaf.png");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179467&di=361591c26ac529dfc3e1ecd4269d2580&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170816%2F45970eaa366f4ba99b754449879101ad.jpg");
//
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202566179&di=9ae804fe902dd8fd1c6d5768d316f27a&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20161228%2F6afacdd888944694a59098e313a46a2a_th.jpeg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179466&di=18c4e168b3c6f1021205408a89b1a2ac&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F3a17ba3ee6b06f4c1b532bb795e688321e1c7685376c-VNpYj3_fw658");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179465&di=7c28231fe46673dc0275dfcd24adcc39&imgtype=0&src=http%3A%2F%2Fhiphotos.baidu.com%2Ffeed%2Fpic%2Fitem%2F6f061d950a7b0208a4fb3cd16fd9f2d3572cc8d4.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202954668&di=1678e498c22909f2167b13c458d4ec31&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1212%2F10%2Fc1%2F16491670_1355126816487.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179465&di=ae92b0157e2e8e798ac1ae6f6f6f5823&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fbebe5455a5424e1677e37e991c5101f4400d95dfb04d-t28Sxg_fw658");
//
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202566179&di=9ae804fe902dd8fd1c6d5768d316f27a&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20161228%2F6afacdd888944694a59098e313a46a2a_th.jpeg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179466&di=18c4e168b3c6f1021205408a89b1a2ac&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F3a17ba3ee6b06f4c1b532bb795e688321e1c7685376c-VNpYj3_fw658");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179465&di=7c28231fe46673dc0275dfcd24adcc39&imgtype=0&src=http%3A%2F%2Fhiphotos.baidu.com%2Ffeed%2Fpic%2Fitem%2F6f061d950a7b0208a4fb3cd16fd9f2d3572cc8d4.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202954668&di=1678e498c22909f2167b13c458d4ec31&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1212%2F10%2Fc1%2F16491670_1355126816487.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179465&di=ae92b0157e2e8e798ac1ae6f6f6f5823&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fbebe5455a5424e1677e37e991c5101f4400d95dfb04d-t28Sxg_fw658");
//
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202566179&di=9ae804fe902dd8fd1c6d5768d316f27a&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20161228%2F6afacdd888944694a59098e313a46a2a_th.jpeg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179466&di=18c4e168b3c6f1021205408a89b1a2ac&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F3a17ba3ee6b06f4c1b532bb795e688321e1c7685376c-VNpYj3_fw658");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179465&di=7c28231fe46673dc0275dfcd24adcc39&imgtype=0&src=http%3A%2F%2Fhiphotos.baidu.com%2Ffeed%2Fpic%2Fitem%2F6f061d950a7b0208a4fb3cd16fd9f2d3572cc8d4.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202954668&di=1678e498c22909f2167b13c458d4ec31&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1212%2F10%2Fc1%2F16491670_1355126816487.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589202179465&di=ae92b0157e2e8e798ac1ae6f6f6f5823&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fbebe5455a5424e1677e37e991c5101f4400d95dfb04d-t28Sxg_fw658");
//
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
//            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");




//            for (int g = 0; g < 50; g++) {
////                if (g%5==0){
//                urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589273710337&di=fa55a0c20b59d1599d93baacbd9df2e2&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201412%2F27%2F134436spkckvz6b9blkivy.jpg");
////                    urls.add("http://prod.prv.muyuhuajiaoyu.com/FtEGWmCID6Z15cu5-lxcfjG8HZUu?e=1589340827&token=B5jLjXcLuFnEaLZLa9jcDyd0fFQEYTFy2sHHetgH:7vdslx2WZvSojfUHUmvb-QBntgo=");
//////                    urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589210389179&di=ed1eda0e88188297889fbf9d4cca95b3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F548e9ebcea950.jpg");
////                }else {
////                    urls.add("");
////                }
//
//            }
//            paragrapBeans = new ArrayList<>();
//            for (int i = 0; i <1 ; i++) {
//                ParagrapBean paragrapBean = new ParagrapBean();
//                if (i % 2 > 0) {
//                    paragrapBean.setContent(getString(R.string.words));
//                } else {
//                    paragrapBean.setContent(getString(R.string.str));
//                }
//                paragrapBean.setCover(urls.get(i));
//                paragrapBeans.add(paragrapBean);
//            }
//            Log.v("TAG","开始下载图片");
//            getBitmap(0, paragrapBeans);
        }

//        findViewById(R.id.button_c).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int start = (int) (Math.random() * 100);
//                int position=(int) (Math.random() * 10);
////                readView.setReadColor(0, 80, 101, 0);
//                readView.setReadColor(0, start, start+(int) (Math.random() * 20), 0);
//                //scrollView.scrollTo(0,readView.getmContentHeightArray().get(position)-readView.getmStaticLayoutArray().get(position).getHeight());
//            }
//        });
    }

    private List<ParagrapBean> paragrapBeans;

    private void getBitmap(final int position, List<ParagrapBean> paragrapBeans) {
        if (paragrapBeans.size() <= 0) {
            return;
        }
        //设置图片圆角角度
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(30));
        Glide.with(this)
                .asBitmap()
                .apply(options)
                .load(paragrapBeans.get(position).getCover())
                .signature(new ObjectKey(UUID.randomUUID().toString()))
                .addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        Log.v("TAG", "加载失败 " + position+"   GlideException = "+e);
                        Message message = new Message();
                        message.what = LOADING;
                        message.arg1 = position;
                        message.obj = null;
                        mHandler.sendMessage(message);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        Log.v("TAG", "加载成功 " + position + "   file = " + resource);
                        Message message = new Message();
                        message.what = LOADING;
                        message.arg1 = position;
                        message.obj = resource;
                        mHandler.sendMessage(message);
                        return false;
                    }
                })
                .submit(400, 1000);

    }

    /**
     * 隐藏头部操作栏
     */
    public static final int LOADING = 0x00050;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == LOADING) {
                //加载
//                paragrapBeans.get(msg.arg1).setPath((String) msg.obj);
                paragrapBeans.get(msg.arg1).setBitmap((Bitmap)msg.obj);
                if (msg.arg1 + 1 < paragrapBeans.size()) {
                    getBitmap(msg.arg1 + 1, paragrapBeans);
                } else {
                    scrollView.removeAllViews();
                    List<String> words = new ArrayList<>();
                    words.add("6#10");
                    words.add("20#29");
                    words.add("50#70");
                    List<String> noReads = new ArrayList<>();
                    noReads.add("11-19");
                    noReads.add("29-32");
                    noReads.add("50-70");
                    readView.setWordsAndNoReads(words, noReads);//等级词变化
                    readView.setmParagrapBeans(paragrapBeans);
                    //为了onMeasure  能精确的测距
                    Log.v("TAG","下载结束");
                    scrollView.addView(readView);

                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
