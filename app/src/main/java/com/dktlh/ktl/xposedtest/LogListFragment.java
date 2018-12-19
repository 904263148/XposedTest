package com.dktlh.ktl.xposedtest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.PluralsRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LogListFragment extends Fragment {
    private View view;
    private static final String KEY = "title";
    private TextView mContentTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_log_list, container,false);
        mContentTv = view.findViewById(R.id.mContentTv);
        return view;
    }

    /**
     * fragment静态传值
     * */
    public static LogListFragment newInstance(String str){
        LogListFragment fragment = new LogListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY,str);
        fragment.setArguments(bundle);
        return fragment;
    }
}
