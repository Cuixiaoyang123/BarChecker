package com.dreamlost.barcodechecker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamlost.barcodechecker.bean.FireRecordBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by DreamLost on 2020/9/28 at 23:45
 * Description:
 */
public class RvAdapter extends RecyclerView.Adapter<RvAdapter.DataViewHolder> {
    private Context mContext;
    private List<FireRecordBean.DataBean> bean;

    public RvAdapter() {
    }

    public RvAdapter(Context mContext, List<FireRecordBean.DataBean> bean) {
        this.mContext = mContext;
        this.bean = bean;
    }

    //用于创建ViewHolder
    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item, null);
        //使用代码设置宽高（xml布局设置无效时）
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        DataViewHolder holder = new DataViewHolder(view);
        return holder;
    }

    //绑定数据
    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        holder.tv_code.setText(bean.get(position).getPositionId().getCode());
        holder.tv_name.setText(bean.get(position).getPositionId().getName());
        if (bean.get(position).getFinished()) {
            holder.tv_finished.setText("已完成");
            holder.tv_finished.setBackgroundColor(mContext.getResources().getColor(R.color.colorPreGreen));
        } else {
            holder.tv_finished.setText("未完成");
            holder.tv_finished.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        }
    }

    //数据总数
    @Override
    public int getItemCount() {
        return bean.size();
    }

    //创建ViewHolder
    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tv_finished,tv_code,tv_name;

        public DataViewHolder(View itemView) {
            super(itemView);
            tv_finished = (TextView) itemView.findViewById(R.id.tv_finished);
            tv_code = (TextView) itemView.findViewById(R.id.tv_code);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}