package com.dktlh.ktl.xposedtest;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dktlh.ktl.xposedtest.capture.CaptureActivity;
import com.dktlh.ktl.xposedtest.model.CodeRequestResponse;
import com.dktlh.ktl.xposedtest.utils.PrefJsonUtil;
import com.dktlh.ktl.xposedtest.utils.api.OkHttpUtils;
import com.dktlh.ktl.xposedtest.websocket.WsManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hx
 * Time 2018/11/10/010.
 * 二维码扫描
 */

public class QrCodeActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvBack, tvScan, tvConfig, tvContent , tvSetContent;

    CodeRequestResponse res;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        if(PrefJsonUtil.getProfile(WsApplication.getContext()) != null && ! TextUtils.isEmpty( PrefJsonUtil.getProfile(WsApplication.getContext()).getToken()) ){
            WsManager.DEF_RELEASE_URL = PrefJsonUtil.getProfile(WsApplication.getContext()).getServer();
            startActivity(new Intent(QrCodeActivity.this, Main2Activity.class));
            finish();
            return;
        }

        initView();
        event();
        loadData();

    }

    private void initView() {
        //初始化控件
        tvBack = this.findViewById(R.id.tv_back);
        tvScan = this.findViewById(R.id.tv_scanning);
        tvConfig = this.findViewById(R.id.tv_config_manual);
        tvContent = this.findViewById(R.id.tv_content);
        tvSetContent = this.findViewById(R.id.tv_set_content);
    }

    private void event() {
        //点击事件
        tvBack.setOnClickListener(this);
        tvScan.setOnClickListener(this);
        tvConfig.setOnClickListener(this);
    }

    private void loadData() {
        //初始化数据

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                //返回键点击事件-关闭当前页面
                onBackPressed();
                break;
            case R.id.tv_scanning:
                //扫描按钮点击事件
                checkPermission();
                break;
            case R.id.tv_config_manual:
                //手工配置按钮点击事件

//                Intent intent = new Intent();
//                ComponentName cmp=new ComponentName("com.eg.android.AlipayGphone","com.alipay.mobile.security.personcenter.PersonCenterActivity");
//                intent.setAction(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setComponent(cmp);
//                startActivity(intent);

                startActivityForResult(new Intent(QrCodeActivity.this, SelfConfigActivity.class) ,100 );

//                if(res == null ){
//                    Toast.makeText(QrCodeActivity.this, "请先扫码获取配置信息", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                WsManager.LOGIN_URL = PrefJsonUtil.getProfile(WsApplication.getContext()).getTest();
//                WsManager.DEF_RELEASE_URL = PrefJsonUtil.getProfile(WsApplication.getContext()).getServer();
//
//                String str = "配置成功" + "\n当前登录地址：" + res.getTest()
//                        + "\n当前websocket地址：" + res.getServer() + "\n当前Token：" + res.getDevice();
//                tvSetContent.setText(str);
                break;
        }
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(QrCodeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //权限发生了改变 true  //  false 小米
            if (ActivityCompat.shouldShowRequestPermissionRationale(QrCodeActivity.this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(QrCodeActivity.this).setTitle("请给程序授权后使用")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 请求授权
                                ActivityCompat.requestPermissions(QrCodeActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
            } else {
                ActivityCompat.requestPermissions(QrCodeActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        } else {
            start_scanner();//已经授权了就调用打开相机的方法
        }
    }


    private void start_scanner() {
        Intent intent = new Intent(QrCodeActivity.this, CaptureActivity.class);
        startActivityForResult(intent, 0);

    }

    //获取扫码结果
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                String result = data.getExtras().getString("result");
                Log.d("TAG", "qrcode_result:" + result);
                OkHttpUtils.build().getokHttp(result).setCallback(new OkHttpUtils.OkHttpCallback() {
                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onResponse(String result) {
                        //成功
                        CodeRequestResponse codeResponse = null;
                        String code;
                        try {
                            code = new JSONObject(result).opt("code").toString();
                            if (!code.equals("ok")) {
                                Toast.makeText(QrCodeActivity.this, "信息读取失败！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            codeResponse = new Gson().fromJson(new JSONObject(result).opt("data").toString(), CodeRequestResponse.class);
                            PrefJsonUtil.setProfile(WsApplication.getContext() , new JSONObject(result).opt("data").toString());
                            res = codeResponse;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (null == codeResponse) {
                            Toast.makeText(QrCodeActivity.this, "信息读取失败！", Toast.LENGTH_SHORT).show();
                            return;
                        }


//                        String str = "商户名称：" + codeResponse.getMerchName() + "\n设备名称：" + codeResponse.getDeviceName()
//                                + "\n设备标识：" + codeResponse.getDevice() + "\nToken：" + codeResponse.getToken()
//                                + "\n服务器地址：" + codeResponse.getServer() + "\n测试地址：" + codeResponse.getTest()
//                                + "\n绑定时间：" + codeResponse.getTime();
//
//                        tvContent.setText(str);
                        login();
                    }
                });
            }

            if(requestCode == 100){
                startActivity(new Intent(QrCodeActivity.this, Main2Activity.class));
                finish();
            }
        }
    }

    private void login(){
        OkHttpUtils.build().getokHttp(PrefJsonUtil.getProfile(WsApplication.getContext()).getTest()).setCallback(new OkHttpUtils.OkHttpCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String result) {
                //成功
                String code;
                try {
                    code = new JSONObject(result).opt("code").toString();
                    if (!code.equals("ok")) {
                        Toast.makeText(QrCodeActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    WsManager.DEF_RELEASE_URL = PrefJsonUtil.getProfile(WsApplication.getContext()).getServer();
                    startActivity(new Intent(QrCodeActivity.this, Main2Activity.class));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                //判断是否有权限
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    start_scanner();//打开相机
                } else {
                    Toast.makeText(this, "权限获取失败！", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
