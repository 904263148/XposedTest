package com.dktlh.ktl.xposedtest;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.PluralsRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dktlh.ktl.xposedtest.model.TaskBean;
import com.dktlh.ktl.xposedtest.ui.adapter.AllTaskAdapter;
import com.dktlh.ktl.xposedtest.websocket.WsManager;
import com.dktlh.ktl.xposedtest.websocket.common.ICallback;
import com.dktlh.ktl.xposedtest.websocket.request.Action;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedBridge;

public class QrCodeFragment extends Fragment {
    private View view;
    private static final String KEY = "title";
    private RecyclerView mRecyclerView;
    private AllTaskAdapter allTaskAdapter;
    private List<TaskBean> mData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_qr_code, container,false);
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(savedInstanceState);
    }

    private void initData(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mData = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        allTaskAdapter = new AllTaskAdapter(R.layout.item_task, mData);
        mRecyclerView.setAdapter(allTaskAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TaskBean taskBean) {
        if (taskBean.getState() == 1) {
            mData.add(0, taskBean);
            allTaskAdapter.setNewData(mData);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * fragment静态传值
     * */
    public static QrCodeFragment newInstance(String str){
        QrCodeFragment fragment = new QrCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY,str);
        fragment.setArguments(bundle);
        return fragment;
    }
}
