package com.example.recitewords.ui.review;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recitewords.Constant;
import com.example.recitewords.MainViewModel;
import com.example.recitewords.R;
import com.example.recitewords.model.Word;
import com.example.recitewords.ui.adapters.WordsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ReviewFragment extends Fragment {
    private SwipeRefreshLayout review_refresh;
    private RecyclerView review_rv;
    private WordsAdapter reviewAdapter;
    private MainViewModel viewModel;
    public static ReviewFragment INSTANCE;
    public static ReviewFragment getInstance(){
        if (INSTANCE == null){
            INSTANCE = new ReviewFragment();
        }
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
        initBroadcast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView(){
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        review_refresh = getView().findViewById(R.id.review_refresh);
        review_rv = getView().findViewById(R.id.review_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        review_rv.setLayoutManager(layoutManager);
        reviewAdapter = new WordsAdapter(getContext(), viewModel);
        refresh();
        review_refresh.setOnRefreshListener(this::refresh);
    }

    private void initListener(){
        FloatingActionButton fab_refresh = getView().findViewById(R.id.fab_refresh);
        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!review_refresh.isRefreshing()){
                    review_refresh.setRefreshing(true);
                    refresh();
                }
            }
        });
    }

    private void initObserver(){
        viewModel.getReviewData().observe(this, new Observer<List<Word>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Word> words) {
                Log.d(Constant.TAG, "OnChanged");
                reviewAdapter.setWords(words);
                viewModel.setReviewWords(words);
                if (review_rv.getAdapter() == null){
                    review_rv.setAdapter(reviewAdapter);
                }else {
                    reviewAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initBroadcast(){
        DataReadyReceiver dataReadyReceiver = new DataReadyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("dataReady");
        getContext().registerReceiver(dataReadyReceiver, intentFilter);
    }

    private void refresh(){
        viewModel.getRepository().queryRandomWords(viewModel.getConstraints()[1], Constant.reviewLimit);
    }

    private class DataReadyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //数据就绪后刷新视图
            initObserver();
            if (review_refresh.isRefreshing()){
                review_refresh.setRefreshing(false);
            }
        }
    }
}