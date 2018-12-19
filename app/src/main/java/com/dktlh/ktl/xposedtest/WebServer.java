package com.dktlh.ktl.xposedtest;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.dktlh.ktl.xposedtest.utils.AbSharedUtil;
import com.dktlh.ktl.xposedtest.utils.BitmapUtil;
import com.dktlh.ktl.xposedtest.utils.MD5;
import com.dktlh.ktl.xposedtest.utils.PayHelperUtils;
import com.dktlh.ktl.xposedtest.utils.QrCodeBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by dengkaitao on 2018/6/30 18:41.
 * Email：724279138@qq.com
 */
public class WebServer extends NanoHTTPD {
    public static String MSGRECEIVED_ACTION = "com.hamancom.hmpayhelp.msgreceived";
    private static final String REQUEST_GETPAY = "/paygateway";
    private static final String REQUEST_GETRESULT = "/getresult";
    private static final String REQUEST_QUERY = "/query";
    private static final String REQUEST_ROOT = "/";
    private static final String REQUEST_TEST = "/test";
    private static final String REQUEST_WECHAT = "/wechat";
    public static final String TAG = WebServer.class.getSimpleName();
    private Context context;

    public WebServer(Context paramContext, int paramInt) {
        super(paramInt);
        this.context = paramContext;
    }

    public WebServer(int port) {
        super(port);
    }

    public WebServer(String hostname, int port) {
        super(hostname, port);
    }

    protected String getQuotaStr(String paramString)
    {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("\"");
        localStringBuilder.append(paramString);
        localStringBuilder.append("\"");
        return localStringBuilder.toString();
    }

    public NanoHTTPD.Response responseQRCode(NanoHTTPD.IHTTPSession paramIHTTPSession, String paramString1, String paramString2)
    {
        paramString1 = BitmapUtil.bitmapToBase64(BitmapUtil.createQRImage(paramString1, 240, null));
        Object localObject = new StringBuilder();
        ((StringBuilder)localObject).append("\"data:image/gif;base64,");
        ((StringBuilder)localObject).append(paramString1);
        ((StringBuilder)localObject).append("\"");
        localObject = ((StringBuilder)localObject).toString();
        StringBuilder sb1 = new StringBuilder();
        sb1.append("<!DOCTYPE html><html><body>");
        sb1.append("<div style=\"width:100%;height:100%;text-align:center;padding-top:20px;\">");
        sb1.append("二维码生成测试<br>");
        sb1.append("<image ");
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("src=");
        localStringBuilder.append((String)localObject);
        localStringBuilder.append(" >");
        sb1.append(localStringBuilder.toString());
        sb1.append("</image><br>");
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("http://");
        ((StringBuilder)localObject).append((String)paramIHTTPSession.getHeaders().get("host"));
        String s = ((StringBuilder)localObject).toString();
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("获取成功，查询订单是否支付：<a href='");
        ((StringBuilder)localObject).append(s);
        ((StringBuilder)localObject).append("/query?no=");
        ((StringBuilder)localObject).append(paramString2);
        ((StringBuilder)localObject).append("' target='_blank'>查询</a><br><br>");
        sb1.append(((StringBuilder)localObject).toString());
        sb1.append("</div>");
        sb1.append("</body></html>\n");
        Log.i("server", paramString1.toString());
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html;charset=UTF-8", paramString1.toString());
    }

    public NanoHTTPD.Response responseJson(NanoHTTPD.IHTTPSession paramIHTTPSession, String paramString)
    {
        Log.i("server", paramString);
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json;charset=UTF-8", paramString);
    }

