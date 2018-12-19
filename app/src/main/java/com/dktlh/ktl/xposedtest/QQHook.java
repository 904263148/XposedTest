package com.dktlh.ktl.xposedtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class QQHook {

    public static String BILLRECEIVED_ACTION = "com.hamancom.hmpayhelp.billreceived";
    public static String QRCODERECEIVED_ACTION = "com.hamancom.hmpayhelp.qrcodereceived";

    private String uin;
    private String auth_code;
    private String money;
    private String bill_no;
    private String bill_mark;
    private String url;

    private boolean first = false;
    private boolean billFirst = false;

    private boolean firstHook = false;  // 第一次不是从网页拿数据自己点进来时不自动点击设置金额

    private boolean intentParams = false; //只有当不是自己第一次点进来和是从网页传值了同时满足才自动点击设置金额

    private int payCount = 0;

    private boolean isPay = false;

    public void hook(ClassLoader paramClassLoader, final Context context){

        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.pluginsdk.PluginProxyActivity", paramClassLoader, "startPluginActivityForResult" , Activity.class ,
                            String.class , Intent.class , int.class ,new XC_MethodHook() {

                                @Override
                                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                    super.beforeHookedMethod(param);
                                    Object localObject1 = param.args[2];
                                    for (String key:
                                            ((Intent) localObject1).getExtras().keySet()) {
                                        if("uin".equals(key)){
                                            uin = ((Intent) localObject1).getStringExtra(key);
                                            XposedBridge.log("二维码拥有者：--------" + uin);
                                        }
                                    }

                                }
                            });

        // 第一步：Hook方法ClassLoader#loadClass(String)
        XposedHelpers.findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (param.hasThrowable()) return;
                final Class<?> cls = (Class<?>) param.getResult();
                String name = cls.getName();

                if("com.tenpay.sdk.activity.QrcodeSettingActivity".equals(name)){

                    first =false;
                    billFirst = false;

                    XposedHelpers.findAndHookMethod(cls, "onCreate", Bundle.class, new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            XposedBridge.log("Hook QQ开始......");

                            SharedPreferences sharedPreferences = context.getSharedPreferences("qrcode", Context.MODE_PRIVATE); //私有数据

                            if( TextUtils.isEmpty(sharedPreferences.getString("money" , "")) || TextUtils.isEmpty(sharedPreferences.getString("mark" , "")) ){
                                    return;
                            }

                            Object oMoney = XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368356)});
                            XposedHelpers.callMethod(oMoney, "setText", sharedPreferences.getString("money" , ""));
                            money = sharedPreferences.getString("money" , "");

                            Object oStr = XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368357)});
                            XposedHelpers.callMethod(oStr, "setText", sharedPreferences.getString("mark" , ""));
                            bill_mark = sharedPreferences.getString("mark" , "");

                            intentParams = sharedPreferences.getBoolean("intentParams" , false);

                            ((Button) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368358)})).performClick();

//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.clear();
//                            editor.apply();

                            XposedBridge.log("Hook QQ开始......" + sharedPreferences.getString("money" , ""));

                        }
                    });
                }

                if("com.tenpay.sdk.activity.QrcodePayActivity".equals(name)){

                    XposedHelpers.findAndHookMethod(cls, "onResume",new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);

                            XposedBridge.log("支付调用顺序222222222");

                            XposedBridge.log(firstHook+"-----boolean参数------"+intentParams);

//                            TextView textView = (TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131367976)});

                            //设置金额控件
                            TextView tv = (TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368337)});

                            XposedBridge.log("设置金额按钮当时的文本内容为："+tv.getText().toString());

                            if( ! firstHook && intentParams ){
                                //设置金额按钮
                                if("设置金额".equals(tv.getText().toString())){
                                    ((TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368337)})).performClick();
                                }else {
                                    ((TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368337)})).performClick();
                                    ((TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368337)})).performClick();
                                }

                                firstHook = true;

                            }

