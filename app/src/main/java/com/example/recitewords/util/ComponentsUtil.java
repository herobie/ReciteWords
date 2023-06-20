package com.example.recitewords.util;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ComponentsUtil {
    /**
     * 创建TextInputLayout
     */
    public static TextInputLayout createNewTextInputLayoutWithEditText(Context context, int hint, int componentId){
        TextInputLayout textInputLayout = new TextInputLayout(context);
        //设置layout参数
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(50, 17, 50, 10);
        //将参数应用至layout
        textInputLayout.setLayoutParams(layoutParams);
        textInputLayout.addView(createNewTextInputEditText(context, hint, componentId));
        return textInputLayout;
    }

    public static TextInputLayout createNewTextInputLayoutWithEditText(Context context, String hint, int componentId){
        TextInputLayout textInputLayout = new TextInputLayout(context);
        //设置layout参数
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(50, 17, 50, 10);
        //将参数应用至layout
        textInputLayout.setLayoutParams(layoutParams);
        textInputLayout.addView(createNewTextInputEditText(context, hint, componentId));
        return textInputLayout;
    }

    public static TextInputEditText createNewTextInputEditText(Context context, int hint, int componentId){
        TextInputEditText textInputEditText = new TextInputEditText(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
        textInputEditText.setLayoutParams(layoutParams);
        textInputEditText.setId(componentId);
        textInputEditText.setHint(hint);
        return textInputEditText;
    }

    public static TextInputEditText createNewTextInputEditText(Context context, String hint, int componentId){
        TextInputEditText textInputEditText = new TextInputEditText(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textInputEditText.setLayoutParams(layoutParams);
        textInputEditText.setId(componentId);
        textInputEditText.setText(hint);
        return textInputEditText;
    }

    /**
     * 移除layout下添加的所有控件
     */
    public static void removeComponent(LinearLayout parent){
        parent.removeAllViews();
    }
}
