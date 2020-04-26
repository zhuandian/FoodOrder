package com.zhuandian.oderapp.business;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zhuandian.oderapp.MainActivity;
import com.zhuandian.oderapp.R;
import com.zhuandian.oderapp.base.BaseActivity;
import com.zhuandian.oderapp.entity.FoodEntity;
import java.util.List;
import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ChoseShopActivity extends BaseActivity {


    @BindView(R.id.rg_shop)
    RadioGroup rgShop;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chose_shop;
    }

    @Override
    public void initView() {

         findViewById(R.id.tv_chose_shop).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 getShopList();
             }
         });
        getShopList();
    }

    private void getShopList() {
        BmobQuery<FoodEntity> query = new BmobQuery<>();
        query.findObjects(new FindListener<FoodEntity>() {
            @Override
            public void done(List<FoodEntity> list, BmobException e) {
                if (e == null) {

                    rgShop.removeAllViews();
                    for (int i = 0; i < list.size(); i++) {
                        if (!TextUtils.isEmpty(list.get(i).getShopName())) {
                            RadioButton radioButton = new RadioButton(ChoseShopActivity.this);
                            radioButton.setText(list.get(i).getShopName());
                            int finalI = i;
                            radioButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChoseShopActivity.this, MainActivity.class);
                                    intent.putExtra("shopName",list.get(finalI).getShopName());
                                    startActivity(intent);
                                    finish();
                                }
                            });

                            rgShop.addView(radioButton);
                        }
                    }
                }
            }
        });
    }

}
