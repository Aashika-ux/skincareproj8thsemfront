package com.example.skincare.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class HighlightAdapter extends ArrayAdapter<String> {

    private String keyword = "";

    public HighlightAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    // Set the current keyword to highlight
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        String item = getItem(position);

        if (keyword != null && !keyword.isEmpty() && item != null) {
            String lowerItem = item.toLowerCase();
            String lowerKeyword = keyword.toLowerCase();

            int start = lowerItem.indexOf(lowerKeyword);
            if (start >= 0) {
                int end = start + keyword.length();
                SpannableString spannable = new SpannableString(item);
                spannable.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                view.setText(spannable);
            } else {
                view.setText(item);
            }
        } else {
            view.setText(item);
        }

        return view;
    }
}
