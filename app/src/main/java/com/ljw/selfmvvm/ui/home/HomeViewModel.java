package com.ljw.selfmvvm.ui.home;

import android.app.Application;

import com.ljw.selfmvvm.base.BaseModelImpl;
import com.ljw.selfmvvm.base.BaseViewModel;
import com.ljw.selfmvvm.bean.BannerBean;
import com.ljw.selfmvvm.bean.HomeBean;
import com.ljw.selfmvvm.bean.HomeCollectionBean;
import com.ljw.selfmvvm.bean.basebean.ParamsBuilder;
import com.ljw.selfmvvm.bean.basebean.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * Create by Ljw on 2019/12/14 14:11
 * 从每个页面的ViewModel里，就能看出每个页面的功能和联网请求。类似于MVP里的契约类
 */
public class HomeViewModel extends BaseViewModel<BaseModelImpl> {


    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    //获取banner轮播
    public LiveData<Resource<List<BannerBean>>> getBanner() {
        return getModel().getBannerList();
    }

    //获取首页文章
    public LiveData<Resource<HomeCollectionBean>> getHomeArticles(int curPage, ParamsBuilder paramsBuilder) {
        return getModel().getHomeArticles(curPage, paramsBuilder);
    }

    //收藏文章
    public LiveData<Resource<String>> collectArticle(HomeBean data) {
        int id = data.getId();
        //收藏站内文章
        if (id > 0) {
            return collectArticle(id);
        }
        //收藏站外文章
        return getModel().collectOutArticle(data.getTitle(), data.getAuthor(), data.getLink());
    }

    public LiveData<Resource<String>> unCollectByHome(int id) {
        return getModel().unCollectByHome(id);
    }

    //收藏站内文章
    public LiveData<Resource<String>> collectArticle(int id) {
        return getModel().collectArticle(id);
    }

    //收藏站外文章
    public LiveData<Resource<String>> collectOutArticle(String title, String author, String link) {
        return getModel().collectOutArticle(title, author, link);
    }


    //退出登录
    public LiveData<Resource<String>> loginOut() {
        return getModel().LoginOut();
    }

    /*public LiveData<Resource<File>> downFile(String destDir, String fileName) {
        return getModel().downFile(destDir, fileName);
    }

    public LiveData<Resource<String>> uoLoad(HashMap<String, String> map, Map<String, File> files) {
        return getModel().upLoad(map, files);
    }*/

}
