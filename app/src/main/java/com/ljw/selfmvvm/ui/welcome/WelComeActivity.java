package com.ljw.selfmvvm.ui.welcome;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ljw.selfmvvm.R;
import com.ljw.selfmvvm.base.BaseActivity;
import com.ljw.selfmvvm.base.NormalViewModel;
import com.ljw.selfmvvm.databinding.ActivityWelcomBinding;
import com.ljw.selfmvvm.ui.home.HomeActivity;
import com.ljw.selfmvvm.utils.ActivityUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Create by Ljw on 2019/12/12 11:11
 */
public class WelComeActivity extends BaseActivity<NormalViewModel, ActivityWelcomBinding> {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_welcom;
    }

    @Override
    protected void processLogic() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha_welcome);
        binding.txtTitle.startAnimation(animation);

        binding.imgSvg.setViewportSize(167, 220);
        binding.imgSvg.setTraceColor(getResources().getColor(R.color.white));
        binding.imgSvg.start();

        Observable.timer(2500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
            ActivityUtils.startActivity(WelComeActivity.this, HomeActivity.class);
            ActivityUtils.finishWithAnim(WelComeActivity.this,0,R.animator.set_anim_activity_exit);
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
