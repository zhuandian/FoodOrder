package com.zhuandian.oderapp.adpter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuandian.oderapp.R;
import com.zhuandian.oderapp.entity.CategoryEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * desc :类别列表adapter
 * author：xiedong
 * data：2018/12/28
 */
public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    private List<CategoryEntity> mDatas;
    private Context context;
    private ItemClickListener itemClickListener;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CategoryListAdapter(Context context, List<CategoryEntity> mDatas) {
        this.mDatas = mDatas;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.tvCategory.setText(mDatas.get(i).getName());
        viewHolder.tvCategory.setBackgroundColor(mDatas.get(i).isSelected() ? Color.parseColor("#f8f8f8") : Color.parseColor("#ffffff"));
        viewHolder.tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.itemClick(mDatas.get(i).getType());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_category)
        TextView tvCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemClickListener {
        void itemClick(int foodType);
    }

}
