package com.example.recitewords.ui.detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recitewords.Constant;
import com.example.recitewords.MainViewModel;
import com.example.recitewords.R;
import com.example.recitewords.databinding.FragmentDetailBinding;

import java.util.Random;

public class DetailFragment extends Fragment {
    private int position;
    private FragmentDetailBinding binding;
    private MainViewModel viewModel;
    public DetailFragment( int position) {
        this.position = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Constant.TAG, "DetailedFragmentViewCreated");
        initView();
        initListener();
    }

    private void initView(){
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        binding.setWord(viewModel.getWords().get(position));
        binding.detailBackground.setBackgroundResource(viewModel.getColors()[new Random().nextInt(10)]);
    }

    private void initListener(){
        binding.detailBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getIsInDetailedPage().setValue(false);
            }
        });

        binding.detailBackground.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                viewModel.getUpdateDialog(getContext()).show();
                viewModel.getUpdateDialog(getContext())
                        .setWord(viewModel.getWords().get(position));
                return true;
            }
        });
    }
}