    public NanoHTTPD.Response responseRootPage(NanoHTTPD.IHTTPSession paramIHTTPSession)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><body>");
        sb.append("Hello World!");
        sb.append("</body></html>\n");
        return newFixedLengthResponse(sb.toString());
    }

    public NanoHTTPD.Response responseText(NanoHTTPD.IHTTPSession paramIHTTPSession, String paramString)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><body>");
        sb.append("<div style=\"width:100%;height:100%;text-align:center;padding-top:20px;\">");
        sb.append(paramString);
        sb.append("</div>");
        sb.append("</body></html>\n");
        Log.i("server", paramIHTTPSession.toString());
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html;charset=UTF-8", sb.toString());
    }


    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession paramIHTTPSession)
    {
        Object localObject1 = TAG;
        Object localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("OnRequest: ");
        ((StringBuilder)localObject2).append(paramIHTTPSession.getUri());
        Log.d((String)localObject1, ((StringBuilder)localObject2).toString());
        label2281:
        label2284:
        label2287:
        label2290:
        label2293:
        for (;;)
        {
            try
            {
                if ("/".equals(paramIHTTPSession.getUri())) {
                    return responseRootPage(paramIHTTPSession);
                }
                Object localObject4;
                Object localObject3 = null;
                double d;
                int i;
                if ("/wechat".equals(paramIHTTPSession.getUri()))
                {
                    localObject1 = paramIHTTPSession.getParms();
                    localObject4 = (String)((Map)localObject1).get("money");
                    localObject3 = (String)((Map)localObject1).get("mark");
                    localObject2 = (String)((Map)localObject1).get("type");
                    if (localObject2 != null)
                    {
                        localObject1 = localObject2;
                        if (!((String)localObject2).equals(""))
                        {
                            d = Double.parseDouble((String)localObject4);
                            if (((String)localObject1).equals("alipay"))
                            {
                                if (d > 50000.0D) {
                                    return responseText(paramIHTTPSession, "支付宝最大支持单笔50000元支付！");
                                }
                            }
                            else if ((((String)localObject1).equals("wechat")) && (d > 15000.0D)) {
                                return responseText(paramIHTTPSession, "微信最大支持单笔15000元支付！");
                            }
                            if ((((String)localObject1).equals("alipay")) && (!PayHelperUtils.isAppRunning(this.context, "com.eg.android.AlipayGphone"))) {
                                PayHelperUtils.startAPP(this.context, "com.eg.android.AlipayGphone");
                            } else if ((((String)localObject1).equals("wechat")) && (!PayHelperUtils.isAppRunning(this.context, "com.tencent.mm"))) {
                                PayHelperUtils.startAPP(this.context, "com.tencent.mm");
                            }
                            localObject2 = new ArrayList();
                            PayHelperUtils.sendAppMsg((String)localObject4, (String)localObject3, (String)localObject1, this.context);
                            i = 0;
//                            localObject4 = new DBManager(CustomApplcation.getInstance().getApplicationContext());
//                            localObject1 = localObject2;
//                            if ((i < 30) && (((List)localObject1).size() == 0))
//                            {
//                                localObject1 = ((DBManager)localObject4).FindQrCodes_order((String)localObject3);
//                                i += 1;
//                                Thread.sleep(500L);
//                                continue;
//                            }
//                            if (((List)localObject1).size() == 0)
//                            {
//                                PayHelperUtils.startAPP();
//                                return responseText(paramIHTTPSession, "获取超时....");
//                            }
                            localObject1 = ((QrCodeBean)((List)localObject1).get(0)).getPayurl();
                            PayHelperUtils.startAPP();
                            return responseQRCode(paramIHTTPSession, (String)localObject1, (String)localObject3);
                        }
                    }
                }
                else
                {
                    Object localObject5;
                    if ("/paygateway".equals(paramIHTTPSession.getUri()))
                    {
                        localObject4 = new JSONObject();
                        localObject2 = paramIHTTPSession.getParms();
                        localObject5 = (String)((Map)localObject2).get("oid");
                        localObject3 = (String)((Map)localObject2).get("amt");
                        Object localObject6 = (String)((Map)localObject2).get("type");
                        localObject1 = (String)((Map)localObject2).get("account");
                        String str2 = (String)((Map)localObject2).get("sign");
                        if (localObject5 == null) {
                            break label2293;
                        }
                        if (!((String)localObject5).equals(""))
                        {
                            if (localObject3 == null) {
                                break label2290;
                            }
                            if (!((String)localObject3).equals(""))
                            {
                                if (localObject1 == null) {
                                    break label2287;
                                }
                                if (!((String)localObject1).equals(""))
                                {
                                    if (localObject6 == null) {
                                        break label2284;
                                    }
                                    if (!((String)localObject6).equals(""))
                                    {
                                        if (str2 == null) {
                                            break label2281;
                                        }
                                        if (!str2.equals(""))
                                        {
                                            d = Double.parseDouble((String)localObject3);
                                            if (((String)localObject6).equals("al"))
                                            {
                                                if (d > 50000.0D)
                                                {
                                                    ((JSONObject)localObject4).put("code", 1);
                                                    ((JSONObject)localObject4).put("msg", "支付宝最大支持单笔50000元支付！");
                                                    return responseJson(paramIHTTPSession, ((JSONObject)localObject4).toString());
                                                }
                                            }
                                            else if ((((String)localObject6).equals("wx")) && (d > 15000.0D))
                                            {
                                                ((JSONObject)localObject4).put("code", 1);
                                                ((JSONObject)localObject4).put("msg", "微信最大支持单笔15000元支付！");
                                                return responseJson(paramIHTTPSession, ((JSONObject)localObject4).toString());
                                            }
                                            localObject2 = AbSharedUtil.getString(this.context, "ApiKey");
                                            localObject1 = AbSharedUtil.getString(this.context, "AccountId");
                                            if (TextUtils.isEmpty((CharSequence)localObject1))
                                            {
                                                ((JSONObject)localObject4).put("code", 1);
                                                ((JSONObject)localObject4).put("msg", "账户绑定不正确！");
                                                return responseJson(paramIHTTPSession, ((JSONObject)localObject4).toString());
                                            }
                                            Object localObject7 = new StringBuilder();
                                            ((StringBuilder)localObject7).append("oid=");
                                            ((StringBuilder)localObject7).append((String)localObject5);
                                            ((StringBuilder)localObject7).append("&amt=");
                                            ((StringBuilder)localObject7).append((String)localObject3);
                                            ((StringBuilder)localObject7).append("&type=");
                                            ((StringBuilder)localObject7).append((String)localObject6);
                                            ((StringBuilder)localObject7).append((String)localObject2);
                                            localObject7 = ((StringBuilder)localObject7).toString();
                                            String str3 = MD5.md5((String)localObject7);
                                            if (!str2.equals(str3))
                                            {
                                                ((JSONObject)localObject4).put("code", 1);
                                                localObject1 = new StringBuilder();
                                                ((StringBuilder)localObject1).append("签名失败！");
                                                ((StringBuilder)localObject1).append((String)localObject7);
                                                ((StringBuilder)localObject1).append(",sgin=");
                                                ((StringBuilder)localObject1).append(str3);
                                                ((JSONObject)localObject4).put("msg", ((StringBuilder)localObject1).toString());
                                                return responseJson(paramIHTTPSession, ((JSONObject)localObject4).toString());
                                            }
                                            if ((((String)localObject6).equals("al")) && (!PayHelperUtils.isAppRunning(this.context, "com.eg.android.AlipayGphone"))) {
                                                PayHelperUtils.startAPP(this.context, "com.eg.android.AlipayGphone");
                                            } else if ((((String)localObject6).equals("wx")) && (!PayHelperUtils.isAppRunning(this.context, "com.tencent.mm"))) {
                                                PayHelperUtils.startAPP(this.context, "com.tencent.mm");
                                            } else if ((((String)localObject6).equals("qq")) && (!PayHelperUtils.isAppRunning(this.context, "com.tencent.mobileqq"))){
                                                PayHelperUtils.startAPP(this.context, "com.tencent.mobileqq");
                                            }
                                            PayHelperUtils.sendAppMsg((String)localObject3, (String)localObject5, (String)localObject6, this.context);
                                            localObject3 = new ArrayList();
//                                            localObject6 = new DBManager(CustomApplcation.getInstance().getApplicationContext());
//                                            i = 0;
//                                            if ((i < 30) && (((List)localObject3).size() == 0))
//                                            {
//                                                localObject3 = ((DBManager)localObject6).FindQrCodes_order((String)localObject5);
//                                                i += 1;
//                                                Thread.sleep(500L);
//                                                continue;
//                                            }
//                                            if (((List)localObject3).size() == 0)
//                                            {
//                                                PayHelperUtils.startAPP();
//                                                ((JSONObject)localObject4).put("code", 1);
//                                                localObject1 = new StringBuilder();
//                                                ((StringBuilder)localObject1).append("获取超时");
//                                                ((StringBuilder)localObject1).append((String)localObject5);
//                                                ((JSONObject)localObject4).put("msg", ((StringBuilder)localObject1).toString());
//                                                return responseJson(paramIHTTPSession, ((JSONObject)localObject4).toString());
//                                            }
                                            localObject1 = ((QrCodeBean)((List)localObject3).get(0)).getPayurl();
                                            PayHelperUtils.startAPP();
                                            ((JSONObject)localObject4).put("msg", "获取成功");
                                            ((JSONObject)localObject4).put("data", localObject1);
                                            ((JSONObject)localObject4).put("code", 0);
                                            return responseJson(paramIHTTPSession, ((JSONObject)localObject4).toString());
                                        }
                                        ((JSONObject)localObject4).put("code", 1);
                                        ((JSONObject)localObject4).put("msg", "签名不能为空");
                                        return responseJson(paramIHTTPSession, ((JSONObject)localObject4).toString());
                                    }
                                    ((JSONObject)localObject4).put("code", 1);
                                    ((JSONObject)localObject4).put("msg", "通道类型不能为空");
                                    return responseJson(paramIHTTPSession, ((JSONObject)localObject4).toString());
                                }
                                ((JSONObject)localObject4).put("code", 1);
                                ((JSONObject)localObject4).put("msg", "支付账户不能为空");
                                return responseJson(paramIHTTPSession, ((JSONObject)localObject4).toString());
                            }
                            ((JSONObject)localObject4).put("code", 1);
                            ((JSONObject)localObject4).put("msg", "金额不能为空");
                            return responseJson(paramIHTTPSession, ((JSONObject)localObject4).toString());
                        }
                        ((JSONObject)localObject4).put("code", 1);
                        ((JSONObject)localObject4).put("msg", "订单号不能为空");
                        return responseJson(paramIHTTPSession, ((JSONObject)localObject4).toString());
                    }
                    if ("/query".equals(paramIHTTPSession.getUri()))
                    {
                        localObject1 = (String)paramIHTTPSession.getParms().get("no");
                        new ArrayList();
//                        localObject1 = new DBManager(CustomApplcation.getInstance().getApplicationContext()).FindOrders((String)localObject1);
//                        if ((localObject1 != null) && (((List)localObject1).size() > 0))
//                        {
//                            localObject1 = PayHelperUtils.stampToDate(((OrderBean)((List)localObject1).get(0)).getDt());
//                            localObject2 = new StringBuilder();
//                            ((StringBuilder)localObject2).append("当前订单已支付，支付时间:");
//                            ((StringBuilder)localObject2).append((String)localObject1);
//                            ((StringBuilder)localObject2).append("....");
//                            return responseText(paramIHTTPSession, ((StringBuilder)localObject2).toString());
//                        }
                        return responseText(paramIHTTPSession, "当前订单未支付....");
                    }
                    if ("/getresult".equals(paramIHTTPSession.getUri()))
                    {
                        localObject1 = (String)paramIHTTPSession.getParms().get("trade_no");
                        new ArrayList();
//                        localObject3 = new DBManager(CustomApplcation.getInstance().getApplicationContext()).FindOrders((String)localObject1);
//                        localObject1 = new JSONObject();
//                        localObject2 = AbSharedUtil.getString(this.context, "returnurl");
//                        if ((localObject3 != null) && (((List)localObject3).size() > 0))
//                        {
//                            localObject3 = PayHelperUtils.stampToDate(((OrderBean)((List)localObject3).get(0)).getDt());
//                            ((JSONObject)localObject1).put("msg", "支付成功");
//                            ((JSONObject)localObject1).put("paytime", localObject3);
//                            if (!TextUtils.isEmpty((CharSequence)localObject2)) {
//                                ((JSONObject)localObject1).put("returnurl", localObject2);
//                            }
//                            return responseJson(paramIHTTPSession, ((JSONObject)localObject1).toString());
//                        }
                        ((JSONObject)localObject1).put("msg", "未支付");
                        return responseJson(paramIHTTPSession, ((JSONObject)localObject1).toString());
                    }
                    if ("/test".equals(paramIHTTPSession.getUri()))
                    {
                        localObject1 = (String)paramIHTTPSession.getParms().get("oid");
//                        localObject2 = new DBManager(CustomApplcation.getInstance().getApplicationContext());
//                        new ArrayList();
//                        localObject3 = new JSONObject();
//                        if ((localObject1 != null) && (!((String)localObject1).equals("")))
//                        {
//                            localObject2 = ((DBManager)localObject2).FindQrCodes_order((String)localObject1);
//                            localObject1 = localObject2;
//                            if (((List)localObject2).size() > 0)
//                            {
//                                localObject1 = new StringBuilder();
//                                ((StringBuilder)localObject1).append("");
//                                ((StringBuilder)localObject1).append(((QrCodeBean)((List)localObject2).get(0)).getMark());
//                                ((JSONObject)localObject3).put("order", ((StringBuilder)localObject1).toString());
//                                localObject1 = new StringBuilder();
//                                ((StringBuilder)localObject1).append("");
//                                ((StringBuilder)localObject1).append(((QrCodeBean)((List)localObject2).get(0)).getPayurl());
//                                ((JSONObject)localObject3).put("payurl", ((StringBuilder)localObject1).toString());
//                                localObject1 = localObject2;
//                            }
//                        }
//                        else
//                        {
//                            i = 0;
//                            localObject2 = ((DBManager)localObject2).FindQrCodesAll();
//                            localObject1 = localObject2;
//                            if (((List)localObject2).size() > 0)
//                            {
//                                localObject1 = "";
//                                if (i < ((List)localObject2).size())
//                                {
//                                    localObject4 = (QrCodeBean)((List)localObject2).get(i);
//                                    localObject5 = new StringBuilder();
//                                    ((StringBuilder)localObject5).append((String)localObject1);
//                                    ((StringBuilder)localObject5).append(", ");
//                                    localObject1 = ((StringBuilder)localObject5).toString();
//                                    localObject5 = new StringBuilder();
//                                    ((StringBuilder)localObject5).append((String)localObject1);
//                                    ((StringBuilder)localObject5).append("mark=");
//                                    ((StringBuilder)localObject5).append(((QrCodeBean)localObject4).getMark());
//                                    ((StringBuilder)localObject5).append("&money=");
//                                    ((StringBuilder)localObject5).append(((QrCodeBean)localObject4).getMoney());
//                                    ((StringBuilder)localObject5).append("&payurl=");
//                                    ((StringBuilder)localObject5).append(((QrCodeBean)localObject4).getPayurl());
//                                    ((StringBuilder)localObject5).append("&type=");
//                                    ((StringBuilder)localObject5).append(((QrCodeBean)localObject4).getType());
//                                    ((StringBuilder)localObject5).append("&dt=");
//                                    ((StringBuilder)localObject5).append(((QrCodeBean)localObject4).getDt());
//                                    localObject1 = ((StringBuilder)localObject5).toString();
//                                    i += 1;
//                                    continue;
//                                }
//                                ((JSONObject)localObject3).put("data", localObject1);
//                                localObject1 = localObject2;
//                            }
                        }
                        localObject2 = new StringBuilder();
                        ((StringBuilder)localObject2).append("");
                        ((StringBuilder)localObject2).append(((List)localObject1).size());
                        ((JSONObject)localObject3).put("qrCodeBeans", ((StringBuilder)localObject2).toString());
                        return responseJson(paramIHTTPSession, ((JSONObject)localObject3).toString());
                    }
//                    localObject1 = response404(paramIHTTPSession, paramIHTTPSession.getUri());
                    return (NanoHTTPD.Response)localObject1;
                } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
