package com.core.compass.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.core.compass.data.model.ThemeModel;
import com.core.compass.utils.CommonUtils;
import com.core.ssvapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Package: com.core.ssvapp.ui.adapter
 * Created by: CuongCK
 * Date: 3/13/18
 */

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ThemeVH> {
    private Context mContext;
    private ArrayList<ThemeModel> themeModels;
    private IThemeListCB mCallback;

    public ThemeAdapter(Context mContext, ArrayList<ThemeModel> themeModels, IThemeListCB mCallback) {
        this.mContext = mContext;
        this.themeModels = themeModels;
        this.mCallback = mCallback;
    }

    @Override
    public ThemeVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ThemeVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme, parent, false));
    }

    @Override
    public void onBindViewHolder(ThemeVH holder, int position) {
        ThemeModel themeModel = themeModels.get(position);

        if (themeModel.isIs_local()) {
            Glide.with(mContext)
                    .load(CommonUtils.getDrawableIDFromName(mContext, themeModel.getImage_theme()))
                    .into(holder.compassImage);
        } else {
            Glide.with(mContext)
                    .load(themeModel.getImage_theme())
                    .into(holder.compassImage);
        }

        Drawable bg = CommonUtils.getDrawableFromName(mContext, themeModel.getItem_bg());
        holder.compassImage.setBackground(bg);
        holder.themeName.setText(themeModel.getThemeName());
        holder.isDefault.setVisibility(themeModel.is_default() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return themeModels.size();
    }

    class ThemeVH extends RecyclerView.ViewHolder {
        @BindView(R.id.theme_compass_image)
        ImageView compassImage;
        @BindView(R.id.theme_name)
        TextView themeName;
        @BindView(R.id.is_default)
        ImageView isDefault;

        public ThemeVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            compassImage.setOnClickListener(view -> {
                if (mCallback != null) {
                    mCallback.onThemeDetail(themeModels.get(getAdapterPosition()).getId());
                }
            });
        }
    }

    public interface IThemeListCB {
        void onThemeDetail(int themeId);
    }
}
