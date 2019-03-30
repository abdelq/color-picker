package com.example.colorpicker;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorInt;

/**
 * La classe SaturationValueGradient fournit les méthodes qui initialisent
 * et mettent à jour un espace qui affiche un dégradé 2D.
 */
class SaturationValueGradient extends LayerDrawable {
    private GradientDrawable saturationGradient;

    private SaturationValueGradient() {
        super(prepareLayers());
        saturationGradient = (GradientDrawable) getDrawable(0);
    }

    SaturationValueGradient(@ColorInt int color) {
        this();
        setColor(color);
    }

    /**
     * Le dégradé 2D du AreaPicker est créé en superposant 2 dégradés linéaires. Un à
     * l'horizontal, et l'autre vertical. Le dégradé du dessus est dégrade entre noir et
     * transparent, de telle sorte à ce qu'il laisse progressivement transparaître le dégradé
     * en dessous de lui. Seul le dégradé du dessous, qui va de blanc à la couleur
     * pleinement saturée, a besoin d'être modifié. C'est "hacky", mais ça marche!
     */
    private static GradientDrawable[] prepareLayers() {
        /* Color layer */
        GradientDrawable saturationGradient = new GradientDrawable();
        saturationGradient.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);

        /* Black and white layer */
        GradientDrawable valueGradient = new GradientDrawable();
        valueGradient.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        valueGradient.setColors(new int[]{Color.TRANSPARENT, Color.BLACK});
        valueGradient.setStroke(1, Color.BLACK);

        return new GradientDrawable[]{saturationGradient, valueGradient};
    }

    /**
     * Met à jour la couleur du gradient
     *
     * @param color couleur choisie par l'usager
     */
    void setColor(@ColorInt int color) {
        saturationGradient.setColors(new int[]{Color.WHITE, color});
    }
}
