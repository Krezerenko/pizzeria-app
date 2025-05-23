package io.github.krezerenko.sem4_pizzeria;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ProfileSetting extends LinearLayout
{
    private final TextView textName;

    public ProfileSetting(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.profile_setting, this, true);
        textName = findViewById(R.id.text_name);

        String name;
        try (TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProfileSetting))
        {
            name = ta.getString(R.styleable.ProfileSetting_android_name);
        }

        textName.setText(name);
    }

    public TextView getTextName()
    {
        return textName;
    }
}
