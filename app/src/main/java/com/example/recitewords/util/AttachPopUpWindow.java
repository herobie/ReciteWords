package com.example.recitewords.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;

import androidx.core.content.ContextCompat;

import com.example.recitewords.Constant;
import com.example.recitewords.R;
import com.google.android.material.textfield.TextInputEditText;

public class AttachPopUpWindow {

    /**
     * 添加点击弹出的window
     */
    @SuppressLint("ClickableViewAccessibility")
    public static void attachClickedWindow(View view, Context context, String[] data, PopUpWindowListeners popUpWindowListeners){
        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(Constant.TAG, "Clicked");
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (view instanceof TextInputEditText){
                        TextInputEditText textInputEditText = (TextInputEditText) view;//转型成TextInputEditText
                        if (event.getX() >= (view.getWidth() - textInputEditText
                                .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() * 3)) {
                            ListPopupWindow listPopupWindow = showListPopUpWindow(textInputEditText, context, data, 400, 500,  popUpWindowListeners);
                            listPopupWindow.setHorizontalOffset(525);
                            listPopupWindow.show();
                            if (listPopupWindow.isShowing()){
                                textInputEditText.setCompoundDrawablesWithIntrinsicBounds(null, null , context.getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_up_24), null);
                            }
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    /**
     * 展示popupWindow
     * @param data 要展示的数据
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public static ListPopupWindow showListPopUpWindow(View view, Context context, String[] data, int width, int height, PopUpWindowListeners popUpWindowListeners) {
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(context);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, data));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(view);//以哪个控件为基准，在该处以editText为基准
        listPopupWindow.setModal(true);
        listPopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.deep_white)));
        listPopupWindow.setWidth(width);
        listPopupWindow.setHeight(height);

        listPopupWindow.setOnItemClickListener(popUpWindowListeners.setItemClickListener(view, listPopupWindow, data));
        listPopupWindow.setOnDismissListener(popUpWindowListeners.setDismissListener(view, listPopupWindow, data));
        return listPopupWindow;
    }
}
