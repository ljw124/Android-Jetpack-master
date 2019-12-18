package com.ljw.selfmvvm.ui.collect;

import android.view.View;
import com.lihang.nbadapter.BaseAdapter;
import com.ljw.selfmvvm.R;
import com.ljw.selfmvvm.base.BaseActivity;
import com.ljw.selfmvvm.base.WebActivity;
import com.ljw.selfmvvm.bean.HomeBean;
import com.ljw.selfmvvm.bean.HomeCollectionBean;
import com.ljw.selfmvvm.bean.basebean.EventBusBean;
import com.ljw.selfmvvm.bean.basebean.ParamsBuilder;
import com.ljw.selfmvvm.databinding.ActivityCollectBinding;
import com.ljw.selfmvvm.utils.ActivityUtils;
import com.ljw.selfmvvm.utils.CommonUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;

/**
 * Create by Ljw on 2019/12/14 12:11
 */
public class CollectActivity extends BaseActivity<CollectViewModel, ActivityCollectBinding> implements BaseAdapter.OnItemClickListener<HomeBean> {
    private CollectAdapter adapter;
    private int curPage = 0;
    private ArrayList<HomeBean> homeBeans = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_collect;
    }

    @Override
    protected void processLogic() {
        getCollects(curPage, null);
        adapter = new CollectAdapter(this);
        adapter.setOnItemClickListener(this);
        adapter.setDataList(homeBeans);
        binding.recyclerView.setAdapter(adapter);
    }

    private void getCollects(int curPage, ParamsBuilder paramsBuilder) {
        mViewModel.getCollectArticles(curPage, paramsBuilder).observe(this, stringResource -> {
            stringResource.handler(new OnCallBack<HomeCollectionBean>() {
                @Override
                public void onSuccess(HomeCollectionBean data) {
                    if (data.getDatas().size() > 0) {
                        if (curPage == 0) {
                            homeBeans.clear();
                        }
                        homeBeans.addAll(data.getDatas());
                        adapter.notifyItemRangeChanged(homeBeans.size() - data.getDatas().size(), data.getDatas().size());
                    } else {
                        binding.smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                    CommonUtils.checkEmpty(homeBeans.size(), binding.empty);
                }
            }, binding.smartRefreshLayout);
        });
    }

    @Override
    protected void setListener() {
        binding.titleBar.bar_left_btn.setOnClickListener(this);
        binding.smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            curPage = 0;
            getCollects(curPage, ParamsBuilder.builder().isShowDialog(false));
        });

        binding.smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            curPage++;
            getCollects(curPage, ParamsBuilder.builder().isShowDialog(false));
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bar_left_btn:
                finish();
                break;
            case R.id.image_zan:
                HomeBean homeBean = (HomeBean) v.getTag(R.id.image_zan);
                int position = (int) v.getTag(R.id.linear_);
                mViewModel.unCollectByMe(homeBean.getId(), homeBean.getOriginId()).observe(this, resource -> {
                    resource.handler(new OnCallBack<String>() {
                        @Override
                        public void onSuccess(String data) {
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(position,adapter.getItemCount());
                            homeBeans.remove(homeBean);
                            EventBus.getDefault().post(new EventBusBean(2, homeBean.getChapterId()));
                            CommonUtils.checkEmpty(homeBeans.size(), binding.empty);
                        }
                    });
                });
                break;
        }
    }

    @Override
    public void onItemClick(HomeBean item, int position) {
        startActivity(ActivityUtils.build(this, WebActivity.class)
                .putExtra("url", item.getLink()));
    }
}
