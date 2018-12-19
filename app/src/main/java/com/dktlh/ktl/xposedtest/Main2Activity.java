package com.dktlh.ktl.xposedtest;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dktlh.ktl.xposedtest.event.ExitEvent;
import com.dktlh.ktl.xposedtest.event.StartAppEvent;
import com.dktlh.ktl.xposedtest.event.WebsocketEvent;
import com.dktlh.ktl.xposedtest.model.TaskBean;
import com.dktlh.ktl.xposedtest.model.TaskBean2;
import com.dktlh.ktl.xposedtest.model.WsPayload;
import com.dktlh.ktl.xposedtest.model.req.CreateWsPayloadRequest;
import com.dktlh.ktl.xposedtest.model.req.NoticeWsPayloadRequest;
import com.dktlh.ktl.xposedtest.model.req.PayStateWsPayloadRequest;
import com.dktlh.ktl.xposedtest.model.resp.CreateWsPayloadResponse;
import com.dktlh.ktl.xposedtest.ui.fragment.AllTaskFragment;
import com.dktlh.ktl.xposedtest.utils.BitmapUtil;
import com.dktlh.ktl.xposedtest.utils.CryptoUtil;
import com.dktlh.ktl.xposedtest.utils.DateUtils;
import com.dktlh.ktl.xposedtest.utils.MyTimeTask;
import com.dktlh.ktl.xposedtest.utils.PayHelperUtils;
import com.dktlh.ktl.xposedtest.utils.PrefJsonUtil;
import com.dktlh.ktl.xposedtest.utils.PrefUtil;
import com.dktlh.ktl.xposedtest.utils.SignatureUtil;
import com.dktlh.ktl.xposedtest.utils.Tools;
import com.dktlh.ktl.xposedtest.websocket.WsManager;
import com.dktlh.ktl.xposedtest.websocket.common.ICallback;
import com.dktlh.ktl.xposedtest.websocket.request.Action;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Main2Activity extends AppCompatActivity {
    private TabLayout tabLayout;
    private TextView tv_tab_title;
    private ImageView mWechatStateIv, mAlipayStateIv;
    private Button mOpenAlipay,mOpenWechat , mOpenQQ , mOpenAliayHk;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    private List<String> listTitles;
    private PowerManager.WakeLock wakeLock;

    private TextView tvConfig , tvStart;

    public static String BILLRECEIVED_ACTION = "com.hamancom.hmpayhelp.billreceived";
    public static String MSGRECEIVED_ACTION = "com.hamancom.hmpayhelp.msgreceived";
    public static String NOTIFY_ACTION = "com.hamancom.hmpayhelp.notify";
    public static String QRCODERECEIVED_ACTION = "com.hamancom.hmpayhelp.qrcodereceived";
    public static String PAYSTATERECEIVED_ACTION = "com.hamancom.hmpayhelp.paystatereceived";
    private WebServer mVideoServer;
    public static int WEBSEERVER_PORT = 8888;
    private Main2Activity.BillReceived billReceived;
    private static TextView console;
    private static ImageView mQrIv;
    private static Context context;

    private EditText mAmountEt, mRemarkEt;
    private RadioGroup mRadioGroup;
    private Button mGetQrBtn;
    static NotificationManager notificationManager;
    static NotificationCompat.Builder mBuilder;

    //存储30个任务
    public static List<String> taskList = new ArrayList<>();

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
            if (Main2Activity.console != null) {
                if (Main2Activity.console.getText() != null)
                {
                    TextView localTextView;
                    StringBuilder localStringBuilder;
                    String[] strings = Main2Activity.console.getText().toString().split("\n");
                    if (strings != null && strings.length > 10) {
                        localStringBuilder = new StringBuilder();
                        localStringBuilder.append(str);
                        localStringBuilder.append("\n");
                        for (int i = 0; i < 10; i++) {
                            localStringBuilder.append(strings[i]);
                            localStringBuilder.append("\n");
                        }
                        localTextView = Main2Activity.console;
                        localTextView.setText(localStringBuilder.toString());
                    }
                    else
                    {
                        localTextView = Main2Activity.console;
                        localStringBuilder = new StringBuilder();
                        localStringBuilder.append(str);
                        localStringBuilder.append("\n");
                        localStringBuilder.append(Main2Activity.console.getText().toString());
                        localTextView.setText(localStringBuilder.toString());
                    }
                }
                else
                {
                    Main2Activity.console.setText(str);
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
        wakeLock.release();
        stopTimer();
        WsManager.getInstance().disconnect();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StartAppEvent event) {
        switch (event.getType()) {
            case 0:
                mOpenWechat.setEnabled((event.getState() == 0));
                mWechatStateIv.setBackgroundResource((event.getState() == 1) ? R.drawable.green_circle : R.drawable.red_circle);
                break;
            case 1:
                mOpenAlipay.setEnabled((event.getState() == 0));
                mAlipayStateIv.setBackgroundResource((event.getState() == 1) ? R.drawable.green_circle : R.drawable.red_circle);
                break;
            case 2:
                mOpenQQ.setEnabled((event.getState() == 0));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ExitEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebsocketEvent event) {
        console.setText(event.getMsg());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WsPayload event) {
        if (event.getAction().equals(Action.CREATE.getAction())) {
            if (!event.getJson().isEmpty()) {

//                if( ! SignatureUtil.verify(PrefJsonUtil.getProfile(WsApplication.getContext()).getToken()
//                        , event.getSignature() , event.getJson() )){
//
//                    String result = "签名校验失败";
//                    Main2Activity.sendmsg(result, "");
//                    return;
//                }

                Gson gson = new Gson();
                CreateWsPayloadResponse createWsPayloadResponse = gson.fromJson(event.getJson(), CreateWsPayloadResponse.class);
                String type = "";
                Intent paramAnonymousView = new Intent();
                switch (createWsPayloadResponse.getType()) {
                    case "wechat":
                        type = "微信";
                        paramAnonymousView.setAction(PayHelperUtils.WECHATSTART_ACTION);
                        break;
                    case "alipay":
                        type = "支付宝";
                        paramAnonymousView.setAction(PayHelperUtils.ALIPAYSTART_ACTION);
                        break;
                    case "qq":
                    case "QQ":
                        type = "QQ";
                        paramAnonymousView.setAction(PayHelperUtils.QQSTART_ACTION);
                        break;
                    case "alipayhk":
                        type = "alipayhk";
                        paramAnonymousView.setAction(PayHelperUtils.ALIPAYHK_ACTION);
                        break;
                }

//                TaskBean2 taskBean2 = new TaskBean2();
                Long time = Long.valueOf(DateUtils.getCurrentTimestamp());
//                taskBean2.setTime(time);
//                taskBean2.setMark(createWsPayloadResponse.getOrderCode());
//                taskBean2.setStste(0);
                if (taskList.size() > 30) {
                    taskList = taskList.subList(0, 29);
                }
//                for (int i = 0; i < taskList.size(); i++) {
//                    if (createWsPayloadResponse.getAmount().toPlainString().equals(taskList.get(i).getMark())) {
//
////                        String resp = createWsPayloadResponse.getOrderCode() + "," + strings[1] + "," + strings[0] + ",已存在" + "," + taskList.get(i).getUrl();
//
//                        //09:01:24 微信 123ddd123 230元 已生成
//                        String result = DateUtils.transForString2(time) +
//                                " " + type + " " + createWsPayloadResponse.getOrderCode() + " " + createWsPayloadResponse.getAmount().toPlainString() + " 已存在 " + taskList.get(i).getUrl();
//                        Main2Activity.sendmsg(result, "");
////                        WsManager.getInstance().sendReq(Action.QRCODE, resp, new ICallback() {
////                            @Override
////                            public void onSuccess(Object o) {
////
////                            }
////
////                            @Override
////                            public void onFail(String msg) {
////
////                            }
////                        });
//                        return;
//                    }
//                }
                taskList.add(0, new Gson().toJson(event));

                //09:01:24 微信 123ddd123 230元 已生成
                String result = DateUtils.transForString2(time) +
                        " " + type + " " + createWsPayloadResponse.getOrderCode() + " " + createWsPayloadResponse.getAmount().toPlainString() + " 已接收";
                Main2Activity.sendmsg(result, "");

                paramAnonymousView.putExtra("mark", createWsPayloadResponse.getOrderCode());
                paramAnonymousView.putExtra("money", createWsPayloadResponse.getAmount().toPlainString());
                Main2Activity.this.sendBroadcast(paramAnonymousView);
                event.setJson("OK");
                WsManager.getInstance().sendReq(Action.CREATE, event, new ICallback() {
                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });

            }
        }
    }


    private static final int TIMER = 999;
    private MyTimeTask task;

    private void setTimer(){
        task =new MyTimeTask(10000, new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(TIMER);
                //或者发广播，启动服务都是可以的
            }
        });
        task.start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIMER:
                    //在此执行定时操作
                    int uid = Tools.getPackageUid(context, "com.eg.android.AlipayGphone");
                    if(uid > 0){
                        boolean rstA = Tools.isAppRunning(context, "com.eg.android.AlipayGphone");
                        boolean rstB = Tools.isProcessRunning(context, uid);
                        String alipayState = "";
                        if(rstA||rstB){
                            //指定包名的程序正在运行中
//                            mOpenAlipay.setEnabled(false);
                            alipayState = "支付宝运行中";
                        }else{
                            //指定包名的程序未在运行中
//                            mOpenAlipay.setEnabled(true);
                            alipayState = "支付宝停止运行";
                        }
//                        WsManager.getInstance().sendReq(Action.QRCODE, alipayState, new ICallback() {
//                            @Override
//                            public void onSuccess(Object o) {
//
//                            }
//
//                            @Override
//                            public void onFail(String msg) {
//
//                            }
//                        });
                    } else{
                        //应用未安装
                    }

                    int uid2 = Tools.getPackageUid(context, "com.tencent.mm");
                    if(uid2 > 0){
                        boolean rstA = Tools.isAppRunning(context, "com.tencent.mm");
                        boolean rstB = Tools.isProcessRunning(context, uid2);
                        String wechatState = "";
                        if(rstA||rstB){
                            //指定包名的程序正在运行中
                            mOpenWechat.setEnabled(false);
                            wechatState = "微信运行中";
                        }else{
                            //指定包名的程序未在运行中
                            mOpenWechat.setEnabled(true);
                            wechatState = "微信停止运行";
                        }
//                        WsManager.getInstance().sendReq(Action.QRCODE, wechatState, new ICallback() {
//                            @Override
//                            public void onSuccess(Object o) {
//
//                            }
//
//                            @Override
//                            public void onFail(String msg) {
//
//                            }
//                        });
                    } else{
                        //应用未安装
                    }

                    int uid3 = Tools.getPackageUid(context, "com.tencent.mobileqq");
                    if(uid3 > 0){
                        boolean rstA = Tools.isAppRunning(context, "com.tencent.mobileqq");
                        boolean rstB = Tools.isProcessRunning(context, uid3);
                        String QQState = "";
                        if(rstA||rstB){
                            //指定包名的程序正在运行中
                            mOpenQQ.setEnabled(false);
                            QQState = "QQ运行中";
                        }else{
                            //指定包名的程序未在运行中
                            mOpenQQ.setEnabled(true);
                            QQState = "QQ停止运行";
                        }
//                        WsManager.getInstance().sendReq(Action.QRCODE, wechatState, new ICallback() {
//                            @Override
//                            public void onSuccess(Object o) {
//
//                            }
//
//                            @Override
//                            public void onFail(String msg) {
//
//                            }
//                        });
                    } else{
                        //应用未安装
                    }

                    int uid4 = Tools.getPackageUid(context, "hk.alipay.wallet");
                    if(uid4 > 0){
                        boolean rstA = Tools.isAppRunning(context, "hk.alipay.wallet");
                        boolean rstB = Tools.isProcessRunning(context, uid4);
                        String QQState = "";
                        if(rstA||rstB){
                            //指定包名的程序正在运行中
                            mOpenAliayHk.setEnabled(false);
                            QQState = "HK运行中";
                        }else{
                            //指定包名的程序未在运行中
                            mOpenAliayHk.setEnabled(true);
                            QQState = "HK停止运行";
                        }
//                        WsManager.getInstance().sendReq(Action.QRCODE, wechatState, new ICallback() {
//                            @Override
//                            public void onSuccess(Object o) {
//
//                            }
//
//                            @Override
//                            public void onFail(String msg) {
//
//                            }
//                        });
                    } else{
                        //应用未安装
                    }

                    break;
                default:
                    break;
            }
        }
    };

    private void stopTimer(){
        task.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        context = getApplicationContext();
        console = findViewById(R.id.console);
        tabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.mViewPager);
        mWechatStateIv = findViewById(R.id.mWechatStateIv);
        mAlipayStateIv = findViewById(R.id.mAlipayStateIv);
        mOpenAlipay = findViewById(R.id.mOpenAlipay);
        mOpenWechat = findViewById(R.id.mOpenWechat);
        mOpenQQ = findViewById(R.id.bottom_qq);
        mOpenAliayHk = findViewById(R.id.bottom_qq_hk);
        tvConfig = findViewById(R.id.tv_config);
        tvStart = findViewById(R.id.tv_start);
        initData();

        tvConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main2Activity.this, ConfigDetailActivity.class));
            }
        });

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("停止".equals(tvStart.getText().toString())){
                    WsManager.getInstance().disconnect();
                    tvStart.setText("开始");
                }else {
                    WsManager.getInstance().reconnect();
                    tvStart.setText("停止");
                }
            }
        });

        EventBus.getDefault().register(this);

        if(PrefJsonUtil.getProfile(WsApplication.getContext()) != null && ! TextUtils.isEmpty( PrefJsonUtil.getProfile(WsApplication.getContext()).getToken()) ){
            WsManager.getInstance().init();
        }

        this.billReceived = new Main2Activity.BillReceived();
        IntentFilter paramBundle = new IntentFilter();
        paramBundle.addAction(BILLRECEIVED_ACTION);
        paramBundle.addAction(MSGRECEIVED_ACTION);
        paramBundle.addAction(QRCODERECEIVED_ACTION);
        paramBundle.addAction(PAYSTATERECEIVED_ACTION);
        registerReceiver(this.billReceived, paramBundle);

        Main2Activity.GetQrCodeReceived getQrCodeReceived = new Main2Activity.GetQrCodeReceived();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("server_get_code");
        registerReceiver(getQrCodeReceived, intentFilter);
