package com.example.recitewords.util;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.recitewords.Constant;
import com.google.android.material.textfield.TextInputEditText;

public class StringUtil {

    /**
     * 获取多个输入框输入的字符并将其转化为一个字符串
     * @param strings 多个输入框输入数据
     */
    public static String formingString(String[] strings){
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings){
            if (string != null && !string.isEmpty()){
                stringBuilder.append(string).append("\n");
            }
        }
        Log.d(Constant.TAG, stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * 分割获取的字符串
     */
    public static String splitString(String initialString){
        StringBuilder stringBuilder = new StringBuilder();
        String[] strings = initialString.split("\n");
        for (String string : strings){
            stringBuilder.append(string).append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 分割获取的字符串，自动换行及标注顺序
     */
    public static String splitStringWithNumber(String initialString){
        String s = "";
        if (!initialString.isEmpty()){
            StringBuilder stringBuilder = new StringBuilder();
            String[] strings = initialString.split("\n");
            int index = 1;
            for (String string : strings){
                stringBuilder.append(index).append("、")
                        .append(string).append("\n\n");
                index++;
            }
            s = stringBuilder.toString();
        }
        return s;
    }

    /**
     * 将字符串分割
     * @return 返回数组格式
     */
    public static String[] splitStringToArray(String initialString){
        return initialString.split("\n");
    }

    /**
     * 获取添加的输入框的值
     * @param parentEditText 同类型的主输入框的值
     */
    public static String getMultiInput(LinearLayout parent, TextInputEditText parentEditText, int componentId){
        String[] strings = new String[parent.getChildCount() + 1];
        strings[0] = parentEditText.getText().toString();
        for (int i = 0 ; i < parent.getChildCount(); i++){
            View view = parent.getChildAt(i);
            TextInputEditText textInputEditText = view.findViewById(componentId);
            if (!textInputEditText.getText().toString().isEmpty()){
                strings[i + 1] = textInputEditText.getText().toString();
            }
        }
        return StringUtil.formingString(strings);
    }
}
