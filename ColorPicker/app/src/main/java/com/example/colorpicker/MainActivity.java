package com.example.colorpicker;

import android.content.DialogInterface;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* CETTE MÉTHODE DEVRA ÊTRE MODIFIÉE */


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ColorPickerDialog dialog = new ColorPickerDialog(this);

        // 3.4 OnColorPickedListener
        dialog.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
            @Override
            public void onColorPicked(ColorPickerDialog colorPickerDialog, @ColorInt int color){
                showPickedColor(color);
            }
        });

        findViewById(R.id.button_pick).setOnClickListener((View v) -> dialog.show());
    }

    public void showPickedColor(@ColorInt int color){
        View ttest = findViewById(R.id.picked_color);
        ttest.setBackgroundColor(color);
    }


}
