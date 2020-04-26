package com.zhuandian.oderapp.business;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuandian.oderapp.R;
import com.zhuandian.oderapp.base.BaseActivity;
import com.zhuandian.oderapp.entity.BillEntity;
import com.zhuandian.oderapp.entity.FoodEntity;
import com.zhuandian.oderapp.entity.OrderEntity;
import com.zhuandian.oderapp.entity.UserEntity;
import com.zhuandian.oderapp.view.OrderItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class OrderActivity extends BaseActivity {

    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_to_order_page)
    TextView tvToOrderPage;
    @BindView(R.id.ll_order_container)
    LinearLayout llOrderContainer;
    private List<FoodEntity> shopCarList;
    private List<OrderEntity> orderEntityList;
    public static final int REQUEST_OPEN_ORDER_PAGE = 1;
    public static final int REQUEST_CLOSE_ORDER_PAGE = 2;
    private double totalPrice;
    private List<String> foodList;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    public void initView() {
        shopCarList = new ArrayList<>();
        orderEntityList = new ArrayList<>();
        shopCarList = (List<FoodEntity>) getIntent().getSerializableExtra("data");
        for (int i = 0; i < shopCarList.size(); i++) {

            boolean isAdd = false;
            if (orderEntityList.size() == 0) {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setFoodId(shopCarList.get(i).getObjectId());
                orderEntity.setFoodUrl(shopCarList.get(i).getFoodImgUrl());
                orderEntity.setOrderName(shopCarList.get(i).getFoodName());
                orderEntity.setOrderPrice(shopCarList.get(i).getFoodPrice());
                orderEntity.setOrderCount(1);
                orderEntityList.add(orderEntity);
            } else {
                for (int j = 0; j < orderEntityList.size(); j++) {
                    if (shopCarList.get(i).getObjectId().equals(orderEntityList.get(j).getFoodId())) {
                        int currentCount = orderEntityList.get(j).getOrderCount() + 1;
                        orderEntityList.get(j).setOrderCount(currentCount);
                        isAdd = true;
                    }
                }
                if (!isAdd) {
                    OrderEntity orderEntity = new OrderEntity();
                    orderEntity.setFoodId(shopCarList.get(i).getObjectId());
                    orderEntity.setFoodUrl(shopCarList.get(i).getFoodImgUrl());
                    orderEntity.setOrderName(shopCarList.get(i).getFoodName());
                    orderEntity.setOrderPrice(shopCarList.get(i).getFoodPrice());
                    orderEntity.setOrderCount(1);
                    orderEntityList.add(orderEntity);
                }


            }
        }
        llOrderContainer.removeAllViews();
         totalPrice = 0;
         foodList = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntityList) {
            totalPrice += orderEntity.getOrderPrice() * orderEntity.getOrderCount();
            foodList.add(orderEntity.getOrderName());
            OrderItemView itemView = new OrderItemView(this);
            itemView.setItemData(this, orderEntity);
            llOrderContainer.addView(itemView);
        }
        tvTotalPrice.setText(String.format("￥%.2f", totalPrice));
    }


    @OnClick(R.id.tv_to_order_page)
    public void onClick() {

        BillEntity billEntity = new BillEntity();
        billEntity.setTotalPrice(totalPrice);
        billEntity.setUserEntity(BmobUser.getCurrentUser(UserEntity.class));
        billEntity.setFoods(foodList);
        billEntity.setShopName(shopCarList.get(0).getShopName());
        billEntity.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                    builder.setTitle("下单成功")
                            .setMessage("下单成功，祝您用餐愉快!!")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setResult(REQUEST_CLOSE_ORDER_PAGE);
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });

    }
}
