package com.example.colorpicker;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

import static android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT;

/**
 * La classe HueSeekBar initialise une barre qui permet la selection de
 * la valeur de la teinte (H), son gradient étant un arc-en-ciel.
 */
class HueSeekBar extends AppCompatSeekBar {
    HueSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        GradientDrawable drawable = new GradientDrawable(LEFT_RIGHT,
                getResources().getIntArray(R.array.rainbow));

        setProgressDrawable(drawable);
    }
}
