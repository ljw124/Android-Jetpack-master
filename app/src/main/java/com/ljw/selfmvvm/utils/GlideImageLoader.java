package com.ljw.selfmvvm.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ljw.selfmvvm.R;
import com.youth.banner.loader.ImageLoader;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Create by Ljw on 2019/12/16 11:5
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path)
//                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .transition(withCrossFade())
                .centerCrop()
                .into(imageView);
    }
}
