package com.wenruixing.spacecolony.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;
import android.widget.Toast;

public final class UiUtils {

    private UiUtils() {
    }

    public static void tintLabel(TextView textView, String colorHex) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor(colorHex));
        drawable.setCornerRadius(24f);
        textView.setBackground(drawable);
        textView.setTextColor(Color.WHITE);
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
