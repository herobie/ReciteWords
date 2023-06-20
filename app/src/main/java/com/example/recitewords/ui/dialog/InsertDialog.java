package com.example.recitewords.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.recitewords.MainViewModel;
import com.example.recitewords.R;
import com.example.recitewords.model.Word;
import com.example.recitewords.databinding.DialogNewWordBinding;
import com.example.recitewords.util.PopUpWindowListeners;
import com.example.recitewords.util.StringUtil;
import com.example.recitewords.util.ComponentsUtil;
import com.example.recitewords.util.AttachPopUpWindow;
import com.google.android.material.textfield.TextInputEditText;

public class InsertDialog extends Dialog implements PopUpWindowListeners {
    private DialogNewWordBinding binding;
    private MainViewModel viewModel;
    public InsertDialog(@NonNull Context context, MainViewModel viewModel) {
        super(context, R.style.DialogBaseStyle);
        this.viewModel = viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogNewWordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initListener();
    }

    private void initView(){
        AttachPopUpWindow.attachClickedWindow(binding.edTags, getContext(), viewModel.getTags().toArray(new String[0]), this);
        AttachPopUpWindow.attachClickedWindow(binding.edTranslate, getContext(),
                new String[]{"v.", "n.", "adj.", "adv.", "prep.", "conj."}, this);
    }

    private void initListener(){
        binding.btnWordCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInput();
                dismiss();
            }
        });

        binding.btnWordConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputCheck()){
                    Word word = new Word();
                    word.setName(binding.edWord.getText().toString());
                    word.setSentence(StringUtil.getMultiInput(binding.sentencesContainer, binding.edExamples, R.id.ed_examples));
                    word.setTranslate(StringUtil.getMultiInput(binding.translateContainer, binding.edTranslate, R.id.ed_translate));
                    word.setTags(binding.edTags.getText().toString());
                    viewModel.getRepository().insertWord(word);
                    clearInput();
                    dismiss();
                    Toast.makeText(getContext(), "操作成功!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnNewSentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加一个例句输入框
                binding.sentencesContainer.addView(ComponentsUtil.createNewTextInputLayoutWithEditText(getContext(), R.string.example_sentence, R.id.ed_examples));
            }
        });

        binding.btnNewTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加一个translate输入框
                binding.translateContainer.addView(ComponentsUtil.createNewTextInputLayoutWithEditText(getContext(), R.string.translate, R.id.ed_translate));
            }
        });
    }

    /**
     * 检查输入内容
     * @return
     */
    private boolean inputCheck(){
        boolean isSatisfied = true;
        if (binding.edTranslate.getText().toString().isEmpty()){
            if (StringUtil.getMultiInput(binding.translateContainer, binding.edTranslate, R.id.ed_translate).isEmpty()){
                isSatisfied = false;
                binding.tvWordWarning.setText("请输入翻译");
            }
        }
        if (binding.edWord.getText().toString().isEmpty()){
            isSatisfied = false;
            binding.tvWordWarning.setText("请输入单词");
        }
        return isSatisfied;
    }

    private void clearInput(){
        //清空输入
        binding.edWord.setText("");
        binding.edTags.setText("");
        binding.edTranslate.setText("");
        binding.edExamples.setText("");
        //清空添加的输入框
        ComponentsUtil.removeComponent(binding.sentencesContainer);
        ComponentsUtil.removeComponent(binding.translateContainer);
    }


    @Override
    public AdapterView.OnItemClickListener setItemClickListener(View v, ListPopupWindow popupWindow, String[] popupData) {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(popupData[position]);
                //把选择的选项内容展示在EditText上
                TextInputEditText textInputEditText = ((TextInputEditText) v);
                textInputEditText.setText(popupData[position]);
                textInputEditText.setSelection(textInputEditText.getText().length());
                popupWindow.dismiss();//如果已经选择了，隐藏起来
            }
        };
    }

    @Override
    public PopupWindow.OnDismissListener setDismissListener(View v, ListPopupWindow popupWindow, String[] popupData) {
        return new PopupWindow.OnDismissListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onDismiss() {
                ((TextInputEditText) v).setCompoundDrawablesWithIntrinsicBounds(null, null , getContext().getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_down_24), null);
            }
        };
    }
}
