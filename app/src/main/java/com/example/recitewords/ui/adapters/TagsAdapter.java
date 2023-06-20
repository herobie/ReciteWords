package com.example.recitewords.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recitewords.Constant;
import com.example.recitewords.MainViewModel;
import com.example.recitewords.R;
import com.example.recitewords.databinding.ItemTagBinding;

import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {
    private MainViewModel viewModel;
    private Context context;
    private ItemTagBinding binding;
    private List<String> tags;

    public TagsAdapter(MainViewModel viewModel, Context context) {
        this.viewModel = viewModel;
        this.context = context;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()) , R.layout.item_tag , parent , false);
        ViewHolder holder = new ViewHolder(binding.getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        binding = DataBindingUtil.getBinding(holder.itemView);
        binding.setTag(tags.get(position));

        binding.tagView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getConstraints()[1] = tags.get(holder.getAdapterPosition());
                if (viewModel.getConstraints()[0] != null){
                    viewModel.getRepository().queryWord(viewModel.getConstraints()[0], viewModel.getConstraints()[1]);
                }else {
                    if (viewModel.getConstraints()[1].equals("全部")){
                        viewModel.getRepository().getAllData();
                    }else {
                        viewModel.getRepository().queryByTag(viewModel.getConstraints()[1]);
                    }
                }
                viewModel.getRepository().queryRandomWords(viewModel.getConstraints()[1], Constant.reviewLimit);
                if (viewModel.getTagsDialog(context).isShowing()){
                    viewModel.getTagsDialog(context).dismiss();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tags == null? 0: tags.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
