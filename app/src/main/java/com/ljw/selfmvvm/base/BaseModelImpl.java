package com.ljw.selfmvvm.base;

import androidx.lifecycle.MutableLiveData;

import com.ljw.selfmvvm.bean.BannerBean;
import com.ljw.selfmvvm.bean.HomeCollectionBean;
import com.ljw.selfmvvm.bean.UserBean;
import com.ljw.selfmvvm.bean.basebean.ParamsBuilder;
import com.ljw.selfmvvm.bean.basebean.Resource;

import java.util.HashMap;
import java.util.List;

/**
 * Create by Ljw on 2019/12/12 17:00
 * 所有网络请求的集合
 * 继承BaseModel，使用里面封装好的方法
 */
public class BaseModelImpl extends BaseModel {

    //获取 banner列表
    public MutableLiveData<Resource<List<BannerBean>>> getBannerList(){
        MutableLiveData<Resource<List<BannerBean>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getBanner(), liveData);
    }

    //获取首页文章
    public MutableLiveData<Resource<HomeCollectionBean>> getHomeArticles(int curPage, ParamsBuilder paramsBuilder){
        MutableLiveData<Resource<HomeCollectionBean>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getHomeArticles(curPage), liveData, paramsBuilder);
    }

    //获取收藏列表
    public MutableLiveData<Resource<HomeCollectionBean>> getCollectArticles(int curPage, ParamsBuilder paramsBuilder) {
        MutableLiveData<Resource<HomeCollectionBean>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getCollectArticles(curPage), liveData, paramsBuilder);
    }

    //站内收藏文章
    public MutableLiveData<Resource<String>> collectArticle(int id) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().collectArticle(id), liveData, ParamsBuilder.builder().isShowDialog(false));//不显示加载logo
    }

    //站外收藏文章
    public MutableLiveData<Resource<String>> collectOutArticle(String title, String author, String link) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().collectOutArticle(title, author, link), liveData, ParamsBuilder.builder().isShowDialog(false));
    }

    //取消收藏 -- 首页列表
    public MutableLiveData<Resource<String>> unCollectByHome(int id) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().unCollectByHome(id), liveData, ParamsBuilder.builder().isShowDialog(false));
    }

    public MutableLiveData<Resource<String>> unCollectByMe(int id, int originId) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().unCollectByMe(id, originId), liveData, null);
    }

    //退出登录
    public MutableLiveData<Resource<String>> LoginOut() {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().loginOut(), liveData);
    }

    //登录
    public MutableLiveData<Resource<UserBean>> login(HashMap<String, Object> map, ParamsBuilder paramsBuilder) {
        MutableLiveData<Resource<UserBean>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().login(map), liveData, paramsBuilder);
    }
}
