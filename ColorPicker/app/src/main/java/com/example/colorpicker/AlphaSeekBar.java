package com.example.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

import static android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT;

/**
 * La classe AlphaSeekBar fournit des méthodes pour une barre qui
 * permet à l'usager de choisir la transparence de la couleur.
 */
class AlphaSeekBar extends AppCompatSeekBar {
    private GradientDrawable drawable;

    /**
     * Initialise la barre
     */
    AlphaSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        drawable = new GradientDrawable(LEFT_RIGHT,
                new int[]{Color.TRANSPARENT, Color.BLACK});

        setProgressDrawable(drawable);
        setBackground(new InsetDrawable(
                context.getDrawable(R.drawable.checker),
                getPaddingLeft(), -1, getPaddingRight(), -1));
    }

    /**
     * Met à jour la couleur du gradient de la barre
     *
     * @param color couleur choisie par l'usager
     */
    void updateGradient(@ColorInt int color) {
        drawable.setColors(new int[]{
                Color.TRANSPARENT, color | Color.BLACK
        });
    }
}
