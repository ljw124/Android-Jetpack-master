package com.ljw.selfmvvm.ui.home;

import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.ljw.selfmvvm.MyApplication;
import com.ljw.selfmvvm.R;
import com.ljw.selfmvvm.base.BaseActivity;
import com.ljw.selfmvvm.base.BaseAdapter;
import com.ljw.selfmvvm.base.WebActivity;
import com.ljw.selfmvvm.bean.BannerBean;
import com.ljw.selfmvvm.bean.HomeBean;
import com.ljw.selfmvvm.bean.HomeCollectionBean;
import com.ljw.selfmvvm.bean.basebean.EventBusBean;
import com.ljw.selfmvvm.bean.basebean.ParamsBuilder;
import com.ljw.selfmvvm.custom.iosdialog.DialogUtil;
import com.ljw.selfmvvm.databinding.ActivityHomeBinding;
import com.ljw.selfmvvm.ui.collect.CollectActivity;
import com.ljw.selfmvvm.ui.login.LoginActivity;
import com.ljw.selfmvvm.utils.ActivityUtils;
import com.ljw.selfmvvm.utils.ButtonClickUtils;
import com.ljw.selfmvvm.utils.GlideImageLoader;
import com.ljw.selfmvvm.utils.ToastUtils;
import com.youth.banner.BannerConfig;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Ljw on 2019/12/13 14:11
 */
public class HomeActivity extends BaseActivity<HomeViewModel, ActivityHomeBinding> implements BaseAdapter.OnItemClickListener<HomeBean> {

