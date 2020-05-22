package com.example.textviewdemo.bean;

import android.graphics.Bitmap;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * 段落
 */
public class ParagrapBean implements Serializable {

    private String cover;  //段落图片
//    private List<SentenceBean> sentenceList;//句子列表

    private String content;
    private String path;

    private List<String> axis;
    private String width;
    private String coverPosition;//  up  down  图片位置
    /**
     * 可能会  oom  如果图片多考虑用  list  不要用单独  ReadView
     */
    private Bitmap bitmap;


    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getAxis() {
        return axis;
    }

    public void setAxis(List<String> axis) {
        this.axis = axis;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getCoverPosition() {
        return coverPosition;
    }

    public void setCoverPosition(String coverPosition) {
        this.coverPosition = coverPosition;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
