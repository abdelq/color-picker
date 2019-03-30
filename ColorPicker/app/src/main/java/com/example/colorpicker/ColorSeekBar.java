package com.example.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.widget.SeekBar;

import static android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT;
import static com.example.colorpicker.ColorPickerDialog.RGBtoHSV;

/**
 * La classe ColorSeekBar fournit les méthodes qui initialisent et mettent
 * à jour des barres qui permettent la selection des valeurs R,G ou B.
 */
class ColorSeekBar extends AppCompatSeekBar {
    private @ColorInt int seekColor;
    private GradientDrawable drawable;
    private ColorPickerDialog dialog;

    /*
     * Initialise la barre de sélection
     *
     * @param context
     * @param attrs
     */
    ColorSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        drawable = new GradientDrawable(LEFT_RIGHT, null);
        drawable.setStroke(1, Color.BLACK);

        setProgressDrawable(drawable);
        setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                @ColorInt int color = dialog.getColor();

                int[] rgb = {Color.red(color), Color.green(color), Color.blue(color)};
                float[] hsv = RGBtoHSV(rgb[0], rgb[1], rgb[2]);
                if (seekColor == Color.RED) {
                    rgb[0] = progress;
                } else if (seekColor == Color.GREEN) {
                    rgb[1] = progress;
                } else {
                    rgb[2] = progress;
                }

                dialog.setColor(rgb[0], rgb[1], rgb[2]);
                if (fromUser) {
                    dialog.setProgress(hsv[0]);
                    dialog.setPick(hsv[1] / 100, (100 - hsv[2]) / 100);
                }

                updateGradient();
                dialog.updateAlphaGradient();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    void init(ColorPickerDialog dialog, @ColorInt int color) {
        this.dialog = dialog;
        this.seekColor = color;

        drawable.setColors(new int[]{Color.BLACK, seekColor});
    }

    /*
     * Met à jour la couleur du gradient de la barre
     */
    void updateGradient() {
        @ColorInt int color = dialog.getColor();

        drawable.setColors(new int[]{
                Color.BLACK | (color & ~seekColor),
                color | seekColor
        });
    }
}
