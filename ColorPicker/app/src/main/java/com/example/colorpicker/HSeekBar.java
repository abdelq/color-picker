package com.example.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.example.colorpicker.Views.AreaPicker;

public class HSeekBar extends AppCompatSeekBar {
    GradientDrawable gd;
    private ColorPickerDialog colorPickerDialog;
    private ColoredSeekBar red, green, blue;
    private SaturationValueGradient gradient;
    private AreaPicker area;

    public HSeekBar(Context context) {
        super(context);
        init();
    }

    public HSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        gd = new GradientDrawable();
        setMax(360);
        setProgressDrawable(gd);
        gd.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        gd.setColors(new int[]{ Color.RED,
                                Color.YELLOW,
                                Color.GREEN,
                                Color.CYAN,
                                Color.BLUE,
                                Color.MAGENTA,
                                Color.RED });
        setOnSeekBarChangeListener(listener);
    }

    void setHSeekBar(ColorPickerDialog colorPickerDialog, ColoredSeekBar red,
                     ColoredSeekBar green, ColoredSeekBar blue,
                     SaturationValueGradient gradient, AreaPicker area) {
        this.colorPickerDialog = colorPickerDialog;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.gradient = gradient;
        this.area = area;
    }

    public int getR(float h, float s, float v) {
        return colorPickerDialog.getRFromHSV(h,s,v);
    }
    public int getG(float h, float s, float v) {
        return colorPickerDialog.getGFromHSV(h,s,v);
    }
    public int getB(float h, float s, float v) {
        return colorPickerDialog.getBFromHSV(h,s,v);
    }

    OnSeekBarChangeListener listener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            int h = progress;
            float s = colorPickerDialog.getS();
            float v = colorPickerDialog.getV();

            int r = getR(h,s,v);
            int g = getG(h,s,v);
            int b = getB(h,s,v);

            updateGradient(colorPickerDialog.getRFromHSV(h,100,100),
                    colorPickerDialog.getGFromHSV(h,100,100),
                    colorPickerDialog.getBFromHSV(h,100,100));

            area.setHValue(h);

            if (fromUser) {
                updateColoredSeekBars(r, g, b);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    void updateColoredSeekBars(int r, int g, int b) {
        red.setProgress(r);
        green.setProgress(g);
        blue.setProgress(b);
    }

    void updateGradient(int r, int g, int b) {
        gradient.setColor(Color.rgb(r,g,b));
    }

}
