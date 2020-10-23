package com.dreamlost.barcodechecker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamlost.barcodechecker.bean.FireRecordBean;
import com.dreamlost.barcodechecker.bean.MissionBean;
import com.dreamlost.barcodechecker.bean.MissionDetailBean;
import com.dreamlost.barcodechecker.bean.NewFireRecordBean;
import com.dreamlost.barcodechecker.bean.RecordBean;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
    private long missionId;
    private String missionChecker;
    private MissionBean missionBean;
    private MissionDetailBean missionDetailBean;
    private RecyclerView rv;
    private RvAdapter adapter;
    private List<FireRecordBean.DataBean> data;
    private EditText commentRecord;
    private double jd,wd;
    private boolean b1 = true,b2 = true, b3=true,b4=true, b5 = true, b6 = true;
    private TextView productionDate,validdateOfBody, validdateOfFire, tv_complete;
    private String preTime1 = "",preTime2 = "",preTime3 = "",postTime1 = "",postTime2 = "", postTime3 = "";
    private String operationType = "", manufactureText = "", commentText ="";
    private AlertDialog mDialog;
    private EditText manufacture, comment;
    private LocationManager locationManager;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        currentDirPath = dirBase + getIntent().getStringExtra("filePath") + "/";
        missionId = getIntent().getLongExtra("missionId",0000);
        missionChecker = getIntent().getStringExtra("checker");

        initView();

        importFiles();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            @SuppressLint("MissingPermission")
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 8, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double jd =location.getLongitude();
                    double wd =location.getLatitude();
                    Toast.makeText(mContext, "位置jd="+jd+",wd="+wd, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                    Toast.makeText(mContext, "onStatusChanged="+s+", "+i, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProviderEnabled(String s) {
                    Toast.makeText(mContext, "onProviderEnabled="+s, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProviderDisabled(String s) {
                    Toast.makeText(mContext, "onProviderDisabled="+s, Toast.LENGTH_SHORT).show();
                }
            });
        }

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
//                    List<Double> locationInfo = Utils.getLocationInfo(SearchActivity.this);
//                    if (locationInfo != null) {
//                        Log.i(TAG, "onEditorAction: wd=" + locationInfo.get(1) + ",jd+" + locationInfo.get(0));
//                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        Log.i(TAG, "onEditorAction: jd=" + location.getLongitude() + ",wd+" + location.getLatitude());
                    }

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
        tv_complete = findViewById(R.id.tv_complete);
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
            Toast.makeText(mContext, "missionDetail保存失败", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(mContext, "检查已完成", Toast.LENGTH_SHORT).show();
        }
        return s;
    }

    public String saveInspect(String name, String content) {
        String s = saveToLocal(name, content, "inspect");
        if (s.equals("")) {
            Toast.makeText(mContext, "inspect保存失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "检查已完成", Toast.LENGTH_SHORT).show();
        }
        return s;
    }

    public String saveNewFireRecord(String name, String content) {
        String s = saveToLocal(name, content, "newFireRecord");
        if (s.equals("")) {
            Toast.makeText(mContext, "newFireRecord保存失败", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(mContext, "检查已完成", Toast.LENGTH_SHORT).show();
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
            MediaScannerConnection.scanFile(mContext,new String[]{file.getAbsolutePath()},null,null);
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
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
            int completeNum = resort(data);
            String s = (data.size()-completeNum)+"个位置未完成，"+completeNum+"个位置已完成";
            tv_complete.setText(s);
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

        final AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(SearchActivity.this);
        final View dialogView = LayoutInflater.from(SearchActivity.this)
                .inflate(R.layout.dialog_fire,null);
        customizeDialog.setTitle("请仔细核查灭火器的相关信息");
        customizeDialog.setView(dialogView);

        TextView barcode = dialogView.findViewById(R.id.barcode);
        barcode.setText(dataBean.getBarcode());

        productionDate = dialogView.findViewById(R.id.productionDate);
        preTime1 = dataBean.getProductionDate();
        productionDate.setText(dataBean.getProductionDate());

        validdateOfBody = dialogView.findViewById(R.id.validdateOfBody);
        preTime2 = dataBean.getValiddateOfBody();
        validdateOfBody.setText(dataBean.getValiddateOfBody());

        validdateOfFire = dialogView.findViewById(R.id.validdateOfFire);
        preTime3 = dataBean.getValiddateOfFire();
        validdateOfFire.setText(dataBean.getValiddateOfFire());

        final RadioGroup status = dialogView.findViewById(R.id.finished);
        status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String b = i == R.id.yes7 ? "正常" : "异常";
                recordBean.setStatus(b);
            }
        });

        manufacture = dialogView.findViewById(R.id.manufacture);
        manufactureText = dataBean.getManufacture();
        manufacture.setText(dataBean.getManufacture());

        comment = dialogView.findViewById(R.id.comment);
        commentText = dataBean.getComment();
        comment.setText(dataBean.getComment());

        RadioGroup qianfeng = dialogView.findViewById(R.id.qianfeng);
        qianfeng.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                b1 = i == R.id.yes1;
                if (!b1) {
                    status.check(R.id.no7);
                } else if (b1 && b2 && b3 && b4 && b5 && b6) {
                    status.check(R.id.yes7);
                }
                recordBean.setQianfeng(b1);
            }
        });

        RadioGroup yali = dialogView.findViewById(R.id.yali);
        yali.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                b2 = i == R.id.yes2;
                if (!b2) {
                    status.check(R.id.no7);
                } else if (b1 && b2 && b3 && b4 && b5 && b6) {
                    status.check(R.id.yes7);
                }
                recordBean.setYali(b2);
            }
        });

        RadioGroup pingti = dialogView.findViewById(R.id.pingti);
        pingti.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                b3 = i == R.id.yes3;
                if (!b3) {
                    status.check(R.id.no7);
                } else if (b1 && b2 && b3 && b4 && b5 && b6) {
                    status.check(R.id.yes7);
                }
                recordBean.setPingti(b3);
            }
        });

        RadioGroup pengguan = dialogView.findViewById(R.id.pengguan);
        pengguan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                b4 = i == R.id.yes4;
                if (!b4) {
                    status.check(R.id.no7);
                } else if (b1 && b2 && b3 && b4 && b5 && b6) {
                    status.check(R.id.yes7);
                }
                recordBean.setPenguan(b4);
            }
        });

        RadioGroup pengzui = dialogView.findViewById(R.id.pengzui);
        pengzui.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                b5 = i == R.id.yes5;
                if (!b5) {
                    status.check(R.id.no7);
                } else if (b1 && b2 && b3 && b4 && b5 && b6) {
                    status.check(R.id.yes7);
                }
                recordBean.setPenzui(b5);
            }
        });

        RadioGroup operation = dialogView.findViewById(R.id.operation);
        operationType = "无";
        operation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.yes6:
                        b6 = true;
                        if (b1 && b2 && b3 && b4 && b5 && b6) {
                            status.check(R.id.yes7);
                        }
                        operationType = "无";
                        manufacture.setEnabled(false);
                        manufacture.setText(manufactureText);
                        comment.setEnabled(false);
                        comment.setText(commentText);
                        productionDate.setEnabled(false);
                        validdateOfBody.setEnabled(false);
                        validdateOfFire.setEnabled(false);
                        productionDate.setText(preTime1);
                        validdateOfBody.setText(preTime2);
                        validdateOfFire.setText(preTime3);
                        break;
                    case R.id.or6:
                        b6 = false;
                        status.check(R.id.no7);
                        operationType = "更换";
                        manufacture.setEnabled(true);
                        comment.setEnabled(true);
                        Toast.makeText(mContext, "请选择更换的时间", Toast.LENGTH_SHORT).show();
                        productionDate.setText("选择时间");
                        productionDate.setEnabled(true);
                        validdateOfBody.setText("选择时间");
                        validdateOfBody.setEnabled(true);
                        validdateOfFire.setText("选择时间");
                        validdateOfFire.setEnabled(true);
                        break;
                    case R.id.no6:
                        b6 = false;
                        status.check(R.id.no7);
                        operationType = "维修";
                        manufacture.setEnabled(false);
                        manufacture.setText(manufactureText);
                        comment.setEnabled(false);
                        comment.setText(commentText);
                        productionDate.setEnabled(false);
                        validdateOfBody.setEnabled(false);
                        validdateOfFire.setEnabled(false);
                        productionDate.setText(preTime1);
                        validdateOfBody.setText(preTime2);
                        validdateOfFire.setText(preTime3);
                        break;
                }
                recordBean.setOperation(operationType);
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

        mDialog = customizeDialog.setPositiveButton("确定", null).setNegativeButton("取消", null).create();
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positionButton=mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton=mDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                positionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (operationType.equals("更换")) {
                            if (postTime1.equals("") || postTime2.equals("") || postTime3.equals("")) {
                                Toast.makeText(mContext, "请选择时间", Toast.LENGTH_SHORT).show();
                            } else {
                                dataBean.setFinished(true);
                                int completeNum = resort(data);
                                String s = (data.size()-completeNum)+"个位置未完成，"+completeNum+"个位置已完成";
                                tv_complete.setText(s);
                                adapter.notifyDataSetChanged();
                                recordBean.setJd(jd);
                                recordBean.setWd(wd);
                                recordBean.setComment(commentRecord.getText().toString().trim());
                                //保存Json到本地
                                String content = new Gson().toJson(recordBean);
                                saveInspect(dataBean.getBarcode(), content);

                                //保存NewFireRecord.Json到本地
                                NewFireRecordBean newFireRecordBean = new NewFireRecordBean();
                                newFireRecordBean.setId(dataBean.getId());
                                newFireRecordBean.setBarcode(dataBean.getBarcode());
                                newFireRecordBean.setProductionDate(postTime1);
                                newFireRecordBean.setValiddateOfBody(postTime2);
                                newFireRecordBean.setValiddateOfFire(postTime3);
                                newFireRecordBean.setManufacture(manufacture.getText().toString().trim());
                                newFireRecordBean.setFinished(true);
                                newFireRecordBean.setDeployer(missionChecker);
                                newFireRecordBean.setDeployDate(Utils.getCurrentDay());
                                newFireRecordBean.setComment(comment.getText().toString().trim());
                                newFireRecordBean.setPositionId(dataBean.getPositionId());

                                String content1 = new Gson().toJson(newFireRecordBean);
                                saveNewFireRecord(dataBean.getBarcode(), content1);
                                mDialog.dismiss();
                            }
                        } else {
                            dataBean.setFinished(true);
                            int completeNum = resort(data);
                            String s = (data.size()-completeNum)+"个位置未完成，"+completeNum+"个位置已完成";
                            tv_complete.setText(s);
                            adapter.notifyDataSetChanged();
                            recordBean.setJd(jd);
                            recordBean.setWd(wd);
                            recordBean.setComment(commentRecord.getText().toString().trim());
                            //保存Json到本地
                            String content = new Gson().toJson(recordBean);
                            saveInspect(dataBean.getBarcode(), content);
                            mDialog.dismiss();
                        }
                    }
                });
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(SearchActivity.this,"取消",Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }
                });

            }
        });
        mDialog.show();

