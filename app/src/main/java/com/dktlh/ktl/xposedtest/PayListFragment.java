package com.dktlh.ktl.xposedtest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.PluralsRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dktlh.ktl.xposedtest.model.TaskBean;
import com.dktlh.ktl.xposedtest.ui.adapter.AllTaskAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class PayListFragment extends Fragment {
    private View view;
    private static final String KEY = "title";
    private RecyclerView mRecyclerView;
    private AllTaskAdapter allTaskAdapter;
    private List<TaskBean> mData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pay_list, container,false);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TaskBean taskBean) {
        if (taskBean.getState() == 2) {
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
    public static PayListFragment newInstance(String str){
        PayListFragment fragment = new PayListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY,str);
        fragment.setArguments(bundle);
        return fragment;
    }
}
