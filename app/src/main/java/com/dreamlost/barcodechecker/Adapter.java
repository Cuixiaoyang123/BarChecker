package com.dreamlost.barcodechecker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dreamlost.barcodechecker.bean.MissionBean;
import com.dreamlost.barcodechecker.bean.MissionDetailBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by DreamLost on 2020/9/28 at 23:45
 * Description:
 */
public class Adapter extends RecyclerView.Adapter<Adapter.DataViewHolder> {
    private Context mContext;
    private List<String> missionPath;
    private List<MissionBean> missionBean;
    private List<MissionDetailBean> missionDetailBean;

    public Adapter() {
    }

    public Adapter(Context mContext, List<String> missionPath, List<MissionBean> missionBeanList, List<MissionDetailBean> missionDetailBeanList) {
        this.mContext = mContext;
        this.missionPath = missionPath;
        this.missionBean = missionBeanList;
        this.missionDetailBean = missionDetailBeanList;
    }

    //用于创建ViewHolder
    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_mission, null);
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
        String s = missionPath.get(position);
        holder.missionData.setText(s);
        holder.maker.setText(missionBean.get(position).getMaker());
        holder.makeTime.setText(missionBean.get(position).getMakeTime());
        holder.carryTime.setText(missionBean.get(position).getCarryTime());
        holder.deadline.setText(missionBean.get(position).getDeadline());
        holder.comment.setText(missionBean.get(position).getComment());
        holder.checker.setText(missionDetailBean.get(position).getChecker());
        holder.result.setText(missionDetailBean.get(position).getResult());
        holder.name.setText(missionDetailBean.get(position).getArea().getName());
        holder.code.setText(missionDetailBean.get(position).getArea().getCode());



    }

    //数据总数
    @Override
    public int getItemCount() {
        return missionBean.size();
    }

    //创建ViewHolder
    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private final TextView maker;
        private final TextView makeTime;
        private final TextView carryTime;
        private final TextView deadline;
        private final TextView comment;
        private final TextView checker;
        private final TextView result;
        private final TextView name;
        private final TextView code;
        private final TextView missionData;

        public DataViewHolder(View dialogView) {
            super(dialogView);
            missionData = dialogView.findViewById(R.id.missionDate);
            maker = dialogView.findViewById(R.id.maker);
            makeTime = dialogView.findViewById(R.id.makeTime);
            carryTime = dialogView.findViewById(R.id.carryTime);
            deadline = dialogView.findViewById(R.id.deadline);
            comment = dialogView.findViewById(R.id.comment);
            checker = dialogView.findViewById(R.id.checker);
            result = dialogView.findViewById(R.id.result);
            name = dialogView.findViewById(R.id.name);
            code = dialogView.findViewById(R.id.code);

        }
    }
}