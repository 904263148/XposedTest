package com.dktlh.ktl.xposedtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dktlh.ktl.xposedtest.model.CodeRequestResponse;
import com.dktlh.ktl.xposedtest.utils.PrefJsonUtil;
import com.dktlh.ktl.xposedtest.utils.api.OkHttpUtils;
import com.dktlh.ktl.xposedtest.websocket.WsManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfigDetailActivity extends AppCompatActivity {

    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_detail);

        tvContent = this.findViewById(R.id.tv_content);

        CodeRequestResponse codeResponse = PrefJsonUtil.getProfile(WsApplication.getContext());

        String token = codeResponse.getToken();
        //字符串截取
        String bb =token.substring(10,20);
        //字符串替换
        String cc = token.replace(bb,"**********");

        String str = "商户名称：" + codeResponse.getMerchName() + "\n设备名称：" + codeResponse.getDeviceName()
                                + "\n设备标识：" + codeResponse.getDevice() + "\nToken：" + cc
                                + "\n服务器地址：" + codeResponse.getServer() + "\n测试地址：" + codeResponse.getTest()
                                + "\n绑定时间：" + codeResponse.getTime();

        tvContent.setText(str);

        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                Toast.makeText(ConfigDetailActivity.this, "测试失败！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(ConfigDetailActivity.this, "测试成功！", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        findViewById(R.id.tv_kill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WsManager.getInstance().disconnect();
                PrefJsonUtil.setProfile(WsApplication.getContext() , "");
                startActivity(new Intent(ConfigDetailActivity.this, QrCodeActivity.class));
                finish();
            }
        });

    }
}