//                            SharedPreferences sharedPreferences = context.getSharedPreferences("qrcode", Context.MODE_PRIVATE); //私有数据
//                            if(sharedPreferences == null ||  ! sharedPreferences.getBoolean("firstHook" , false) ){
//                                return;     && !TextUtils.isEmpty(textView.getText().toString())
//                            }
//                            if(firstHook){
//                                ((TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368337)})).performClick();
//                            }

//                            if( ! beginHook){
//                                ((TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368337)})).performClick();
//                            }
//                            TextView oStr = (TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368337)});
//                            if("设置金额".equals(oStr.getText().toString())){
//                                ((TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368337)})).performClick();
//                            }else {
//                                ((TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368337)})).performClick();
//                            }

                            // 获取到指定名称类声明的所有方法的信息
                            final Class clazz = param.thisObject.getClass();
                            Method[] m = clazz.getDeclaredMethods();
                            /// 打印获取到的所有的类方法的信息
                            for (int i = 0; i < m.length; i++) {


                                    // 对指定名称类中声明的非抽象方法进行java Hook处理
                                    XposedBridge.hookMethod(m[i], new XC_MethodHook() {

                                        // 被java Hook的类方法执行完毕之后，打印log日志
                                        @Override
                                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                                            // 打印被java Hook的类方法的名称和参数类型等信息
                                            XposedBridge.log("HOOKED METHOD: "+clazz.getName()+"-"+param.method.toString());
                                        }
                                    });
                            }

                        }
                    });

                    XposedHelpers.findAndHookMethod(cls, "a", String.class , JSONObject.class , new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);

                            JSONObject object = (JSONObject) param.args[1];
                            auth_code = object.getString("auth_code");
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            TextView textView = (TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131367976)});
                            TextView memo = (TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131367977)});

                            StringBuffer str = new StringBuffer();
                            str.append("https://i.qianbao.qq.com/wallet/sqrcode.htm?m=tenpay&f=wallet&u=");
                            str.append(uin);
                            str.append("&a=1&n=%E5%88%98%E5%8A%B2%E5%B3%B0&ac=");
                            str.append(auth_code);
                            url = str.toString();

                            Intent intent = new Intent();
                            intent.putExtra("money", textView.getText().toString());
                            intent.putExtra("mark", memo.getText().toString());
                            intent.putExtra("type", "qq");
                            intent.putExtra("payurl", url);

                            if (TextUtils.isEmpty(textView.getText().toString()) || TextUtils.isEmpty(memo.getText().toString()) ) {
                                return;
                            }

                            if(! first){

//                                if( TextUtils.isEmpty(sharedPreferences.getString("money" , "")) || TextUtils.isEmpty(sharedPreferences.getString("mark" , "")) ){
//                                    return;
//                                }

                                intent.setAction(QQHook.QRCODERECEIVED_ACTION);
                                context.sendBroadcast(intent);

                                XposedBridge.log("调用增加数据方法==>QQ");
                                XposedBridge.log("金额:---------" + money);
                                XposedBridge.log("备注:---------" + bill_mark);
                                XposedBridge.log(" 二维码的auth_code:--------- " + auth_code);
                                XposedBridge.log("二维码url:---------" + url);
                                XposedBridge.log("生成完毕==>QQ");

                                //清空金额和备注
                                ((TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368337)})).performClick();

                                first = true;
                                firstHook = false;

                                // 9/21 09:39 生成二维码后设置false
//                                SharedPreferences sharedPreferences = context.getSharedPreferences("qrcode", Context.MODE_PRIVATE); //私有数据
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.putBoolean("intentParams" , false);
//                                editor.apply();
//                                intentParams = false;
//                                //  当是支付成功或者失败过后 下一次进来还是要生成二维码
//                                if(isPay){
//                                    intentParams = false;
//                                    isPay = false;
//                                }else {
//                                    intentParams = true;
//                                }
                            }

                        }
                    });

                    XposedHelpers.findAndHookMethod(cls, "a", JSONObject.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);

                            XposedBridge.log("支付调用顺序11111111");

                            //[{"from_name":"   往事随风。","from_uin":"904263148","pay_msg":"支付中...","pay_status":"1","type":"qrcode_trans"}]
                            JSONObject object = (JSONObject) param.args[0];
                            String from_name = object.getString("from_name");
                            String from_uin = object.getString("from_uin");
                            String pay_msg = object.getString("pay_msg");
                            String pay_status = object.getString("pay_status");
                            String type = object.getString("type");

                            bill_no = object.toString();

                            Intent intent = new Intent();
                            intent.putExtra("bill_no", bill_no);
                            intent.putExtra("bill_money", money);
                            intent.putExtra("bill_type", "qq");
                            intent.putExtra("bill_mark", bill_mark);

                            //
