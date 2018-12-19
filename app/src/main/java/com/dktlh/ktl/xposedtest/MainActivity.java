package com.dktlh.ktl.xposedtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dktlh.ktl.xposedtest.utils.BitmapUtil;
import com.dktlh.ktl.xposedtest.utils.PayHelperUtils;
import com.dktlh.ktl.xposedtest.websocket.WsManager;
import com.dktlh.ktl.xposedtest.websocket.common.ICallback;
import com.dktlh.ktl.xposedtest.websocket.notify.NotifyListenerManager;
import com.dktlh.ktl.xposedtest.websocket.request.Action;
import com.dktlh.ktl.xposedtest.websocket.request.Request;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.robv.android.xposed.XposedBridge;

public class MainActivity extends FragmentActivity {

    public static String BILLRECEIVED_ACTION = "com.hamancom.hmpayhelp.billreceived";
    public static String MSGRECEIVED_ACTION = "com.hamancom.hmpayhelp.msgreceived";
    public static String NOTIFY_ACTION = "com.hamancom.hmpayhelp.notify";
    public static String QRCODERECEIVED_ACTION = "com.hamancom.hmpayhelp.qrcodereceived";
    private WebServer mVideoServer;
    public static int WEBSEERVER_PORT = 8888;
    private BillReceived billReceived;
    private static TextView console;
    private static ImageView mQrIv;
    private static Context context;

    private EditText mAmountEt, mRemarkEt;
    private RadioGroup mRadioGroup;
    private Button mGetQrBtn;
    static NotificationManager notificationManager;
    static NotificationCompat.Builder mBuilder;

    public static void showNotif(String message) {

        /**
         *  设置Builder
         */
        //设置标题
        mBuilder.setContentTitle("我是标题")
                //设置内容
                .setContentText(message)
                //设置大图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置通知时间
                .setWhen(System.currentTimeMillis())
                //首次进入时显示效果
                .setTicker("我是测试内容")
                //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
                .setDefaults(Notification.DEFAULT_SOUND);
        //发送通知请求
        notificationManager.notify(10, mBuilder.build());
    }

    public static Handler handler = new Handler()
    {
        public void handleMessage(Message paramAnonymousMessage)
        {
                    String str = paramAnonymousMessage.getData().getString("log");
//                    MainActivity.showNotif(str);
                    if (MainActivity.console != null) {
                        if (MainActivity.console.getText() != null)
                        {
                            TextView localTextView;
                            StringBuilder localStringBuilder;
                            if (MainActivity.console.getText().toString().length() > 2000)
                            {
                                localTextView = MainActivity.console;
                                localStringBuilder = new StringBuilder();
                                localStringBuilder.append("日志定时清理完成...\n\n");
                                localStringBuilder.append(str);
                                localTextView.setText(localStringBuilder.toString());
                            }
                            else
                            {
                                localTextView = MainActivity.console;
                                localStringBuilder = new StringBuilder();
                                localStringBuilder.append(MainActivity.console.getText().toString());
                                localStringBuilder.append("\n\n");
                                localStringBuilder.append(str);
                                localTextView.setText(localStringBuilder.toString());
                            }
                        }
                        else {
                            MainActivity.console.setText(str);
                        }
                    }
            super.handleMessage(paramAnonymousMessage);

        }
    };
    public static void sendmsg(String paramString, String payUrl) {
        Message localMessage = new Message();
        localMessage.what = 1;
        Bundle localBundle = new Bundle();
        localBundle.putString("log", paramString);
        localBundle.putString("payUrl", payUrl);
//        localBundle.putSerializable("qrCodeBean", qrCodeBean);
//        localMessage.obj = localStringBuilder.toString();
//        localMessage.what = 0;
//        handler.sendMessage(localMessage);
        localMessage.setData(localBundle);
        try
        {
            handler.sendMessage(localMessage);
            return;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WsManager.getInstance().disconnect();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMessageEvent(String text) {
//        String[] strings = text.split(",");
//        if (strings.length != 3) {
//            return;
//        }
//        Intent paramAnonymousView = new Intent();
//        switch (strings[0]) {
//            case "wechat":
//                paramAnonymousView.setAction(PayHelperUtils.WECHATSTART_ACTION);
//                break;
//            case "alipay":
//                paramAnonymousView.setAction(PayHelperUtils.ALIPAYSTART_ACTION);
//                break;
//        }
//        paramAnonymousView.putExtra("mark", strings[1]);
//        paramAnonymousView.putExtra("money", strings[2]);
//        MainActivity.this.sendBroadcast(paramAnonymousView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        console = findViewById(R.id.console);
        mQrIv = findViewById(R.id.mQrIv);
        mAmountEt = findViewById(R.id.mAmountEt);
        mRemarkEt = findViewById(R.id.mRemarkEt);
        mRadioGroup = findViewById(R.id.mRadioGroup);
        mGetQrBtn = findViewById(R.id.mGetQrBtn);

        WsManager.getInstance().init();
//        notificationManager= (NotificationManager) getSystemService
//                (NOTIFICATION_SERVICE);
//        mBuilder = new NotificationCompat.Builder(this);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.weChat:
                        mGetQrBtn.setText("生成微信二维码");
                        break;
                    case R.id.zfb:
                        mGetQrBtn.setText("生成支付宝二维码");
                        break;
                }
            }
        });

