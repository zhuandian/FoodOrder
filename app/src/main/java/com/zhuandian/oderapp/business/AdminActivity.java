package com.zhuandian.oderapp.business;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhuandian.oderapp.R;
import com.zhuandian.oderapp.Utils.PictureSelectorUtils;
import com.zhuandian.oderapp.base.BaseActivity;
import com.zhuandian.oderapp.entity.CategoryEntity;
import com.zhuandian.oderapp.entity.FoodEntity;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.http.I;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class AdminActivity extends BaseActivity {


    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.et_shop_name)
    EditText etShopName;
    private String imgUrl;
    private String typeName;
    private int type;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_admin;
    }

    @Override
    public void initView() {

    }



    @OnClick({R.id.iv_img, R.id.tv_type, R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_img:
                PictureSelectorUtils.selectImg(PictureSelector.create(this), 1);
                break;
            case R.id.tv_type:
                showTypeDialog();
                break;
            case R.id.tv_commit:
                commitFood();
                break;
        }
    }

    private void commitFood() {
        String name = etName.getText().toString();
        String desc = etDesc.getText().toString();
        String price = etPrice.getText().toString();
        String shopName = etShopName.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(price)|| TextUtils.isEmpty(shopName)) {
            Toast.makeText(this, "请完善信息", Toast.LENGTH_SHORT).show();
            return;
        }

        FoodEntity foodEntity = new FoodEntity();
        foodEntity.setTypeName(typeName);
        foodEntity.setFoodImgUrl(imgUrl);
        foodEntity.setFoodDesc(desc);
        foodEntity.setFoodName(name);
        foodEntity.setFoodPrice(Integer.parseInt(price));
        foodEntity.setFoodType(type);
        foodEntity.setShopName(shopName);

        foodEntity.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(AdminActivity.this, "上传成功...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AdminActivity.this, AdminActivity.class));
                    finish();
                }
            }
        });
    }

    private void showTypeDialog() {

        BmobQuery<CategoryEntity> query = new BmobQuery();
        query.findObjects(new FindListener<CategoryEntity>() {
            @Override
            public void done(List<CategoryEntity> typeList, BmobException e) {
                if (e == null) {

                    String[] items = new String[typeList.size()];
                    for (int i = 0; i < typeList.size(); i++) {
                        items[i] = typeList.get(i).getName();
                    }
                    new AlertDialog.Builder(AdminActivity.this)
                            .setTitle("选择分类")
                            .setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    typeName = typeList.get(which).getName();
                                    type = typeList.get(which).getType();
                                    dialog.dismiss();
                                    tvType.setText(typeName);
                                }
                            }).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList.size() > 0) {
                    String imagePath = selectList.get(0).getCompressPath();
                    decodePath2Bitmap(imagePath);
                    BmobFile bmobFile = new BmobFile(new File(imagePath));
                    bmobFile.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                imgUrl = bmobFile.getFileUrl();
                            }
                        }
                    });
                }
            }
        }
    }


    /**
     * 把指定路径的image资源转成Bitmap
     *
     * @param path
     */
    private void decodePath2Bitmap(String path) {
        Bitmap bm = BitmapFactory.decodeFile(path);
        if (bm != null) {
            ivImg.setImageBitmap(bm);
        }
    }
}
