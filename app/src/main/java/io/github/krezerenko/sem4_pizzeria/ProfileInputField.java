package io.github.krezerenko.sem4_pizzeria;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ProfileInputField extends LinearLayout
{
    private final TextView textTitle;
    private final EditText editFieldValue;

    public ProfileInputField(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.profile_input_field, this, true);
        textTitle = findViewById(R.id.text_title);
        editFieldValue = findViewById(R.id.edit_field_value);

        String title;
        String hint;
        int inputType;
        try (TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProfileInputField))
        {
            title = ta.getString(R.styleable.ProfileInputField_android_title);
            hint = ta.getString(R.styleable.ProfileInputField_android_hint);
            inputType = ta.getInt(R.styleable.ProfileInputField_android_inputType, InputType.TYPE_CLASS_TEXT);
        }

        textTitle.setText(title);
        editFieldValue.setHint(hint);
        editFieldValue.setInputType(inputType);
    }

    public TextView getTextTitle()
    {
        return textTitle;
    }

    public EditText getEditFieldValue()
    {
        return editFieldValue;
    }
}
