package com.example.recitewords.ui.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recitewords.Constant;
import com.example.recitewords.MainViewModel;
import com.example.recitewords.R;
import com.example.recitewords.model.Word;
import com.example.recitewords.databinding.ItemWordBinding;
import com.example.recitewords.util.AttachPopUpWindow;
import com.example.recitewords.util.PopUpWindowListeners;

import java.util.List;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder>{
    private List<Word> words;
    private MainViewModel viewModel;
    private ItemWordBinding binding;
    private Context context;
    private final String[] popupData = {"更新数据", "删除"};
    public WordsAdapter(Context context, MainViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public List<Word> getWords() {
        return words;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()) , R.layout.item_word , parent , false);
        ViewHolder holder = new ViewHolder(binding.getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        binding = DataBindingUtil.getBinding(holder.itemView);
        binding.setWord(words.get(position));
        binding.itemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.currentPage.equals("Word")){
                    viewModel.getIsInDetailedPage().setValue(true);
                    Intent intent = new Intent("changePage");
                    intent.putExtra("page", position);
                    context.sendBroadcast(intent);
                }
            }
        });

        binding.itemCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ListPopupWindow listPopupWindow = AttachPopUpWindow.showListPopUpWindow(v, context, popupData, 400, ListPopupWindow.WRAP_CONTENT,  new PopUpWindowListeners() {
                    @Override
                    public AdapterView.OnItemClickListener setItemClickListener(View v, ListPopupWindow popupWindow, String[] popupData) {
                        return new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int mPosition, long id) {
                                switch (mPosition){
                                    case 0:
                                        for (Word mWord : viewModel.getWords()){
                                            //获取指定位置单词的id传入更新dialog中
                                            if (words.get(position).getId() == mWord.getId()){//注意这里的position是哪个，不要和上面onItemClick的搞错了
                                                viewModel.getUpdateDialog(context).show();
                                                viewModel.getUpdateDialog(context).setWord(mWord);
                                                break;
                                            }
                                        }
                                        break;
                                    case 1:
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                        alertDialog.setTitle("警告");
                                        alertDialog.setMessage("确认删除?");
                                        alertDialog.setCancelable(true);

                                        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });

                                        alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @SuppressLint("NotifyDataSetChanged")
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                popupWindow.dismiss();
                                                Constant.removedWordId = words.get(position).getId();
                                                viewModel.getRepository().removeWord(words.get(position).getId(), words.get(position).getName());
                                                Toast.makeText(context, "移除成功!", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        });
                                        alertDialog.show();
                                        break;
                                }
                            }
                        };
                    }

                    @Override
                    public PopupWindow.OnDismissListener setDismissListener(View v, ListPopupWindow popupWindow, String[] popupData) {
                        return null;
                    }
                });
                listPopupWindow.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (words != null){
            size = words.size();
        }
        return size;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
