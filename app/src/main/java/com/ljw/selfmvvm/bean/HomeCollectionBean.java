package com.ljw.selfmvvm.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Create by Ljw on 2019/12/13 11:56
 * 首页文章列表
 */
public class HomeCollectionBean implements Serializable {

    private ArrayList<HomeBean> datas;

    public ArrayList<HomeBean> getDatas(){
        return datas;
    }

    public void setDatas(ArrayList<HomeBean> datas) {
        this.datas = datas;
    }
}
