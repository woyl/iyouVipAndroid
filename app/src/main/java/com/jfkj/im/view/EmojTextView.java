package com.jfkj.im.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.adapter.chat.EmojiManager;
import com.jfkj.im.adapter.chat.EmojiconHandler;

public class EmojTextView extends TextView {
    private Context context;
    private View view = null;
    private int mEmojiconSize;
    private int mEmojiconAlignment;
    private int mEmojiconTextSize;
    private boolean mUseSystemDefault = false;
    public EmojTextView(Context context) {
        super(context);
        this.context = context;
        initView(context, null);
    }

    public EmojTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context, attrs);
    }

    public EmojTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context, attrs);
    }

    public EmojTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initView(context, attrs);
    }
    private void initView(Context context, AttributeSet attrs) {
        mEmojiconSize = (int) getTextSize();
        mEmojiconTextSize = (int) getTextSize();
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emojicon);
        mEmojiconSize = (int) a.getDimension(R.styleable.Emojicon_emojiconSize, getTextSize());
        mEmojiconAlignment = a.getInt(R.styleable.Emojicon_emojiconAlignment, DynamicDrawableSpan.ALIGN_BASELINE);
        mUseSystemDefault = a.getBoolean(R.styleable.Emojicon_emojiconUseSystemDefault, false);
        a.recycle();
        mEmojiconTextSize = (int) getTextSize();
        setText(getText());

    }

}
