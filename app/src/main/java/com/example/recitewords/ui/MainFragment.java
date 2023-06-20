package com.example.recitewords.ui;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recitewords.Constant;
import com.example.recitewords.MainViewModel;
import com.example.recitewords.R;
import com.example.recitewords.adapters.FragmentsAdapter;
import com.example.recitewords.databinding.FragmentMainBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private MainViewModel viewModel;
    private FragmentMainBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_main, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
        initObserver();
    }

    private void initView(){
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        binding.setTagName(viewModel.getConstraints()[1]);
        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabMain, binding.viewPager, (tab, position) -> {
            if (position == 0){
                tab.setText("收藏单词");
            }else if (position == 1){
                tab.setText("复习列表");
            }
        });
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(viewModel.getWordsFragment());
        fragments.add(viewModel.getReviewFragment());
        viewModel.setFragmentsAdapter(new FragmentsAdapter(this, fragments));
        binding.viewPager.setAdapter(viewModel.getFragmentsAdapter());
        if (!mediator.isAttached()){
            mediator.attach();
        }

    }

    private void initListener(){
        viewModel.getTagsDialog(getContext()).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                binding.tagMain.setText(viewModel.getConstraints()[1]);
            }
        });

        binding.tagMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getTagsDialog(getContext()).show();
            }
        });

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0){
                    Constant.currentPage = "Word";
                }else {
                    Constant.currentPage = "Review";
                }
            }
        });
    }

    private void initObserver(){
        viewModel.getIsTagsReady().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                //获取全部tag信息，获取成功后显示dialog
                if (aBoolean){
                    viewModel.getTagsLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
                        @Override
                        public void onChanged(List<String> strings) {
                            strings.add(0, "全部");
                            viewModel.getTagsAdapter(getContext()).setTags(strings);
                            viewModel.setTags(strings);
                        }
                    });
                }
            }
        });
    }
}