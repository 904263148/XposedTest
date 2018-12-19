package com.dktlh.ktl.xposedtest.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dktlh.ktl.xposedtest.R;
import com.dktlh.ktl.xposedtest.model.TaskBean;
import com.dktlh.ktl.xposedtest.utils.DateUtils;

import java.util.List;

public class AllTaskAdapter extends BaseQuickAdapter<TaskBean, BaseViewHolder> {
    public AllTaskAdapter(int layoutResId, @Nullable List<TaskBean> data) {
        super(layoutResId, data);
    }

    //接收到任务[未处理],客户端生成二维码成功[已生成],推送二维码到服务器成功[已推送],失败[推送失败],支付成功并推送成功[通知成功],支付成功推送失败[通知失败],
    @Override
    protected void convert(BaseViewHolder helper, TaskBean item) {
        String state = "";
        int color = ContextCompat.getColor(mContext, R.color.red);
        switch (item.getState()) {
            case 0:
                state = "未处理";
                color = ContextCompat.getColor(mContext, R.color.red);
                break;
            case 1:
                state = "已生成";
                color = ContextCompat.getColor(mContext, R.color.orange);
                break;
            case 2:
                state = "已推送";
                color = ContextCompat.getColor(mContext, R.color.green);
                break;
        }
        helper.setText(R.id.mTypeTv, "支付类型：" + item.getType())
                .setText(R.id.mTimeTv, DateUtils.transForString(item.getTime()))
                .setText(R.id.mQrCodeLinkTv, "二维码链接：" + item.getUrl())
                .setText(R.id.mMarkTv, "备注：" + item.getMark())
                .setText(R.id.mPriceTv, "金额：" + item.getPrice() + "元")
                .setText(R.id.mStateTv, state);
        helper.setTextColor(R.id.mStateTv, color);
        helper.setGone(R.id.mQrCodeLinkTv, !item.getUrl().equals(""));
    }
}
