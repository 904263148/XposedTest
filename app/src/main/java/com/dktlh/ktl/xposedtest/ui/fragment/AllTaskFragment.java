package com.dktlh.ktl.xposedtest.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dktlh.ktl.xposedtest.Main2Activity;
import com.dktlh.ktl.xposedtest.R;
import com.dktlh.ktl.xposedtest.model.TaskBean;
import com.dktlh.ktl.xposedtest.ui.adapter.AllTaskAdapter;
import com.dktlh.ktl.xposedtest.utils.BitmapUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class AllTaskFragment extends Fragment {
    private View view;
    private static final String KEY = "title";
    private RecyclerView mRecyclerView;
    private AllTaskAdapter allTaskAdapter;
    private List<TaskBean> mData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_task, container,false);
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        EventBus.getDefault().register(this);
        mData = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        allTaskAdapter = new AllTaskAdapter(R.layout.item_task, mData);
        mRecyclerView.setAdapter(allTaskAdapter);
    }

    private boolean isExist = false;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //执行接收Message后的逻辑
                    isExist = false;
                    TaskBean taskBean = (TaskBean) msg.obj;
                    if (mData.isEmpty()) {
                        mData.add(0, taskBean);
                        allTaskAdapter.setNewData(mData);
                    } else {
                        for (int i = 0; i < mData.size(); i++) {
                            if (mData.get(i).getMark().equals(taskBean.getMark())) {
                                mData.set(i, taskBean);
                                allTaskAdapter.setNewData(mData);
                                isExist = true;
                            }
                        }
                        if (!isExist) {
                            mData.add(0, taskBean);
                            allTaskAdapter.setNewData(mData);
                        }
                    }
                    break;
            }
            return true;
        }
    });

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final TaskBean taskBean) {
        Message message=new Message();
        message.what=1;
        message.obj=taskBean;
        handler.sendMessage(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * fragment静态传值
     * */
    public static AllTaskFragment newInstance(String str){
        AllTaskFragment fragment = new AllTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY,str);
        fragment.setArguments(bundle);
        return fragment;
    }
}
