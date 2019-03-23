package com.example.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.example.colorpicker.Views.AreaPicker;

public class ColoredSeekBar extends AppCompatSeekBar {
    private final static int MAX_RGB_VALUE = 255;
    private int isRed, isGreen, isBlue;
    private int r, g, b;
    private ColorPickerDialog colorPickerDialog;
    private HSeekBar hBar;
    private AreaPicker area;
    GradientDrawable gd;

    public ColoredSeekBar(Context context) {
        super(context);
        init();
    }

    public ColoredSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColoredSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        gd = new GradientDrawable();
        setMax(MAX_RGB_VALUE);
        setOnSeekBarChangeListener(listener);
        gd.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        gd.setStroke(1, Color.BLACK);
    }

    public void setElements(ColorPickerDialog colorPickerDialog,
                            HSeekBar hBar, AreaPicker area){
        this.colorPickerDialog = colorPickerDialog;
        this.hBar = hBar;
        this.area = area;
    }

    OnSeekBarChangeListener listener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            r = isRed*progress+(1-isRed)*r;
            g = isGreen*progress+(1-isGreen)*g;
            b = isBlue*progress+(1-isBlue)*b;

            float h = colorPickerDialog.getH();
            float s = colorPickerDialog.getS();
            float v = colorPickerDialog.getV();

            colorPickerDialog.setColor(Color.rgb(r, g, b));
            colorPickerDialog.updateColors();

            if(fromUser) {
                updateHBar(h);
                updateArea(s,v);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };

    // update la valeur de la seekBar qui représente h
    void updateHBar(float h) {
        hBar.setProgress((int)h);
    }

    // update les valeurs de x et y dans AreaPicker
    void updateArea(float s, float v) {
        area.setPickedX(s/100);
        area.setPickedY((100-v)/100);
    }

    public void setColorSeekBar(int isRed, int isGreen, int isBlue){
        this.isRed = isRed;
        this.isGreen = isGreen;
        this.isBlue = isBlue;
    }

    void updateColor(){
        @ColorInt int colorMin;
        @ColorInt int colorMax;

        r = colorPickerDialog.getR();
        g = colorPickerDialog.getG();
        b = colorPickerDialog.getB();

        colorMin = Color.rgb((1-isRed)*r, (1-isGreen)*g, (1-isBlue)*b);
        colorMax = Color.rgb((1-isRed)*r+isRed*MAX_RGB_VALUE, (1-isGreen)*g+isGreen*MAX_RGB_VALUE,
                (1-isBlue)*b+isBlue*MAX_RGB_VALUE);

        gd.setColors(new int[] { colorMin, colorMax});
        this.setProgressDrawable(gd);
    }
}
