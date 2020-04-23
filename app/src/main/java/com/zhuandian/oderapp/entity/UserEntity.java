package com.zhuandian.oderapp.entity;

import cn.bmob.v3.BmobUser;

/**
 * desc :用户实体
 * author：xiedong
 * data：2018/12/28
 */
public class UserEntity extends BmobUser {
    private int type;  // 1 商家  0 用户

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