    private HomeAdapter adapter;
    private int curPage = 0;
    private ArrayList<HomeBean> homeBeans = new ArrayList<>();
    private ArrayList<BannerBean> bannerBeans = new ArrayList<>();
    //沉浸式状态栏
    protected ImmersionBar mImmersionBar;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_home;
    }

    @Override
    protected void processLogic() {
        initView();

        getBanner();
        getHomeArticles(curPage, null);
        adapter = new HomeAdapter(this);
        adapter.setOnItemClickListener(this);
        adapter.setDataList(homeBeans);
        binding.recyclerView.setAdapter(adapter);
    }

    private void initView() {
        EventBus.getDefault().register(this);
        //初始化 ImmersionBar
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
        //关闭手势运动
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        binding.setOnclickListener(this);
        //初始化 Banner
        binding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        binding.banner.setImageLoader(new GlideImageLoader());
    }

    private void getBanner() {
        mViewModel.getBanner().observe(this, resource -> resource.handler(new OnCallBack<List<BannerBean>>() {
            @Override
            public void onSuccess(List<BannerBean> data) {
                bannerBeans.addAll(data);
                updateBanner(data);
            }
        }));
    }

    private void updateBanner(List<BannerBean> data) {
        if (data == null || data.size() <= 0) {
            return;
        }
        List<String> urls = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            urls.add(data.get(i).getImagePath());
            titles.add(data.get(i).getTitle());
        }
        binding.banner.setBannerTitles(titles);
        binding.banner.setImages(urls);
        binding.banner.start();
    }

    @Override
    protected void setListener() {
        binding.banner.setOnBannerListener(position -> {
            startActivity(ActivityUtils.build(this, WebActivity.class)
                    .putExtra("url", bannerBeans.get(position).getUrl()));
        });
        binding.smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            curPage = 0;
            getHomeArticles(curPage, ParamsBuilder.builder().isShowDialog(false));
        });

        binding.smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            curPage++;
            getHomeArticles(curPage, ParamsBuilder.builder().isShowDialog(false));
        });

        binding.titleBar.bar_left_btn.setOnClickListener(this);
        binding.titleBar.bar_right_btn.setOnClickListener(this);

        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                View mContent = binding.drawerLayout.getChildAt(0);
                mContent.setTranslationX(drawerView.getWidth() * slideOffset);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void getHomeArticles(int currenPage, ParamsBuilder paramsBuilder) {
        mViewModel.getHomeArticles(currenPage, paramsBuilder).observe(this, resource -> resource.handler(new OnCallBack<HomeCollectionBean>() {
            @Override
            public void onSuccess(HomeCollectionBean data) {
                if (data.getDatas().size() > 0) {
                    if (currenPage == 0) {
                        homeBeans.clear();
                    }
                    homeBeans.addAll(data.getDatas());
                    adapter.notifyItemRangeChanged(homeBeans.size() - data.getDatas().size(), data.getDatas().size());
                } else {
                    binding.smartRefreshLayout.finishLoadMoreWithNoMoreData();
                }
            }
        }, binding.smartRefreshLayout));
    }

    public void collectArt(HomeBean homeBean) {
        mViewModel.collectArticle(homeBean).observe(this, resource -> {
            resource.handler(new OnCallBack<String>() {
                @Override
                public void onSuccess(String data) {

                }
            });
        });
    }

    public void unCollectArt(int id) {
        mViewModel.unCollectByHome(id).observe(this, resource -> {
            resource.handler(new OnCallBack<String>() {
                @Override
                public void onSuccess(String data) {

                }
            });
        });
    }

    @Override
    public void onClick(View v) {
        if (ButtonClickUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.image_zan:
                if (MyApplication.getLoginUser() == null) {
                    ActivityUtils.startActivity(this, LoginActivity.class);
                    return;
                }
                ImageView imageView = (ImageView) v;
                HomeBean homeBean = (HomeBean) v.getTag();
                if (homeBean.isCollect()) {
                    imageView.setImageResource(R.mipmap.card_zan_1);
                    homeBean.setCollect(false);
                    unCollectArt(homeBean.getId());
                } else {
                    imageView.setImageResource(R.drawable.zan_click_userdetail);
                    AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                    animationDrawable.start();
                    homeBean.setCollect(true);
                    collectArt(homeBean);
                }
                break;
            case R.id.bar_left_btn:
                if (MyApplication.getLoginUser() == null) {
                    ActivityUtils.startActivity(this, LoginActivity.class);
                } else {
                    binding.txtName.setText(MyApplication.getLoginUser().getPublicName());
                    openDrawLayout();
                }
                break;

            case R.id.bar_right_btn:
                ToastUtils.showToast("该功能还在路上");
                break;

            case R.id.txt_loginOut:
                DialogUtil.alertIosDialog(HomeActivity.this, "是否退出登录？", "确定", "取消", () -> {
                    mViewModel.loginOut().observe(this, resource -> {
                        resource.handler(new OnCallBack<String>() {
                            @Override
                            public void onSuccess(String data) {
                                MyApplication.loginOut();
                                binding.drawerLayout.closeDrawer(binding.txtTest);
                                binding.smartRefreshLayout.autoRefresh();
                            }
                        });
                    });
                });
                break;
            case R.id.txt_collect:
                ActivityUtils.startActivity(this, CollectActivity.class);
                break;
        }
    }

    public void openDrawLayout() {
        if (binding.drawerLayout.isDrawerOpen(binding.txtTest)) {
            binding.drawerLayout.closeDrawer(binding.txtTest);
        } else {
            binding.drawerLayout.openDrawer(binding.txtTest);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.banner.stopAutoPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(HomeBean item, int position) {
        startActivity(ActivityUtils.build(this, WebActivity.class)
                .putExtra("url", item.getLink()));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //点击返回键，不退出应用程序。直接返回后台
            ActivityUtils.startHome();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onbackEvent(EventBusBean eventBusBean) {
        switch (eventBusBean.getType()) {
            case 1:
                binding.smartRefreshLayout.autoRefresh();
                break;
            case 2:
                int id = (int) eventBusBean.getValue();
                for (int i = 0; i < homeBeans.size(); i++) {
                    if (homeBeans.get(i).getChapterId() == id) {
                        homeBeans.get(i).setCollect(false);
                        adapter.notifyItemChanged(i);
                    }
                }
                break;
        }
    }
}
