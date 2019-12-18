package com.ljw.selfmvvm.ui.home;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lihang.nbadapter.BaseAdapter;
import com.ljw.selfmvvm.R;
import com.ljw.selfmvvm.base.BaseViewHolder;
import com.ljw.selfmvvm.bean.HomeBean;
import com.ljw.selfmvvm.databinding.ItemHomeBinding;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by Ljw on 2019/12/13 15:11
 */
public class HomeAdapter extends BaseAdapter<HomeBean> {
    private View.OnClickListener onClickListener;

    public HomeAdapter(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup viewGroup, int viewType) {
        ItemHomeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_home, viewGroup, false);
        return new BaseViewHolder(binding);
    }

    @Override
    public void onBindMyViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;
        ItemHomeBinding binding = (ItemHomeBinding) baseViewHolder.binding;
        HomeBean itemBean = dataList.get(i);
        binding.txtTitle.setText(itemBean.getTitle());
        if (TextUtils.isEmpty(itemBean.getSuperChapterName())) {
            binding.txtCome.setText("火星");
        } else {
            binding.txtCome.setText(itemBean.getSuperChapterName());
        }
        if (TextUtils.isEmpty(itemBean.getAuthor())) {
            binding.txtAurtor.setText(itemBean.getShareUser());
        } else {
            binding.txtAurtor.setText(itemBean.getAuthor());
        }

        if (itemBean.isCollect()) {
            binding.imageZan.setImageResource(R.mipmap.card_zan_35);
        } else {
            binding.imageZan.setImageResource(R.mipmap.card_zan_1);
        }
        binding.imageZan.setTag(itemBean);
        binding.txtBlogFrom.setText(itemBean.getNiceShareDate());
        binding.imageZan.setOnClickListener(onClickListener);
    }
}
