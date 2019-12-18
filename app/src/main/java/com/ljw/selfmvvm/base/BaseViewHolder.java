package com.ljw.selfmvvm.base;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by Ljw on 2019/12/13 18:11
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    public ViewDataBinding binding;

    public BaseViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

}