//        this.mVideoServer = new WebServer(this, WEBSEERVER_PORT);
//        try {
//            this.mVideoServer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        new Thread(
//                new Runnable() {
//
//                    @Override
//                    public void run() {
//                        for (int i = 0; i < 10; i++) {
//                            Intent paramAnonymousView = new Intent();
//                            paramAnonymousView.setAction(PayHelperUtils.WECHATSTART_ACTION);
//                            paramAnonymousView.putExtra("mark", "test" + i);
//                            paramAnonymousView.putExtra("money", (1 + i) + "");
//                            MainActivity.this.sendBroadcast(paramAnonymousView);
//                            try {
//                                Thread.sleep(5000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }).start();
        mGetQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paramAnonymousView = new Intent();
                switch (mRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.weChat:
                        paramAnonymousView.setAction(PayHelperUtils.WECHATSTART_ACTION);
                        break;
                    case R.id.zfb:
                        paramAnonymousView.setAction(PayHelperUtils.ALIPAYSTART_ACTION);
                        break;
                }
                paramAnonymousView.putExtra("mark", mRemarkEt.getText().toString());
                paramAnonymousView.putExtra("money", mAmountEt.getText().toString());
                MainActivity.this.sendBroadcast(paramAnonymousView);
            }
        });
        this.billReceived = new BillReceived();
        IntentFilter paramBundle = new IntentFilter();
        paramBundle.addAction(BILLRECEIVED_ACTION);
        paramBundle.addAction(MSGRECEIVED_ACTION);
        paramBundle.addAction(QRCODERECEIVED_ACTION);
        registerReceiver(this.billReceived, paramBundle);

        GetQrCodeReceived getQrCodeReceived = new GetQrCodeReceived();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("server_get_code");
        registerReceiver(getQrCodeReceived, intentFilter);
