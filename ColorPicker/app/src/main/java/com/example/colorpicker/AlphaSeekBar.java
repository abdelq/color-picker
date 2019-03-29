package com.example.colorpicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

import static android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT;

class AlphaSeekBar extends AppCompatSeekBar {
    private GradientDrawable drawable;

    AlphaSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        drawable = new GradientDrawable(LEFT_RIGHT,
                new int[]{Color.TRANSPARENT, Color.BLACK});
        drawable.setStroke(1, Color.BLACK);

        setProgress(0xFF);
        setProgressDrawable(drawable);

        setBackground(new InsetDrawable(context.getDrawable(R.drawable.checker), getPaddingStart()));
    }

    void updateGradient(@ColorInt int color) {
        drawable.setColors(new int[]{
                Color.TRANSPARENT, color | Color.BLACK
        });
    }
}
