package com.dreamlost.barcodechecker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamlost.barcodechecker.bean.FireRecordBean;
import com.dreamlost.barcodechecker.bean.MissionBean;
import com.dreamlost.barcodechecker.bean.MissionDetailBean;
import com.dreamlost.barcodechecker.bean.RecordBean;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SearchActivity";

    private static final int REQUEST_CODE_SCAN = 1;
    private String dirBase = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ftmtp/";
    private String currentDirPath;
    private Button btn_submit;
    private Context mContext;
    private EditText editText;
    private FireRecordBean fireRecordBean;
    private HashMap<String, FireRecordBean.DataBean> map = new HashMap<>();
    private long missionId = 222232;
    private MissionBean missionBean;
    private MissionDetailBean missionDetailBean;
    private RecyclerView rv;
    private RvAdapter adapter;
    private List<FireRecordBean.DataBean> data;
    private EditText commentRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        currentDirPath =dirBase + getIntent().getStringExtra("filePath")+"/";

        initView();

        importFiles();

        adapter = new RvAdapter(this,data);
        rv.setAdapter(adapter);

//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                Log.i("cxy", "afterTextChanged: "+editable);
//                Log.i("cxy", "onEditorAction: "+editText.getText().toString());
//            }
//        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    scanBarcode();
                    return true;
                }
                return false;
            }
        });



    }

    @Override
    protected void onStop() {
        if (fireRecordBean != null) {
            String content = new Gson().toJson(fireRecordBean);
            saveFire(content);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        editText = findViewById(R.id.et_result);
//        result = findViewById(R.id.tv_result);
//        text = findViewById(R.id.tv_context);
//        btn_mission = findViewById(R.id.btn_mission);
        btn_submit = findViewById(R.id.btn_submit);
//        btn_scan = findViewById(R.id.btn_scan);
//        btn_export = findViewById(R.id.btn_export);
//        btn_mission.setOnClickListener(this);
//        btn_export.setOnClickListener(this);
//        btn_scan.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        rv = (RecyclerView) findViewById(R.id.rv);
        //设置布局管理器
        rv.setLayoutManager(new LinearLayoutManager(this));//线性
//        rv.setLayoutManager(new GridLayoutManager(this,4));//线性
//        rv.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));//线性

    }


    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public String saveFire(String content) {
        return saveToLocal("fireRecord", content, "fireRecord");
    }

    public String saveMission(String name, String content) {
        return saveToLocal(name, content, "mission");
    }

    public String saveMissionDetail(String name, String content) {
        String s = saveToLocal(name, content, "missionDetail");
        if (s.equals("")) {
            Toast.makeText(mContext, "Config保存失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "保存Config成功 path:" + s, Toast.LENGTH_SHORT).show();
        }
        return s;
    }

    public String saveInspect(String name, String content) {
        String s = saveToLocal(name, content, "inspect");
        if (s.equals("")) {
            Toast.makeText(mContext, "Config保存失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "保存Config成功 path:" + s, Toast.LENGTH_SHORT).show();
        }
        return s;
    }

    public String saveToLocal(String name,String content,String path) {
        File dir = null;
        if (currentDirPath.equals("")) {
            String day = Utils.getCurrentDay();
            //文件夹路径
            dir = new File(Environment.getExternalStorageDirectory() + "/ftmtp/"+day+"/"+path+"/");
        } else {
            //文件夹路径
            dir = new File(currentDirPath + path + "/");

        }
        String day = currentDirPath;
        Log.i(TAG, "saveToLocal: " + day);
        //文件名
        String fileName = name+".json";
        try {
            //文件夹不存在和传入的value值为1时，才允许进入创建
            if (!dir.exists()) {
                //创建文件夹
                dir.mkdirs();
            }

                File file = new File(dir, fileName);
                OutputStream out = new FileOutputStream(file);
                out.write(content.getBytes());
                out.close();
//                Toast.makeText(mContext, "保存Config成功 path:" + file.getPath(), Toast.LENGTH_SHORT).show();
                return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(mContext, "Config保存失败", Toast.LENGTH_SHORT).show();
        }
        return "";
    }


    private String writeJsonToSD(String path) {
        StringBuilder result = new StringBuilder();

        try {
            FileInputStream f = new FileInputStream(path);
            BufferedReader bis = new BufferedReader(new InputStreamReader(f));
            String line = "";
            while ((line = bis.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                boolean isComplete = checkCompletion();
                if (isComplete && missionBean != null && missionDetailBean != null) {
                    showMissionDialog(missionBean, missionDetailBean);
                } else {
                    Toast.makeText(mContext, "请完成所有任务再提交", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public void scanBarcode(){
        //处理事件
        String barcode = editText.getText().toString();
        Log.i("cxy", "onEditorAction: " + barcode);
        FireRecordBean.DataBean dataBean = searchFire(barcode);
        if (dataBean != null) {
            RecordBean recordBean = generateRecord(dataBean.getId(), missionId);
            showFireDialog(dataBean,recordBean);
//                    String content = new Gson().toJson(recordBean);
//                    saveToLocal(name, content);
        }else{
            Toast.makeText(mContext, "条码错误", Toast.LENGTH_SHORT).show();
        }
        editText.setText(null);
    }

    private void importFiles() {
        PermissionUtils.verifyCameraPermissions(SearchActivity.this);

        boolean b = exportMission(dirBase);

        if (!b) {
            Toast.makeText(mContext, "导入Mission失败，请检查存储目录", Toast.LENGTH_SHORT).show();
            return;
        }
        String json = Utils.getFileFromSD(currentDirPath+"/fireRecord/fireRecord.json");
        if (json.equals("")) {
            Toast.makeText(mContext, "导入灭火器数据失败，请检查存储目录", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        fireRecordBean = new Gson().fromJson(json, FireRecordBean.class);

        if (fireRecordBean != null) {
            data = fireRecordBean.getData();
            if (data.size() != 0) {
                for (FireRecordBean.DataBean sub : data) {
                    String key = sub.getBarcode();
                    map.put(key, sub);
                    Log.i("cxy", "onCreate: " + sub.toString());
                    stringBuilder.append(sub.toString());
                    stringBuilder.append("\n");
                }
            }
        }
    }


    private boolean checkCompletion() {
        if (map == null) {
            return false;
        }
        Iterator<Map.Entry<String, FireRecordBean.DataBean>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            //存在灭火器未检查
            if (!iterator.next().getValue().getFinished()) {
                return false;
            }
        }
        return true;
    }

    private RecordBean generateRecord(long id, long missionId) {
        RecordBean recordBean = new RecordBean();
        recordBean.setFireId(id);
        recordBean.setMissionId(missionId);
        return recordBean;
    }


    private FireRecordBean.DataBean searchFire(String barcode) {
        FireRecordBean.DataBean dataBean = map.get(barcode);
        return dataBean;
    }

    private void showMissionDialog(final MissionBean missionBean, final MissionDetailBean missionDetailBean) {

        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(SearchActivity.this);
        final View dialogView = LayoutInflater.from(SearchActivity.this)
                .inflate(R.layout.dialog_mission,null);
        customizeDialog.setTitle("请再次核查此次任务的相关信息");
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //保存mission.Json到本地
//                        String content = new Gson().toJson(missionBean);
//                        saveMission(String.valueOf(missionBean.getId()), content);
                        //保存missionDetail.Json到本地
                        String detail = new Gson().toJson(missionDetailBean);
                        saveMissionDetail(missionDetailBean.getId()+"_1", detail);
                    }
                });
        customizeDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        TextView maker = dialogView.findViewById(R.id.maker);
        maker.setText(missionBean.getMaker());

        TextView makeTime = dialogView.findViewById(R.id.makeTime);
        makeTime.setText(missionBean.getMakeTime());

        TextView carryTime = dialogView.findViewById(R.id.carryTime);
        carryTime.setText(missionBean.getCarryTime());

        TextView deadline = dialogView.findViewById(R.id.deadline);
        deadline.setText(missionBean.getDeadline());

        TextView comment = dialogView.findViewById(R.id.comment);
        comment.setText(missionBean.getComment());

        TextView checker = dialogView.findViewById(R.id.checker);
        checker.setText(missionDetailBean.getChecker());

        TextView result = dialogView.findViewById(R.id.result);
        result.setText(missionDetailBean.getResult());

        TextView name = dialogView.findViewById(R.id.name);
        name.setText(missionDetailBean.getArea().getName());

        TextView code = dialogView.findViewById(R.id.code);
        code.setText(missionDetailBean.getArea().getCode());

        customizeDialog.show();

    }



    private void showFireDialog(final FireRecordBean.DataBean dataBean, final RecordBean recordBean) {

        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(SearchActivity.this);
        final View dialogView = LayoutInflater.from(SearchActivity.this)
                .inflate(R.layout.dialog_fire,null);
        customizeDialog.setTitle("请仔细核查灭火器的相关信息");
        customizeDialog.setView(dialogView);
        customizeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyDataSetChanged();
                        dataBean.setFinished(true);
                        recordBean.setComment(commentRecord.getText().toString().trim());
                        //保存Json到本地
                        String content = new Gson().toJson(recordBean);
                        saveInspect(dataBean.getBarcode(), content);
                    }
                });

        TextView barcode = dialogView.findViewById(R.id.barcode);
        barcode.setText(dataBean.getBarcode());

        TextView proructionDate = dialogView.findViewById(R.id.proructionDate);
        proructionDate.setText(dataBean.getProructionDate());

        TextView validdateOfBody = dialogView.findViewById(R.id.validdateOfBody);
        validdateOfBody.setText(dataBean.getValiddateOfBody());

        TextView validdateOfFire = dialogView.findViewById(R.id.validdateOfFire);
        validdateOfFire.setText(dataBean.getValiddateOfFire());

        TextView manufacture = dialogView.findViewById(R.id.manufacture);
        manufacture.setText(dataBean.getManufacture());

        TextView comment = dialogView.findViewById(R.id.comment);
        comment.setText(dataBean.getComment());

        RadioGroup qianfeng = dialogView.findViewById(R.id.qianfeng);
        qianfeng.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                boolean b = i == R.id.yes1 ;
                recordBean.setQianfeng(b);
            }
        });

        RadioGroup yali = dialogView.findViewById(R.id.yali);
        yali.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                boolean b = i == R.id.yes2;
                recordBean.setYali(b);
            }
        });

        RadioGroup pingti = dialogView.findViewById(R.id.pingti);
        pingti.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                boolean b = i == R.id.yes3;
                recordBean.setPingti(b);
            }
        });

        RadioGroup pengguan = dialogView.findViewById(R.id.pengguan);
        pengguan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                boolean b = i == R.id.yes4;
                recordBean.setPenguan(b);
            }
        });

        RadioGroup pengzui = dialogView.findViewById(R.id.pengzui);
        pengzui.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                boolean b = i == R.id.yes5;
                recordBean.setPenzui(b);
            }
        });

        RadioGroup operation = dialogView.findViewById(R.id.operation);
        operation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String b = "";
                switch (i) {
                    case R.id.yes6:
                        b = "无";
                        break;
                    case R.id.or6:
                        b = "更换";
                        break;
                    case R.id.no6:
                        b = "维修";
                        break;
                }
                recordBean.setOperation(b);
            }
        });

        RadioGroup status = dialogView.findViewById(R.id.finished);
        status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String b = i == R.id.yes7 ? "正常" : "异常";
                recordBean.setStatus(b);
            }
        });

        TextView inspectTime = dialogView.findViewById(R.id.inspectTime);
        String time = Utils.getCurrentSecond();
        inspectTime.setText(time);
        recordBean.setInspectTime(time);

        TextView fireId = dialogView.findViewById(R.id.fireId);
        fireId.setText(String.valueOf(recordBean.getFireId()));

        TextView missionId = dialogView.findViewById(R.id.missionId);
        missionId.setText(String.valueOf(recordBean.getMissionId()));

        commentRecord = dialogView.findViewById(R.id.commentRecord);
        commentRecord.setText(String.valueOf(recordBean.getComment()));

        customizeDialog.show();
    }

    /**
     *
     * @param dirBase 根目录 ftmtp
     * @return 是否导入成功
     */
    public boolean exportMission(String dirBase) {

//        String recentFileName = Utils.getRecentFile(dirBase);
        String recentFileName = currentDirPath;

        if (recentFileName.equals("")) {
            return false;
        }
        String[] sub = Utils.getAllSubFiles(recentFileName + "/mission");
        if (sub == null) {
            return false;
        }
        String missionJson = Utils.getFileFromSD(recentFileName + "/mission/"+sub[0]);//bug
        missionBean = new Gson().fromJson(missionJson, MissionBean.class);

        String[] subs = Utils.getAllSubFiles(recentFileName + "/missionDetail");
        if (subs == null) {
            return false;
        }
        String missionDetailJson = Utils.getFileFromSD(recentFileName + "/missionDetail/"+subs[0]);//bug
        missionDetailBean = new Gson().fromJson(missionDetailJson, MissionDetailBean.class);
        return true;
    }
}