//                            if("3".equals(pay_status)){
//                                if( ! billFirst ){
//                                    intent.setAction(QQHook.BILLRECEIVED_ACTION);
//                                    context.sendBroadcast(intent);
//                                    XposedBridge.log("支付成功");
//                                    billFirst = true;
//
//                                    //新加的------
//                                    firstHook = false;
//
//                                }
//                            }

                            // 9/21 09:39

                            //设置金额控件
                            TextView tv = (TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368337)});

                            //如果支付结果等于三，即支付成功，只执行一次回调。 因为此方法会执行两次
                            if("3".equals(pay_status)){

                                isPay = true;
                                payCount++;

                                XposedBridge.log("进入支付回调的次数："+ payCount);

                                if(payCount == 2){
                                    //取第二次进行回调
                                    intent.setAction(QQHook.BILLRECEIVED_ACTION);
                                    context.sendBroadcast(intent);
                                    //回调完以后执行点击设置金额。

                                    XposedBridge.log("设置金额按钮当时的文本内容为："+tv.getText().toString());

                                    ((TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368337)})).performClick();
                                    ((TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368337)})).performClick();

                                    payCount = 0;//清零

                                    XposedBridge.log("from_name:---------" + from_name);
                                    XposedBridge.log("from_uin:---------" + from_uin);
                                    XposedBridge.log("pay_msg:---------" + pay_msg);
                                    XposedBridge.log("pay_status:---------" + pay_status);
                                    XposedBridge.log("type:---------" + type);
                                }
                            }

                            if("2".equals(pay_status)){

                                isPay = true;
                                payCount++;

                                if(payCount == 2){
                                    //回调完以后执行点击设置金额。

                                    ((TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368337)})).performClick();
                                    ((TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368337)})).performClick();

                                    payCount = 0;//清零

                                    XposedBridge.log("from_name:---------" + from_name);
                                    XposedBridge.log("from_uin:---------" + from_uin);
                                    XposedBridge.log("pay_msg:---------" + pay_msg);
                                    XposedBridge.log("pay_status:---------" + pay_status);
                                    XposedBridge.log("type:---------" + type);
                                }
                            }

//                            if( ! billFirst ){
//
//                                XposedBridge.log("from_name:---------" + from_name);
//                                XposedBridge.log("from_uin:---------" + from_uin);
//                                XposedBridge.log("pay_msg:---------" + pay_msg);
//                                XposedBridge.log("pay_status:---------" + pay_status);
//                                XposedBridge.log("type:---------" + type);
//
//                                if("3".equals(pay_status)){
//                                    intent.setAction(QQHook.BILLRECEIVED_ACTION);
//                                    context.sendBroadcast(intent);
//                                }
//                                billFirst = true;
//                                //新加的------
//                                firstHook = false;
//
//                                //进入支付界面时 状态 2 和 3  点击一下设置金额
//                                if( !"1".equals(pay_status) ){
//                                    ((TextView) XposedHelpers.callMethod(param.thisObject, "findViewById", new Object[]{Integer.valueOf(2131368337)})).performClick();
//                                }
//
//                            }

                        }
                    });

                    XposedHelpers.findAndHookMethod(cls, "h", String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            XposedBridge.log("支付页面方法h(String)的参数：" + param.args[0]);
                        }
                    });
//
                    XposedHelpers.findAndHookMethod(cls, "a", String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            XposedBridge.log("支付页面方法a(String)的参数：" + param.args[0]);
                        }
                    });

                }
            }
        });

    }

}
