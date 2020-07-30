package com.jfkj.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.jfkj.im.R;


public class SplashAdapter extends RecyclerView.Adapter<SplashAdapter.ViewHolder> {

    private int imgWidth;
    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }


    public SplashAdapter(Context context) {
        imgWidth = getScreenWidth(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_splash, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       /* ViewGroup.LayoutParams lp = holder.item_bg.getLayoutParams();
        lp.width = imgWidth;
        lp.height =imgWidth*5;
        holder.item_bg.setLayoutParams(lp);*/
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView item_bg;

        public ViewHolder(final View itemView) {
            super(itemView);
            item_bg = itemView.findViewById(R.id.item_bg);
        }

    }

}
