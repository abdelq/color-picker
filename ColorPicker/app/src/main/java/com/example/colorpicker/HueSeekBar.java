package com.example.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

import static android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT;

class HueSeekBar extends AppCompatSeekBar {
    HueSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        GradientDrawable drawable = new GradientDrawable(LEFT_RIGHT,
                getResources().getIntArray(R.array.rainbow));
        drawable.setStroke(1, Color.BLACK);

        setProgressDrawable(drawable);
    }
}
