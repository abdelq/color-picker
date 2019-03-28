package com.example.colorpicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this);
        colorPickerDialog.setOnColorPickedListener((dialog, color) ->
                findViewById(R.id.picked_color).setBackgroundColor(color));
        findViewById(R.id.button_pick).setOnClickListener(view -> colorPickerDialog.show());
    }
}
