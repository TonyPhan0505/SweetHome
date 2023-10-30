package com.example.sweethome;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatEditText;

public class CustomAddTagsField extends AppCompatEditText {
    private LinearLayout tags_container;

    public CustomAddTagsField(Context context) {
        super(context);
        init();
    }

    public CustomAddTagsField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomAddTagsField(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        tags_container = new LinearLayout(getContext());
        tags_container.setOrientation(LinearLayout.HORIZONTAL);
        tags_container.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tags_container.setPadding(0, 16, 0, 0);
        this.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == 62 || keyCode == KeyEvent.KEYCODE_ENTER) {
                addTag(getText().toString().trim());
                setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                return true;
            }
            return false;
        });
        this.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addTag(getText().toString().trim());
                    setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                    Context context = textView.getContext();
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
            }
        });
    }

    private void addTag(String tagName) {
        if (!tagName.isEmpty()) {
            setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            View tag_item = LayoutInflater.from(getContext()).inflate(R.layout.tag_item, null);
            TextView tag_name_field = tag_item.findViewById(R.id.tag_name);
            ImageView remove_tag_button = tag_item.findViewById(R.id.remove_tag_button);
            tag_name_field.setText(tagName);
            tags_container = ((Activity) getContext()).findViewById(R.id.tags_container);
            if (tags_container != null) {
                tags_container.addView(tag_item);
            }
            remove_tag_button.setOnClickListener(v -> {
                tags_container.removeView(tag_item);
            });
            setText(null);
        }
    }
}