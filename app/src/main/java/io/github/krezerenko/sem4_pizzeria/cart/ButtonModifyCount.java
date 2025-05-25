package io.github.krezerenko.sem4_pizzeria.cart;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.Nullable;

import io.github.krezerenko.sem4_pizzeria.R;

public class ButtonModifyCount extends LinearLayout
{
    private int count;
    private final TextView textCount;
    private final ViewSwitcher switcher;

    private OnCountChangedListener onIncreaseListener;
    private OnCountChangedListener onDecreaseListener;
    private OnCountChangedListener onCountChangedListener;

    public ButtonModifyCount(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.button_modify_count, this, true);
        switcher = (ViewSwitcher) getChildAt(0);
        textCount = findViewById(R.id.text_button_modify_count);
        LinearLayout buttonSelected = findViewById(R.id.button_button_modify_selected);
        TextView textPlus = findViewById(R.id.text_button_modify_plus);
        TextView textMinus = findViewById(R.id.text_button_modify_minus);
        Button buttonNotSelected = findViewById(R.id.button_button_modify_not_selected);

        float padding;
        float insets;
        try (TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ButtonModifyCount))
        {
            count = ta.getInt(R.styleable.ButtonModifyCount_count, 0);
            padding = ta.getDimension(R.styleable.ButtonModifyCount_paddingHorizontal, dp(20));
            insets = ta.getDimension(R.styleable.ButtonModifyCount_insets, dp(8));
        }
        buttonNotSelected.post(() ->
        {
            int width = buttonNotSelected.getMeasuredWidth();
            int height = buttonNotSelected.getMeasuredHeight() - Math.round(insets) * 2;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
            params.setMargins(0, Math.round(insets), 0, Math.round(insets));
            buttonSelected.setLayoutParams(params);
        });
        textPlus.setPadding((int) padding, 0, 0, 0);
        textPlus.setOnClickListener(v -> increment());
        textMinus.setPadding(0, 0, (int) padding, 0);
        textMinus.setOnClickListener(v -> decrement());
        buttonNotSelected.setOnClickListener(v -> increment());
        textCount.setText(String.valueOf(count));
    }

    public int getCount()
    {
        return count;
    }

    public void setCountNoCallbacks(int count)
    {
        textCount.setText(String.valueOf(count));
        if ((this.count == 0) ^ (count == 0)) switcher.showNext();
        this.count = count;
    }

    public void setCount(int count)
    {
        if (this.count != count && onCountChangedListener != null) onCountChangedListener.onCountChanged(this.count, count);
        if (count > this.count && onIncreaseListener != null) onIncreaseListener.onCountChanged(this.count, count);
        if (count < this.count && onDecreaseListener != null) onDecreaseListener.onCountChanged(this.count, count);
        setCountNoCallbacks(count);
    }

    public void increment()
    {
        setCount(count + 1);
    }

    public void decrement()
    {
        if (count == 0) return;
        setCount(count - 1);
    }

    public void reset()
    {
        setCount(0);
    }

    public void setOnIncreaseListener(OnCountChangedListener l)
    {
        onIncreaseListener = l;
    }

    public void setOnDecreaseListener(OnCountChangedListener l)
    {
        onDecreaseListener = l;
    }

    public void setOnCountChangedListener(OnCountChangedListener l)
    {
        this.onCountChangedListener = l;
    }

    private float dp(float dp)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public interface OnCountChangedListener
    {
        void onCountChanged(int oldCount, int newCount);
    }
}
