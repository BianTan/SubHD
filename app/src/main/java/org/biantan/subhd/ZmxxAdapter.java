package org.biantan.subhd;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ZmxxAdapter extends RecyclerView.Adapter<ZmxxAdapter.ViewHolder> {

    private Context mContext;
    private List<Zmxx> mZmxxList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View zmxxView;
        TextView zmxxzmm;
        TextView zmxxzmdx;

        public ViewHolder(View view) {
            super(view);
            zmxxView = view;
            zmxxzmm = (TextView) view.findViewById(R.id.zmm);
            zmxxzmdx = (TextView) view.findViewById(R.id.zmdx);
        }
    }

    public ZmxxAdapter(Context context, List<Zmxx> zmxxList) {
        mContext = context;
        mZmxxList = zmxxList;
    }

    public ZmxxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zmwj_item, parent, false);
        final ZmxxAdapter.ViewHolder holder = new ZmxxAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Zmxx zmxx = mZmxxList.get(position);
        holder.zmxxzmm.setText(zmxx.getzmm());
        holder.zmxxzmdx.setText(zmxx.getzmdx());
    }

    @Override
    public int getItemCount() {
        return mZmxxList.size();
    }
}