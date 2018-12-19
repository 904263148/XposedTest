package com.dktlh.ktl.xposedtest.tools;

import com.alibaba.fastjson.JSON;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class AnyMethodHook extends XC_MethodHook {
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        String cn=param.method.getDeclaringClass().getName();
        String scn=param.method.getDeclaringClass().getSimpleName();
        String methodName=param.method.getName();
        //XposedBridge.log("AnyMethodHook className:"+cn);
        XposedBridge.log("AnyMethodHook methodName:"+methodName);
        if(param.args!=null)
        {
            for (int a=0;a<param.args.length;a++)
            {
                if(param.args[a]==null)
                {
                    XposedBridge.log("AnyMethodHook arg"+a+"=null");
                    continue;
                }
                XposedBridge.log("AnyMethodHook arg"+a+":cn="+param.args[a].getClass().getName());
                try {
                    XposedBridge.log(JSON.toJSONString(param.args[a]));

                }catch (Exception e)
                {

                }

            }
        }


        if(param.getResult()!=null)
        {
            XposedBridge.log("AnyMethodHook result:cn="+param.getResult().getClass().getName());
            try {
                XposedBridge.log(JSON.toJSONString(param.getResult()));
            }catch (Exception e)
            {

            }

        }

//        if(cn.equals("com.alipay.mobile.phonecashier.service.PhoneCashierServiceImpl"))
//        {
//            throw new RuntimeException();
//        }
    }
}
