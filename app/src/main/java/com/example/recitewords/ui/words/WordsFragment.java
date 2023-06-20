package com.example.recitewords.ui.words;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recitewords.Constant;
import com.example.recitewords.MainViewModel;
import com.example.recitewords.R;
import com.example.recitewords.model.Word;
import com.example.recitewords.databinding.FragmentWordsBinding;

import java.util.List;

public class WordsFragment extends Fragment {
    private MainViewModel viewModel;
    private FragmentWordsBinding binding;
    public static WordsFragment INSTANCE;
    public static WordsFragment getInstance(){
        if (INSTANCE == null){
            INSTANCE = new WordsFragment();
        }
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_words, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBroadcast();
        initView();
        initListener();
    }

    private void initView(){
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.wordsRv.setLayoutManager(layoutManager);
        viewModel.getRepository().getAllData();
    }

    private void initListener(){
        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getInsertDialog(getContext()).show();
            }
        });

        binding.wordsRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.canScrollVertically(1)){//能够向上滚动（rv选项下滑)时，返回true
                    binding.fabToBottom.setVisibility(View.VISIBLE);
                }else {
                    binding.fabToBottom.setVisibility(View.GONE);
                }
                if (recyclerView.canScrollVertically(-1)){//能够向下滚动（rv选项上滑)时，返回true
                    binding.fabToTop.setVisibility(View.VISIBLE);
                }else {
                    binding.fabToTop.setVisibility(View.GONE);
                }
            }
        });

        binding.fabToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.wordsRv.smoothScrollToPosition(0);
            }
        });

        binding.fabToBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.wordsRv.smoothScrollToPosition(viewModel.getWordsAdapter(getContext()).getItemCount());
            }
        });
    }

    private void initObserver(){
        viewModel.getWordLiveData().observe(this, new Observer<List<Word>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Word> words) {
                //更新数据源，刷新视图
                Log.d(Constant.TAG, "DataChanged");
                viewModel.setWords(words);
                viewModel.getWordsAdapter(getContext()).setWords(words);
                //如果没有绑定适配器，则绑定，否则刷新数据
                if (binding.wordsRv.getAdapter() == null){
                    binding.wordsRv.setAdapter(viewModel.getWordsAdapter(getContext()));
                }else {
                    viewModel.getWordsAdapter(getContext()).notifyDataSetChanged();
                }
                Constant.isDataChanged = true;
            }
        });
    }

    private void initBroadcast(){
        DataReadyReceiver dataReadyReceiver = new DataReadyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("dataReady");
        getContext().registerReceiver(dataReadyReceiver, intentFilter);
    }

    private class DataReadyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //数据就绪后刷新视图
            initObserver();
        }
    }
}