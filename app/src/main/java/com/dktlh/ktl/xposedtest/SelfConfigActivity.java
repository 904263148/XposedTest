package com.dktlh.ktl.xposedtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dktlh.ktl.xposedtest.model.CodeRequestResponse;
import com.dktlh.ktl.xposedtest.utils.PrefJsonUtil;
import com.dktlh.ktl.xposedtest.utils.api.OkHttpUtils;
import com.dktlh.ktl.xposedtest.websocket.WsManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class SelfConfigActivity extends AppCompatActivity {

    private EditText merchname , devicename , device , token , login , socket , time;

    private CodeRequestResponse codeResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_config);

        merchname = findViewById(R.id.et_merchname);
        devicename = findViewById(R.id.et_devicename);
        device = findViewById(R.id.et_device);
        token = findViewById(R.id.et_token);
        login = findViewById(R.id.et_login);
        socket = findViewById(R.id.et_socket);
        time = findViewById(R.id.et_time);

        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.btn_config).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeResponse = new CodeRequestResponse();
                codeResponse.setMerchName(merchname.getText().toString());
                codeResponse.setDeviceName(devicename.getText().toString());
                codeResponse.setDevice(device.getText().toString());
                codeResponse.setToken(token.getText().toString());
                codeResponse.setTest(login.getText().toString());
                codeResponse.setServer(socket.getText().toString());
                codeResponse.setTime(time.getText().toString());
                login();

            }
        });
    }

    private void login(){
        OkHttpUtils.build().getokHttp(codeResponse.getTest()).setCallback(new OkHttpUtils.OkHttpCallback() {
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
                        Toast.makeText(SelfConfigActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    PrefJsonUtil.setProfile(WsApplication.getContext(), new Gson().toJson(codeResponse));
                    WsManager.DEF_RELEASE_URL = codeResponse.getServer();
                    setResult(RESULT_OK);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