//        paramBundle = new AlarmReceiver();
//        IntentFilter localIntentFilter = new IntentFilter();
//        localIntentFilter.addAction(NOTIFY_ACTION);
//        registerReceiver(paramBundle, localIntentFilter);
//        startService(new Intent(this, DaemonService.class));

    }

    class GetQrCodeReceived extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            String type = "";
//            String text = intent.getStringExtra("server_get_code");
//            String[] strings = text.split(",");
//            if (strings.length != 3) {
//                return;
//            }
//            Intent paramAnonymousView = new Intent();
//            switch (strings[0]) {
//                case "wechat":
//                    type = "微信";
//                    paramAnonymousView.setAction(PayHelperUtils.WECHATSTART_ACTION);
//                    break;
//                case "alipay":
//                    type = "支付宝";
//                    paramAnonymousView.setAction(PayHelperUtils.ALIPAYSTART_ACTION);
//                    break;
//            }
//
//            TaskBean2 taskBean2 = new TaskBean2();
//            Long time = Long.valueOf(DateUtils.getCurrentTimestamp());
//            taskBean2.setTime(time);
//            taskBean2.setMark(strings[1]);
//            taskBean2.setStste(0);
//            if (taskList.size() > 30) {
//                taskList = taskList.subList(0, 29);
//            }
////            for (int i = 0; i < taskList.size(); i++) {
////                if (strings[1].equals(taskList.get(i).getMark())) {
////
////                    String resp = strings[2] + "," + strings[1] + "," + strings[0] + ",已存在" + "," + taskList.get(i).getUrl();
////
////                    //09:01:24 微信 123ddd123 230元 已生成
////                    String result = DateUtils.transForString2(time) +
////                            " " + type + " " + strings[1] + " " + strings[2] + " 已存在 " + taskList.get(i).getUrl();
////                    Main2Activity.sendmsg(result, "");
////                    WsManager.getInstance().sendReq(Action.QRCODE, resp, new ICallback() {
////                        @Override
////                        public void onSuccess(Object o) {
////
////                        }
////
////                        @Override
////                        public void onFail(String msg) {
////
////                        }
////                    });
////                    return;
////                }
////            }
////            taskList.add(0, taskBean2);
//
//            //09:01:24 微信 123ddd123 230元 已生成
//            String result = DateUtils.transForString2(time) +
//                    " " + type + " " + strings[1] + " " + strings[2] + " 已接收";
//            Main2Activity.sendmsg(result, "");
//
//            paramAnonymousView.putExtra("mark", strings[1]);
//            paramAnonymousView.putExtra("money", strings[2]);
//            Main2Activity.this.sendBroadcast(paramAnonymousView);

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
                if (paramIntent.getAction().contentEquals(Main2Activity.BILLRECEIVED_ACTION))
                {

                    str1 = paramIntent.getStringExtra("bill_no");
                    str2 = paramIntent.getStringExtra("bill_money");
                    localObject1 = paramIntent.getStringExtra("bill_mark");
                    String type = paramIntent.getStringExtra("bill_type");
                    String resp = str1 + "," + str2 + "," + localObject1 + "," + type;

                    Log.d("bill_no" , str1);
                    Log.d("bill_mark" , localObject1.toString());


                    String payType = "";
                    if (type.equals("alipay")) {
                        payType = "支付宝";
                    } else if (type.equals("wechat")) {
                        payType = "微信";
                    }else if(type.equals("qq")){
                        payType = "qq";
                    }

                    //09:01:24 微信 123ddd123 230元 已生成
                    String result = DateUtils.transForString2(Long.valueOf(DateUtils.getCurrentTimestamp())) +
                            " " + payType + " " + (String) localObject1 + " " + str2 + " 已支付";

                    Gson gson = new Gson();
                    WsPayload wsPayload = new WsPayload();
                    NoticeWsPayloadRequest noticeWsPayloadRequest = new NoticeWsPayloadRequest();

                    noticeWsPayloadRequest.setAmount(new BigDecimal(str2));
                    noticeWsPayloadRequest.setOrderCode(localObject1.toString());
                    noticeWsPayloadRequest.setAdditional(str1);
                    noticeWsPayloadRequest.setType(type);

                    wsPayload.setUuid(CryptoUtil.uuid());
                    wsPayload.setAction(Action.NOTICE.getAction());
                    String json = gson.toJson(noticeWsPayloadRequest);
                    wsPayload.setJson(json);
                    wsPayload.setSignature(SignatureUtil.hmacSha1(PrefJsonUtil.getProfile(WsApplication.getContext()).getToken() , json));

//                    for (int i = 0; i < taskList.size(); i++) {
//                        if (taskList.get(i).contains(localObject1.toString())) {
//                            noticeWsPayloadRequest.setAmount(new BigDecimal(str2));
//                            noticeWsPayloadRequest.setOrderCode(localObject1.toString());
//                            noticeWsPayloadRequest.setAdditional(str1);
//                            noticeWsPayloadRequest.setType(type);
//
//                            WsPayload ws = gson.fromJson(taskList.get(i), WsPayload.class);
//                            wsPayload.setUuid(ws.getUuid());
//                            wsPayload.setAction(Action.NOTICE.getAction());
//                            String json = gson.toJson(noticeWsPayloadRequest);
//                            wsPayload.setJson(json);
//                            wsPayload.setSignature(SignatureUtil.hmacSha1(PrefJsonUtil.getProfile(WsApplication.getContext()).getToken() , json));
//
////                            String signature = SignatureUtil.hmacSha1(token , gson.toJson(noticeWsPayloadRequest));
//
//                        }
//                    }
                    Log.d("bill--------Uuid" , wsPayload.getUuid());
                    Log.d("bill---------Json" , wsPayload.getJson());
                    Log.d("bill---------Action" , wsPayload.getAction());

                    WsManager.getInstance().sendReq(Action.NOTICE, wsPayload, new ICallback() {
                        @Override
                        public void onSuccess(Object o) {

                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });
//                    if (type.equals("alipay")) {
//                        type = "支付宝";
//                    } else if (type.equals("wechat")) {
//                        type = "微信";
//                    }else if(str2.equals("qq")){
//                        str2 = "QQ";
//                    }
//
//                    //09:01:24 微信 123ddd123 230元 已生成
//                    String result = DateUtils.transForString2(Long.valueOf(DateUtils.getCurrentTimestamp())) +
//                            " " + type + " " + (String) localObject1 + " " + str2 + " 已支付";
                    Main2Activity.sendmsg(result, "");

//                    TaskBean taskBean = new TaskBean();
//                    taskBean.setType(type);
//                    taskBean.setMark((String) localObject1);
//                    taskBean.setState(2);
//                    taskBean.setUrl("");
//                    taskBean.setPrice(str2);
//                    taskBean.setTime(Long.valueOf(DateUtils.getCurrentTimestamp()));
//                    EventBus.getDefault().post(taskBean);

                }
                else if (paramIntent.getAction().contentEquals(Main2Activity.QRCODERECEIVED_ACTION)) {


                    if(IsForeground(Main2Activity.this) == false) {
                        ActivityManager am = (ActivityManager) Main2Activity.this.getSystemService(Context.ACTIVITY_SERVICE) ;
                        am.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
                    }

                    String type = "";
                    money = paramIntent.getStringExtra("money");
                    str1 = paramIntent.getStringExtra("mark");
                    str2 = paramIntent.getStringExtra("type");
                    payUrl = paramIntent.getStringExtra("payurl");
                    type = str2;
                    if (str2.equals("al")) {
                        str2 = "支付宝";
                    } else if (str2.equals("wx")) {
                        str2 = "微信";
                    }else if(str2.equals("qq")){
                        str2 = "qq";
                    }else if(str2.equals("alipayhk")){
                        str2 = "alipayhk";
                    }
                    Long time = Long.valueOf(DateUtils.getCurrentTimestamp());

                    Log.d("money" , money);
                    Log.d("mark" , str1);
                    Log.d("type" , str2);
                    Log.d("payurl" , payUrl);

                    //09:01:24 微信 123ddd123 230元 已生成
                    String result = DateUtils.transForString2(time) +
                            " " + str2 + " " + str1 + " " + money + " 已生成 " + payUrl;
                    String resp = money + "," + str1 + "," + str2 + ",已生成" + "," + payUrl;
//                    for (int i = 0; i < taskList.size(); i++) {
//                        if (str1.equals(taskList.get(i).getMark())) {
//                            taskList.get(i).setStste(1);
//                            taskList.get(i).setUrl(payUrl);
//                        }
//                        if (str1.equals(taskList.get(i).getMark()) && (time - taskList.get(i).getTime()) > 5000) {
//                            //09:01:24 微信 123ddd123 230元 已生成
//                            result = DateUtils.transForString2(time) +
//                                    " " + str2 + " " + str1 + " " + money + " 生成超时" + " ";
//                            resp = money + "," + str1 + "," + str2 + ",生成超时" + "," + "";
//                        }
//                    }

                    Gson gson = new Gson();
                    WsPayload wsPayload = new WsPayload();
                    CreateWsPayloadRequest createWsPayloadRequest = new CreateWsPayloadRequest();
                    for (int i = 0; i < taskList.size(); i++) {
                        if (taskList.get(i).contains(str1)) {
                            createWsPayloadRequest.setAmount(new BigDecimal(money));
                            createWsPayloadRequest.setOrderCode(str1);
                            createWsPayloadRequest.setQrcode(payUrl);
                            createWsPayloadRequest.setType(type);

                            WsPayload ws = gson.fromJson(taskList.get(i), WsPayload.class);
                            wsPayload.setUuid(ws.getUuid());
                            wsPayload.setAction(Action.QRCODE.getAction());
                            String json = gson.toJson(createWsPayloadRequest);
                            wsPayload.setJson(json);
                            wsPayload.setSignature(SignatureUtil.hmacSha1(PrefJsonUtil.getProfile(WsApplication.getContext()).getToken() , json));

                        }
                    }

                    Log.d("------------Uuid" , wsPayload.getUuid());
                    Log.d("----------------Json" , wsPayload.getJson());
                    Log.d("-----------------Action" , wsPayload.getAction());

                    WsManager.getInstance().sendReq(Action.QRCODE, wsPayload, new ICallback() {
                        @Override
                        public void onSuccess(Object o) {

                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });

                    Main2Activity.sendmsg(result, "");
                }
                else if (paramIntent.getAction().contentEquals(Main2Activity.MSGRECEIVED_ACTION)) {
                    Main2Activity.sendmsg(paramIntent.getStringExtra("msg"), "");
                }
                else if (paramIntent.getAction().contentEquals(Main2Activity.PAYSTATERECEIVED_ACTION)) {

                    String type = "alipay";
                    BigDecimal amount;
                    if( !"".equals(paramIntent.getStringExtra("amount")) ){
                        amount = new BigDecimal(paramIntent.getStringExtra("amount"));
                        Log.d("Xposed" , "amount:"+ amount);
                    }else {
                        amount = null;
                    }

                    String userId = "";
                    if(!"".equals(paramIntent.getStringExtra("userId"))){
                        userId = paramIntent.getStringExtra("userId");
                        Log.d("Xposed" , "userId:"+ userId);
                    }else {
                        userId = null;
                    }

                    String payerUserId = "";
                    if(!"".equals(paramIntent.getStringExtra("payerUserId"))){
                        payerUserId = paramIntent.getStringExtra("payerUserId");
                        Log.d("Xposed" , "payerUserId:"+ payerUserId);
                    }else {
                        payerUserId = null;
                    }

                    String transferNo = "";
                    if(!"".equals(paramIntent.getStringExtra("transferNo"))){
                        transferNo =  paramIntent.getStringExtra("transferNo");
                        Log.d("Xposed" , "transferNo:"+ transferNo);
                    }else {
                        transferNo = null;
                    }

                    String state = "";
                    if (paramIntent.getStringExtra("state").equals("0"))
                    {
                        state = "支付中";
                    }
                    if (paramIntent.getStringExtra("state").equals("2"))
                    {
                        state = "支付失败";
                    }
                    if (paramIntent.getStringExtra("state").equals("1"))
                    {
                        state = "支付成功";
                    }

                    Gson gson = new Gson();
                    WsPayload wsPayload = new WsPayload();
                    PayStateWsPayloadRequest payloadRequest = new PayStateWsPayloadRequest();
                    payloadRequest.setAmount(amount);
                    payloadRequest.setType(type);
                    payloadRequest.setUserId(userId);
                    payloadRequest.setPayerUserId(payerUserId);
                    payloadRequest.setTransferNo(transferNo);
                    payloadRequest.setState(state);

                    Log.d("Xposed" , "支付通知"+ payloadRequest.toString());

                    wsPayload.setUuid(CryptoUtil.uuid());
                    wsPayload.setAction(Action.PAYSTATE.getAction());
                    String json = gson.toJson(payloadRequest);
                    Log.d("Xposed" , "json"+ json);
                    wsPayload.setJson(json);

                    Log.d("Xposed" , PrefJsonUtil.getProfile(WsApplication.getContext()).getToken());
                    Log.d("Xposed" , SignatureUtil.hmacSha1(PrefJsonUtil.getProfile(WsApplication.getContext()).getToken() , json));
                    wsPayload.setSignature(SignatureUtil.hmacSha1(PrefJsonUtil.getProfile(WsApplication.getContext()).getToken() , json));

                    Log.d("Xposed" , "走到这了");

                    WsManager.getInstance().sendReq(Action.PAYSTATE, wsPayload, new ICallback() {
                        @Override
                        public void onSuccess(Object o) {
                            Log.d("Xposed" , "成功返回值"+o.toString());
                        }

                        @Override
                        public void onFail(String msg) {
                            Log.d("Xposed" , "失败返回值"+msg);
                        }
                    });

                    Main2Activity.sendmsg(payloadRequest.toString(), "");

                }
                return;
            }
            catch (Exception e)
            {
                Log.d("Xposed" , "Exception:"+e.toString());
            }
        }
    }

    public boolean IsForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    private void initData() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //保持app再锁屏情况下处于唤醒状态
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "myservice");
        wakeLock.acquire();


        fragments = new ArrayList<>();
        listTitles = new ArrayList<>();

        listTitles.add("二维码列表");
        listTitles.add("已支付列表");
        listTitles.add("错误日志列表");
        listTitles.add("所有任务");


        //mTabLayout.setTabMode(TabLayout.SCROLL_AXIS_HORIZONTAL);//设置tab模式，当前为系统默认模式
        for (int i=0;i<listTitles.size();i++){
            tabLayout.addTab(tabLayout.newTab().setText(listTitles.get(i)));//添加tab选项
        }

        fragments.add(QrCodeFragment.newInstance("qrcode"));
        fragments.add(PayListFragment.newInstance("paylist"));
        fragments.add(LogListFragment.newInstance("loglist"));
        fragments.add(AllTaskFragment.newInstance("alltast"));

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
            @Override
            public CharSequence getPageTitle(int position) {
                return listTitles.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
            }
        };
        mViewPager.setAdapter(mAdapter);


        mViewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        tabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

        //打开微信
        mOpenWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isWeixinAvilible(Main2Activity.this)) {
                    Intent intent = new Intent();
                    ComponentName cmp=new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    startActivity(intent);

//                    StartAppEvent startAppEvent = new StartAppEvent();
//                    startAppEvent.setState(1);
//                    startAppEvent.setType(0);
//                    EventBus.getDefault().post(startAppEvent);
                } else {
                    Toast.makeText(Main2Activity.this, "请先安装微信", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //打开支付宝
        mOpenAlipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    PackageManager packageManager
//                            = Main2Activity.this.getApplicationContext().getPackageManager();
//                    Intent intent = packageManager.
//                            getLaunchIntentForPackage("com.eg.android.AlipayGphone");
//                    startActivity(intent);

                    Intent intent = new Intent();
                    ComponentName cmp=new ComponentName("com.eg.android.AlipayGphone","com.alipay.mobile.payee.ui.PayeeQRActivity");
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    startActivity(intent);

//                    StartAppEvent startAppEvent = new StartAppEvent();
//                    startAppEvent.setState(1);
//                    startAppEvent.setType(1);
//                    EventBus.getDefault().post(startAppEvent);
//                }catch (Exception e) {
//                    String url = "https://ds.alipay.com/?from=mobileweb";
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(url));
//                    startActivity(intent);
//                }
                //com.alipay.mobile.payee.ui.PayeeQRActivity
            }
        });

        //打开QQ
        mOpenQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkApkExist(Main2Activity.this, "com.tencent.mobileqq")) {
                    Intent intent = new Intent();                                           //cooperation.qzone.QzoneFeedsPluginProxyActivity
                    ComponentName cmp=new ComponentName("com.tencent.mobileqq","com.tencent.mobileqq.activity.SplashActivity");
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    startActivity(intent);
                } else {
                    Toast.makeText(Main2Activity.this, "请先安装QQ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //打开港版支付宝
        mOpenAliayHk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkApkExist(Main2Activity.this, "hk.alipay.wallet")) {
                    Intent intent = new Intent();                                           //cooperation.qzone.QzoneFeedsPluginProxyActivity
                    ComponentName cmp=new ComponentName("hk.alipay.wallet","com.eg.android.AlipayGphone.AlipayLogin");
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    startActivity(intent);
                } else {
                    Toast.makeText(Main2Activity.this, "请先安装港版支付宝", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setTimer();
    }

    //检测QQ是否在本手机中安装；
    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
