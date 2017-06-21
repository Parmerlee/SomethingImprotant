package com.bonc.mobile.hbmclient.spring;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.common.util.NumSdf;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.MainKpiActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/12.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements View.OnClickListener {

    private LayoutInflater mInflater;
    private List<Map<String, String>> list;

    private int itemWidth;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public RecyclerAdapter(Context context) {

        this.mInflater = LayoutInflater.from(context);
    }

    public RecyclerAdapter(Context context, List<Map<String, String>> list) {

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        if (width == 720) {
            itemWidth = (width) / 2;
        } else {
            itemWidth = (width) / 2 + 1;
        }
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }


    //view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.top10_item_layout_spring, viewGroup, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(itemWidth,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        lp.setMargins(5, 0, 0, 0);
        view.setLayoutParams(lp);

        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    //bind date
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.item_rank.setText(list.get(position).get("rank").toString());
        viewHolder.item_Name.setText(list.get(position).get("modelName").toString());
        viewHolder.item_value1.setText(NumSdf.changeData(list.get(position).get("value1").toString()));
        viewHolder.item_value2.setText((NumSdf.changeData(list.get(position).get("value2").toString())));
        viewHolder.item_value3.setText((NumSdf.changeData(list.get(position).get("value3").toString())));
        viewHolder.item_title.setText(list.get(position).get("top10").toString());
        viewHolder.itemView.setTag(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (Map<String, String>) v.getTag());
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_Name;
        public TextView item_value1;
        public TextView item_value2;
        public TextView item_value3;
        public TextView item_rank;
        public TextView item_title;

        public ViewHolder(View view) {
            super(view);
            item_Name = (TextView) view.findViewById(R.id.id_name);
            item_value1 = (TextView) view.findViewById(R.id.id_value1);
            item_value2 = (TextView) view.findViewById(R.id.id_value2);
            item_value3 = (TextView) view.findViewById(R.id.id_value3);
            item_rank = (TextView) view.findViewById(R.id.id_rank);
            item_title = (TextView) view.findViewById(R.id.id_title);
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Map data);
    }

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}

