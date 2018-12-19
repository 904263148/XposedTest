package com.dktlh.ktl.xposedtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dktlh.ktl.xposedtest.model.resp.LoginResponse;
import com.dktlh.ktl.xposedtest.ui.LoadingDialog;
import com.dktlh.ktl.xposedtest.utils.PrefUtil;
import com.dktlh.ktl.xposedtest.utils.api.OkHttpUtils;
import com.dktlh.ktl.xposedtest.websocket.WsManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button mLoginBtn, mChangeUrl, btnScanner;
    private SharedPreferences.Editor editor;

    private int changeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginBtn = findViewById(R.id.mLoginBtn);
//        mNameEt = findViewById(R.id.mNameEt);
//        mPasswordEt = findViewById(R.id.mPasswordEt);
        btnScanner = findViewById(R.id.url_scanner);
        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, QrCodeActivity.class);
                startActivity(intent);
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("qrcode", Context.MODE_PRIVATE); //私有数据

//        editor = sharedPreferences.edit();//获取编辑器
//        mNameEt.setText(sharedPreferences.getString("name", ""));
//        mPasswordEt.setText(sharedPreferences.getString("password", ""));

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final String name = mNameEt.getText().toString();
//                final String pwd = mPasswordEt.getText().toString();
//                if (name.equals("") || pwd.equals("")) {
//                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_LONG).show();
//                    return;
//                }
                if(TextUtils.isEmpty(WsManager.LOGIN_URL)){
                    Toast.makeText(LoginActivity.this, "请先前往设置页面扫一扫获取登录地址", Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String, String> map = new HashMap<>();
//                map.put("username", name);
//                map.put("password", pwd);
                final LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
                loadingDialog.setText("加载中...");
                loadingDialog.show();
                OkHttpUtils.build()
                        .postOkHttp(WsManager.LOGIN_URL, map)
                        .setCallback(new OkHttpUtils.OkHttpCallback() {
                            @Override
                            public void onError(Exception e) {
                                //异常
                                loadingDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "服务器异常", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onResponse(String result) {
                                Log.e("result", result);
                                loadingDialog.dismiss();
//                                //成功
//                                Gson gson = new Gson();
//                                LoginResponse loginResponse = gson.fromJson(result, LoginResponse.class);
//                                if (loginResponse.isOK()) {
//                                    editor.putString("name", name);
//                                    editor.putString("password", pwd);
//                                    editor.commit();
//
//                                    try {
//                                        String token = new JSONObject(result).optString("token");
//                                        PrefUtil.setName(WsApplication.getContext(), token);
//                                        Log.d("token-------------", token);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    startActivity(new Intent(LoginActivity.this, Main2Activity.class));
//                                } else {
//                                    Toast.makeText(LoginActivity.this, loginResponse.getMsg(), Toast.LENGTH_LONG).show();
//                                }
                                try {
                                    String code = new JSONObject(result).opt("code").toString();
                                    if (!code.equals("ok")) {
                                        Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    startActivity(new Intent(LoginActivity.this, Main2Activity.class));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PrefUtil.setName(WsApplication.getContext(), "");
    }
}
