package com.ljw.selfmvvm.bean;

import java.io.Serializable;

/**
 * Create by Ljw on 2019/12/12 11:14
 */
public class BannerBean implements Serializable {

    /**
     * desc : 享学~
     * id : 29
     * imagePath : https://www.wanandroid.com/blogimgs/51616c39-dd11-4173-857c-ec317b8ad9a0.png
     * isVisible : 1
     * order : 0
     * title : BUG如风，常伴吾身！
     * type : 0
     * url : https://mp.weixin.qq.com/s/j804G2-76YMqOVUBLRwwoA
     */
    private String desc;
    private int id;
    private String imagePath;
    private int isVisible;
    private int order;
    private String title;
    private int type;
    private String url;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