//        paramBundle = new AlarmReceiver();
//        IntentFilter localIntentFilter = new IntentFilter();
//        localIntentFilter.addAction(NOTIFY_ACTION);
//        registerReceiver(paramBundle, localIntentFilter);
//        startService(new Intent(this, DaemonService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    class GetQrCodeReceived extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra("server_get_code");
            String[] strings = text.split(",");
            if (strings.length != 3) {
                return;
            }
            Intent paramAnonymousView = new Intent();
            switch (strings[0]) {
                case "wechat":
                    paramAnonymousView.setAction(PayHelperUtils.WECHATSTART_ACTION);
                    break;
                case "alipay":
                    paramAnonymousView.setAction(PayHelperUtils.ALIPAYSTART_ACTION);
                    break;
            }
            paramAnonymousView.putExtra("mark", strings[1]);
            paramAnonymousView.putExtra("money", strings[2]);
            MainActivity.this.sendBroadcast(paramAnonymousView);
        }
    }

    class BillReceived
            extends BroadcastReceiver
    {
        BillReceived() {}

//        private void update(String paramString1, String paramString2)
//        {
//            new DBManager(CustomApplcation.getInstance().getApplicationContext()).updateOrder(paramString1, paramString2);
//        }

//        public void notify(String paramString1, final String paramString2, String paramString3, String paramString4, String paramString5)
//        {
//            String str1 = AbSharedUtil.getString(MainActivity.this.getApplicationContext(), "notifyurl");
//            String str3 = AbSharedUtil.getString(MainActivity.this.getApplicationContext(), "ApiKey");
//            if ((!TextUtils.isEmpty(str1)) && (!TextUtils.isEmpty(str3)))
//            {
//                String str2 = AbSharedUtil.getString(MainActivity.this.getApplicationContext(), "AccountId");
//                Object localObject = new StringBuilder();
//                ((StringBuilder)localObject).append("orderno=");
//                ((StringBuilder)localObject).append(paramString4);
//                ((StringBuilder)localObject).append("&tarno=");
//                ((StringBuilder)localObject).append(paramString2);
//                ((StringBuilder)localObject).append("&amount=");
//                ((StringBuilder)localObject).append(paramString3);
//                ((StringBuilder)localObject).append("&dt=");
//                ((StringBuilder)localObject).append(paramString5);
//                ((StringBuilder)localObject).append(str3);
//                str3 = MD5.md5(((StringBuilder)localObject).toString());
//                localObject = new HttpUtils(15000);
//                RequestParams localRequestParams = new RequestParams();
//                localRequestParams.addBodyParameter("orderno", paramString4);
//                localRequestParams.addBodyParameter("tarno", paramString2);
//                localRequestParams.addBodyParameter("type", paramString1);
//                localRequestParams.addBodyParameter("amount", paramString3);
//                localRequestParams.addBodyParameter("dt", paramString5);
//                localRequestParams.addBodyParameter("account", str2);
//                localRequestParams.addBodyParameter("sign", str3);
//                ((HttpUtils)localObject).send(HttpRequest.HttpMethod.POST, str1, localRequestParams, new RequestCallBack()
//                {
//                    public void onFailure(HttpException paramAnonymousHttpException, String paramAnonymousString)
//                    {
//                        paramAnonymousHttpException = new StringBuilder();
//                        paramAnonymousHttpException.append("发送异步通知异常，服务器异常");
//                        paramAnonymousHttpException.append(paramAnonymousString);
//                        PayActivity.sendmsg(paramAnonymousHttpException.toString());
//                        PayActivity.BillReceived.this.update(paramString2, paramAnonymousString);
//                    }
//
//                    public void onSuccess(ResponseInfo<String> paramAnonymousResponseInfo)
//                    {
//                        paramAnonymousResponseInfo = (String)paramAnonymousResponseInfo.result;
//                        StringBuilder localStringBuilder;
//                        if (paramAnonymousResponseInfo.contains("success"))
//                        {
//                            localStringBuilder = new StringBuilder();
//                            localStringBuilder.append("发送异步通知成功，服务器返回");
//                            localStringBuilder.append(paramAnonymousResponseInfo);
//                            PayActivity.sendmsg(localStringBuilder.toString());
//                        }
//                        else
//                        {
//                            localStringBuilder = new StringBuilder();
//                            localStringBuilder.append("发送异步通知失败，服务器返回");
//                            localStringBuilder.append(paramAnonymousResponseInfo);
//                            PayActivity.sendmsg(localStringBuilder.toString());
//                        }
//                        PayActivity.BillReceived.this.update(paramString2, paramAnonymousResponseInfo);
//                    }
//                });
//                return;
//            }
//            PayActivity.sendmsg("发送异步通知异常，异步通知地址为空");
//            update(paramString2, "异步通知地址为空");
//        }

        public void onReceive(Context paramContext, Intent paramIntent)
        {
            try
            {
                String str1;
                String str2;
                String money;
                String payUrl;
                Object localObject1;
                if (paramIntent.getAction().contentEquals(MainActivity.BILLRECEIVED_ACTION))
                {

                    str1 = paramIntent.getStringExtra("bill_no");
                    str2 = paramIntent.getStringExtra("bill_money");
                    localObject1 = paramIntent.getStringExtra("bill_mark");
                    String type = paramIntent.getStringExtra("bill_type");
                    String resp = str1 + "," + str2 + "," + localObject1 + "," + type;
//                    WsManager.getInstance().sendReq(Action.QRCODE, resp, new ICallback() {
//                        @Override
//                        public void onSuccess(Object o) {
//
//                        }
//
//                        @Override
//                        public void onFail(String msg) {
//
//                        }
//                    });
//                    paramContext = new DBManager(CustomApplcation.getInstance().getApplicationContext());
//                    Object localObject2 = new StringBuilder();
//                    ((StringBuilder)localObject2).append(System.currentTimeMillis());
//                    ((StringBuilder)localObject2).append("");
//                    localObject2 = ((StringBuilder)localObject2).toString();
////                    paramContext.addOrder(new OrderBean(str2, (String)localObject1, paramIntent, str1, (String)localObject2, "", 0));
//                    if (type.equals("al")) {
//                        type = "支付宝";
//                    } else if (type.equals("wx")) {
//                        type = "微信";
//                    }
//                    StringBuilder localStringBuilder = new StringBuilder();
//                    localStringBuilder.append("收到");
//                    localStringBuilder.append(type);
//                    localStringBuilder.append("订单,订单号：");
//                    localStringBuilder.append(str1);
//                    localStringBuilder.append("金额：");
//                    localStringBuilder.append(str2);
//                    localStringBuilder.append("备注：");
//                    localStringBuilder.append((String)localObject1);
//                    MainActivity.sendmsg(localStringBuilder.toString(), "");
//                    notify(type, str1, str2, (String)localObject1, (String)localObject2);
                }
                else if (paramIntent.getAction().contentEquals(MainActivity.QRCODERECEIVED_ACTION)) {
                    money = paramIntent.getStringExtra("money");
                    str1 = paramIntent.getStringExtra("mark");
                    str2 = paramIntent.getStringExtra("type");
                    payUrl = paramIntent.getStringExtra("payurl");

                    String resp = money + "," + str1 + "," + str2 + "," + payUrl;
//                    WsManager.getInstance().sendReq(Action.QRCODE, resp, new ICallback() {
//                        @Override
//                        public void onSuccess(Object o) {
//
//                        }
//
//                        @Override
//                        public void onFail(String msg) {
//
//                        }
//                    });
                    Intent it = new Intent("android.intent.action.MAIN");
                    it.setComponent(new ComponentName(context.getPackageName(), MainActivity.class.getName()));
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(it);

//                    localObject1 = new StringBuilder();
//                    ((StringBuilder)localObject1).append(System.currentTimeMillis());
//                    ((StringBuilder)localObject1).append("");
//                    localObject1 = ((StringBuilder)localObject1).toString();
////                    new DBManager(CustomApplcation.getInstance().getApplicationContext()).addQrCode(new QrCodeBean(money, str1, str2, payUrl, (String)localObject1));
//                    localObject1 = new StringBuilder();
//                    ((StringBuilder)localObject1).append("生成成功,类型:");
//                    ((StringBuilder)localObject1).append(str2);
//                    ((StringBuilder)localObject1).append(",金额:");
//                    ((StringBuilder)localObject1).append(money);
//                    ((StringBuilder)localObject1).append("订单号:");
//                    ((StringBuilder)localObject1).append(str1);
//                    ((StringBuilder)localObject1).append("二维码:");
//                    ((StringBuilder)localObject1).append(payUrl);
//                    MainActivity.sendmsg(((StringBuilder)localObject1).toString(), payUrl);
                }
                else if (paramIntent.getAction().contentEquals(MainActivity.MSGRECEIVED_ACTION))
                {
                    MainActivity.sendmsg(paramIntent.getStringExtra("msg"), "");
                }
                return;
            }
            catch (Exception e)
            {
                XposedBridge.log(e.getMessage());
            }
        }
    }
}
