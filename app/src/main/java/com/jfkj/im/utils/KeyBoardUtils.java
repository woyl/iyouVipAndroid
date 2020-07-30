package com.jfkj.im.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jfkj.im.App;


/**
 * 软键盘工具类
 * <p>
 * Created by tanbiheng on 2017/9/19.
 */

public class KeyBoardUtils {
    /**
     * 打开软键盘
     *
     * @param mEditText 输入框
     */
    public static void showKeybord(EditText mEditText) {
        if (mEditText == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) App.getAppContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
        mEditText.setSelection(mEditText.getText().length());
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     */
    public static void hideKeybord(EditText mEditText) {
        if (mEditText == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) App.getAppContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * @param @param editText
     * @param @param action<br/>
     *               EditorInfo.IME_ACTION_SEARCH 搜索 <br/>
     *               EditorInfo.IME_ACTION_SEND 发送<br/>
     *               EditorInfo.IME_ACTION_NEXT 下一步<br/>
     *               EditorInfo.IME_ACTION_DONE 完成<br/>
     *               EditorInfo.IME_ACTION_NEXT 去往<br/>
     * @return void
     * @Desp: 设置输入法中回车按钮的显示内容
     */
    public static void setImeOptions(EditText editText, int action) {
        if (editText == null) {
            return;
        }
        editText.setImeOptions(action);
    }

    /**
     * 输入框获取焦点
     *
     * @param mEditText
     */
    public static void requestFocus(final EditText mEditText) {
        if (mEditText != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mEditText.requestFocus();
                    mEditText.setFocusable(true);
                    mEditText.setFocusableInTouchMode(true);
                }
            }, 50);
        }
    }

    /**
     * 输入框获取焦点弹出输入框
     *
     * @param mEditText
     */
    public static void requestShowKeyBord(final EditText mEditText) {
        if (mEditText != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mEditText.requestFocus();
                    mEditText.setFocusable(true);
                    mEditText.setFocusableInTouchMode(true);

                    showKeybord(mEditText);
                }
            }, 50);
        }
    }


    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
//        editText.setFocusable(true);
//        editText.setFocusableInTouchMode(true);
//        editText.requestFocus();
        //显示软键盘
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //如果上面的代码没有弹出软键盘 可以使用下面另一种方式
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, 0);
        }
    }

}
