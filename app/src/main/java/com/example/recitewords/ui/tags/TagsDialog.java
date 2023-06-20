package com.example.recitewords.ui.tags;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.recitewords.MainViewModel;
import com.example.recitewords.R;

import com.example.recitewords.databinding.DialogTagsBinding;

public class TagsDialog extends Dialog {
    private DialogTagsBinding binding;
    private MainViewModel viewModel;
    public TagsDialog(@NonNull Context context, MainViewModel viewModel) {
        super(context, R.style.DialogBaseStyle);
        this.viewModel = viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogTagsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        binding.tagRv.addItemDecoration(new DividerItemDecoration(getContext().getApplicationContext() , DividerItemDecoration.VERTICAL));
        binding.tagRv.setLayoutManager(layoutManager);
        binding.tagRv.setAdapter(viewModel.getTagsAdapter(getContext()));

        binding.tagCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
