package com.example.recitewords.util;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;

public interface PopUpWindowListeners {
    AdapterView.OnItemClickListener setItemClickListener(View v, ListPopupWindow popupWindow, String[] popupData);

    PopupWindow.OnDismissListener setDismissListener(View v, ListPopupWindow popupWindow, String[] popupData);
}
