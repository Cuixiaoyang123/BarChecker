package com.dreamlost.barcodechecker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamlost.barcodechecker.bean.MissionBean;
import com.dreamlost.barcodechecker.bean.MissionDetailBean;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MissionListActivity extends AppCompatActivity {
    private static final String TAG = "MissionListActivity";
    private String dirBase = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ftmtp/";
    private RecyclerView listView;
    private String currentDirPath;
    private List<String> currentDataPathList = new ArrayList<>();
    private Adapter adapter;
    private MissionBean missionBean;
    private List<MissionBean> missionBeanList = new ArrayList<>();
    private MissionDetailBean missionDetailBean;
    private List<MissionDetailBean> missionDetailBeanList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_list);

        final String[] missions = Utils.getAllSubFiles(dirBase);

        if (null != missions) {
            for (int i = 0; i < missions.length; i++) {
                if (checkFileComplete(missions[i])) {
                    exportMission(missions[i]);
                    currentDataPathList.add(missions[i]);
                }
            }

            //在视图中找到ListView
            listView = (RecyclerView) findViewById(R.id.listView);
            //设置布局管理器
            listView.setLayoutManager(new LinearLayoutManager(this));//线性
             adapter = new Adapter(this,currentDataPathList,missionBeanList,missionDetailBeanList);
            listView.setAdapter(adapter);

//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                    if (checkFileComplete(missions[i])) {
//                        boolean b = exportMission(missions[i]);
//                        if (b) {
//                            justShowMissionDialog(missions[i]);
//                        }
//
//                    } else {
//                        Toast.makeText(getApplicationContext(), "文件[" + missions[i] + "]不完整", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            });
        } else {
            showNormalDialog();
        }

    }

//    private void justShowMissionDialog(final String day) {
//
//        AlertDialog.Builder customizeDialog =
//                new AlertDialog.Builder(MissionListActivity.this);
//        final View dialogView = LayoutInflater.from(MissionListActivity.this)
//                .inflate(R.layout.dialog_mission,null);
//        customizeDialog.setTitle("请核查此次巡检任务的相关信息");
//        customizeDialog.setView(dialogView);
//        customizeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        customizeDialog.setPositiveButton("开始巡检",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(MissionListActivity.this, SearchActivity.class);
//                        intent.putExtra("filePath", day);
//                        intent.putExtra("missionId", missionDetailBean.getMissionId());
//                        intent.putExtra("checker", missionDetailBean.getChecker());
//                        startActivity(intent);
//                    }
//                });
//
//        TextView maker = dialogView.findViewById(R.id.maker);
//        maker.setText(missionBean.getMaker());
//
//        TextView makeTime = dialogView.findViewById(R.id.makeTime);
//        makeTime.setText(missionBean.getMakeTime());
//
//        TextView carryTime = dialogView.findViewById(R.id.carryTime);
//        carryTime.setText(missionBean.getCarryTime());
//
//        TextView deadline = dialogView.findViewById(R.id.deadline);
//        deadline.setText(missionBean.getDeadline());
//
//        TextView comment = dialogView.findViewById(R.id.comment);
//        comment.setText(missionBean.getComment());
//
//        TextView checker = dialogView.findViewById(R.id.checker);
//        checker.setText(missionDetailBean.getChecker());
//
//        TextView result = dialogView.findViewById(R.id.result);
//        result.setText(missionDetailBean.getResult());
//
//        TextView name = dialogView.findViewById(R.id.name);
//        name.setText(missionDetailBean.getArea().getName());
//
//        TextView code = dialogView.findViewById(R.id.code);
//        code.setText(missionDetailBean.getArea().getCode());
//
//        customizeDialog.show();
//
//    }

    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MissionListActivity.this);
        normalDialog.setTitle("文件缺失");
        normalDialog.setCancelable(false);
        normalDialog.setMessage("暂无灭火器检查任务，请先导入灭火器巡检任务！");
        normalDialog.setNegativeButton("关闭应用",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
        // 显示
        normalDialog.show();
    }

    /**
     *
     * @param missionPath Mission 的文件夹
     * @return 是否导入成功
     */
    public boolean exportMission(String missionPath) {

        String[] sub = Utils.getAllSubFiles(dirBase + "/" + missionPath + "/mission");
        if (sub == null) {
            return false;
        }
        String missionJson = Utils.getFileFromSD(dirBase + "/" + missionPath + "/mission/"+sub[0]);//bug
        missionBean = new Gson().fromJson(missionJson, MissionBean.class);
        missionBeanList.add(missionBean);

        String[] subs = Utils.getAllSubFiles(dirBase + "/" + missionPath + "/missionDetail");
        if (subs == null) {
            return false;
        }
        String missionDetailJson = Utils.getFileFromSD(dirBase + "/" + missionPath + "/missionDetail/"+subs[0]);//bug
        missionDetailBean = new Gson().fromJson(missionDetailJson, MissionDetailBean.class);
        missionDetailBeanList.add(missionDetailBean);
        currentDirPath = dirBase + "/" + missionPath+"/";
        return true;
    }

    public boolean checkFileComplete(String dir) {
        File file = new File(dirBase + dir + "/fireRecord/fireRecord.json");
        if (!file.exists()) {
            return false;
        }
        File file1 = new File(dirBase + dir + "/mission");
        if (!file1.exists()) {
            return false;
        }
        File file2 = new File(dirBase + dir + "/missionDetail");
        if (!file2.exists()) {
            return false;
        }
        String[] allSubFiles = Utils.getAllSubFiles(dirBase + dir + "/mission");
        if (null == allSubFiles) {
            return false;
        }

        String[] allSubFiles1 = Utils.getAllSubFiles(dirBase + dir + "/missionDetail");
        if (null == allSubFiles1) {
            return false;
        }
        return true;
    }

    public void onStart(View view) {
        int position = listView.getChildAdapterPosition((TableLayout) view.getParent());
        Intent intent = new Intent(MissionListActivity.this, SearchActivity.class);
        intent.putExtra("filePath", currentDataPathList.get(position));
        intent.putExtra("missionId", missionDetailBeanList.get(position).getMissionId());
        intent.putExtra("checker", missionDetailBeanList.get(position).getChecker());
        startActivity(intent);
    }
}