//        customizeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        customizeDialog.setPositiveButton("确定",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (operationType.equals("更换")) {
//                            if (postTime1.equals("") || postTime2.equals("") || postTime3.equals("")) {
//                                Toast.makeText(mContext, "请选择时间", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            adapter.notifyDataSetChanged();
//                            dataBean.setFinished(true);
//                            recordBean.setJd(jd);
//                            recordBean.setWd(wd);
//                            recordBean.setComment(commentRecord.getText().toString().trim());
//                            //保存Json到本地
//                            String content = new Gson().toJson(recordBean);
//                            saveInspect(dataBean.getBarcode(), content);
//                        }
//                    }
//                });

    }


    private int resort(List<FireRecordBean.DataBean> list) {
        int num = 0;
        Collections.sort(list,(o1,o2)->{
                    if (o1.getFinished()) {
                        return 1;
                    } else {
                        return -1;
                    }
        });
        for (FireRecordBean.DataBean dataBean : list) {
            if (dataBean.getFinished()) {
                num++;
            }
        }

        return num;

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

    public void time1(View view) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(SearchActivity.this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    postTime1 = year + "-" + (month+1) + "-" + dayOfMonth + " 08:00:00";
                    productionDate.setText(postTime1);
                }
            }
            , calendar.get(Calendar.YEAR)
            , calendar.get(Calendar.MONTH)
            , calendar.get(Calendar.DAY_OF_MONTH)).show();

    }
    public void time2(View view) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(SearchActivity.this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    postTime2 = year + "-" + (month + 1) + "-" + dayOfMonth + " 08:00:00";
                    validdateOfBody.setText(postTime2);
                }
            }
            , calendar.get(Calendar.YEAR)
            , calendar.get(Calendar.MONTH)
            , calendar.get(Calendar.DAY_OF_MONTH)).show();

    }
    public void time3(View view) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(SearchActivity.this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    postTime3 = year + "-" + (month+1) + "-" + dayOfMonth + " 08:00:00";
                    validdateOfFire.setText(postTime3);
                }
            }
            , calendar.get(Calendar.YEAR)
            , calendar.get(Calendar.MONTH)
            , calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public static List<Double> getLocationInfo(Activity context) {
        String locationProvider;
        ArrayList<Double> list = new ArrayList<>();
//        private LocationManager locationManager;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            // 如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        }
//    else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
//       // 如果是Network
//       locationProvider = LocationManager.NETWORK_PROVIDER;
//    }
        else {
            Toast.makeText(context, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return null;
        }
        // 获取Location
        PermissionUtils.requestLocationPermission(context);
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return null ;
        }
        // 监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 2000, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double jd =location.getLongitude();
                double wd =location.getLatitude();
                Toast.makeText(context, "位置jd="+jd+",wd="+wd, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Toast.makeText(context, "onStatusChanged="+s+", "+i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderEnabled(String s) {
                Toast.makeText(context, "onProviderEnabled="+s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String s) {
                Toast.makeText(context, "onProviderDisabled="+s, Toast.LENGTH_SHORT).show();
            }
        });
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            // 不为空,显示地理位置经纬度
            double jd =location.getLongitude();
            double wd =location.getLatitude();
            Toast.makeText(context, "位置jd="+jd+",wd="+wd, Toast.LENGTH_SHORT).show();
            list.add(jd);
            list.add(wd);
            return list;
        } else {
            Toast.makeText(context, "室内GPS信号差", Toast.LENGTH_SHORT).show();
            return null;
//            System.out.println("GPS未定位到位置,请查看是否打开了GPS ？");
        }

    }

}
