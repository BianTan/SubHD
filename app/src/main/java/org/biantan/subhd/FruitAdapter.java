package org.biantan.subhd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

    private Context mContext;
    private List<Fruit> mFruitList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View fruitView;
        TextView fruittitle;
        TextView fruitname;
        TextView fruitinfo;
        TextView fruitsuccess;
        TextView fruitprimary;
        LinearLayout fruitllinfo;
        LinearLayout fruitllprimary;
        LinearLayout fruitllsuccess;
        ImageView fruitimgurl;
        LinearLayout fruitsteam;

        public ViewHolder(View view) {
            super(view);
            fruitView = view;
            fruittitle = (TextView) view.findViewById(R.id.textView);
            fruitname = (TextView) view.findViewById(R.id.textView2);
            fruitinfo = (TextView) view.findViewById(R.id.info);
            fruitprimary = (TextView) view.findViewById(R.id.primary);
            fruitsuccess = (TextView) view.findViewById(R.id.success);
            fruitllinfo = (LinearLayout) view.findViewById(R.id.llinfo);
            fruitllprimary = (LinearLayout) view.findViewById(R.id.llprimary);
            fruitllsuccess = (LinearLayout) view.findViewById(R.id.llsuccess);
            fruitimgurl = (ImageView) view.findViewById(R.id.imageView);
            fruitsteam = (LinearLayout) view.findViewById(R.id.steam);
        }
    }

    public FruitAdapter(Context context, List<Fruit> fruitList) {
        mContext = context;
        mFruitList = fruitList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        //点击事件
        holder.fruitsteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Fruit fruit = mFruitList.get(position);
                Intent intent = new Intent(mContext, ScrollingActivity.class);
                intent.putExtra("dz1", fruit.gettitle() + "+");
                intent.putExtra("dz2", fruit.getName());
                intent.putExtra("dz3", fruit.gettargetUrl());
                intent.putExtra("dz4", fruit.getimgurl());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fruit fruit = mFruitList.get(position);
        holder.fruittitle.setText(fruit.gettitle());
        holder.fruitname.setText(fruit.getName());
        if (fruit.getinfo() != "") {
            holder.fruitinfo.setText(fruit.getinfo());
        } else {
            holder.fruitllinfo.setVisibility(View.GONE);
        }
        if (fruit.getprimary() != "") {
            holder.fruitprimary.setText(fruit.getprimary());
        } else {
            holder.fruitllprimary.setVisibility(View.GONE);
        }
        if (fruit.getsuccess() != "") {
            holder.fruitsuccess.setText(fruit.getsuccess());
        } else {
            holder.fruitllsuccess.setVisibility(View.GONE);
        }
        Glide.with(mContext)
                .load(fruit.getimgurl())
                .placeholder(R.drawable.movie_default_large)//图片加载出来前，显示的图片
                .error(R.drawable.movie_default_large)//图片加载失败后，显示的图片
                .crossFade()//淡入淡出动画
                .fitCenter()//等比拉伸
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存源资源和转换后的资源
                .into(holder.fruitimgurl);
    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }
}