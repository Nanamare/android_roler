package com.buttering.roler.helper;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 헬퍼 : 키보드
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 5. 15.
 */
public class KeyboardHelper {

    /**
     * 올리기
     * @param act
     */
    public static void show(Activity act){
        View view = act.getCurrentFocus();
        if (view == null) view = new View(act);
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

    /**
     * 내리기
     * @param act
     */
    public static void hide(Activity act){
        View view = act.getCurrentFocus();
        if (view == null) view = new View(act);
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void show(EditText et) {
        InputMethodManager imm = (InputMethodManager) et.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, 0);
    }

    public static void hide(EditText et) {
        InputMethodManager imm = (InputMethodManager) et.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    public static void show(SearchView sv) {
        InputMethodManager imm = (InputMethodManager) sv.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(sv, 0);
    }

    public static void hide(SearchView sv) {
        InputMethodManager imm = (InputMethodManager) sv.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(sv.getWindowToken(), 0);
    }

    public static void show(Dialog dialog) {
        if(dialog != null) dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
