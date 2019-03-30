package com.example.colorpicker;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * La classe MainActivity affiche un bouton qui permet de choisir une couleur
 * et un carré dans lequel la couleur choisie est démontrée.
 * Lorsque le bouton est cliqué, un popup apparait pour faire la sélection de
 * la couleur.
 */
class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View pickedColor = findViewById(R.id.picked_color);
        pickedColor.setBackground(new LayerDrawable(new Drawable[]{
                new InsetDrawable(getBaseContext().getDrawable(R.drawable.checker), 0),
                new ColorDrawable(Color.BLACK)
        }));

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this);
        colorPickerDialog.setOnColorPickedListener((dialog, color) -> {
            LayerDrawable layer = (LayerDrawable) pickedColor.getBackground();
            ((ColorDrawable) layer.getDrawable(1)).setColor(color);
        });
        findViewById(R.id.button_pick).setOnClickListener(view -> colorPickerDialog.show());
    }
}
