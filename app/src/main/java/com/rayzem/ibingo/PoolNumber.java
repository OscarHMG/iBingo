package com.rayzem.ibingo;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PoolNumber {
    private int number;
    private boolean selected;
    private TextView textView;

    public PoolNumber(int number, boolean selected,  Context context) {
        this.number = number;
        this.selected = selected;
        this.textView = new TextView(context);
        this.textView.setId(number);
        this.textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        this.textView.setText(""+number);
        this.textView.setTextSize(11);


    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
