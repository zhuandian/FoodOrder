package com.zhuandian.oderapp.business;

import android.content.Intent;
import android.view.View;

import com.zhuandian.oderapp.MainActivity;
import com.zhuandian.oderapp.R;
import com.zhuandian.oderapp.base.BaseFragment;
import com.zhuandian.oderapp.login.LoginActivity;

import butterknife.OnClick;

/**
 * desc :商家页
 * author：xiedong
 * data：2018/12/29
 */
public class ShopStoreFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shop_store;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.tv_logout})
    public void onClick(View view) {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}
