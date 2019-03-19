package com.example.colorpicker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import com.example.colorpicker.Views.AreaPicker;

class ColorPickerDialog extends AlertDialog {
    private final static int MAX_RGB_VALUE = 255;
    private final static int MAX_SV_VALUE = 100;
    private final static int MAX_H_VALUE = 360;

    private AreaPicker seekSV;
    private SaturationValueGradient saturationValueGradient;

    // Représentation/stockage interne de la couleur présentement sélectionnée par le Dialog.
    private int r, g, b;
    private OnColorPickedListener onColorPickedListener;

    private ColoredSeekBar redSeekBar, greenSeekBar, blueSeekBar;

    ColorPickerDialog(Context context) {
        super(context);
        init(context);
    }

    ColorPickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    ColorPickerDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context){
        /* CETTE MÉTHODE DEVRA ÊTRE MODIFIÉE */

        // Initialize dialog
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_picker, null);
        setView(v);

        //3.2 Titre et boutons
        setTitle("Choisir une couleur");
        setButton(DialogInterface.BUTTON_POSITIVE, "Ok", (dialog, which) -> {
            // 3.4 OnColorPickedListener
            onColorPickedListener.onColorPicked(ColorPickerDialog.this, getColor());
        });
        setButton(DialogInterface.BUTTON_NEGATIVE, "cancel", (dialog, which) -> {
        });
        setCancelable(true);

        // Initialize SV gradient
        seekSV = v.findViewById(R.id.seekSV);
        saturationValueGradient = new SaturationValueGradient();
        seekSV.setInsetDrawable(saturationValueGradient);

        // Exemple pour afficher un gradient SV centré sur du rouge pur.
        saturationValueGradient.setColor(Color.RED);

        // 3.6 + 3.7
        setSeekBarSGB(v);

        // Default color
        setColor(getContext().getColor(R.color.defaultColor));
    }

    // 3.6 + 3.7 SeekBar avec gradient rgb
    private void setSeekBarSGB(View v){
        redSeekBar = v.findViewById(R.id.seekR);
        greenSeekBar = v.findViewById(R.id.seekG);
        blueSeekBar = v.findViewById(R.id.seekB);

        redSeekBar.setColorPickerDialog(this);
        redSeekBar.setColorSeekBar(1, 0, 0);

        greenSeekBar.setColorPickerDialog(this);
        greenSeekBar.setColorSeekBar(0, 1, 0);

        blueSeekBar.setColorPickerDialog(this);
        blueSeekBar.setColorSeekBar(0, 0, 1);

        updateColors();
    }

    void updateColors(){
        redSeekBar.updateColor();
        greenSeekBar.updateColor();
        blueSeekBar.updateColor();
    }

    int getR(){ return r;}
    int getG(){ return g;}
    int getB(){ return b;}

    @ColorInt int getColor(){
        // 3.3 Retourne la couleur présentement sélectionnée par le dialog.
        return Color.rgb(r, g, b);
    }

    public void setColor(@ColorInt int newColor){
        // 3.3 Met à jour l'état du dialog pour que la couleur sélectionnée corresponde à newColor
        r = Color.red(newColor);
        g = Color.green(newColor);
        b = Color.blue(newColor);
    }

    static private int[] HSVtoRGB(int h, int s, int v){
        // 3.8.2 HSV à RGB: Elle doit convertir un trio de valeurs HSL à un trio de valeurs RGB

        float hPrime, sPrime, vPrime;
        hPrime = (float) h/60;
        sPrime = (float) s/100;
        vPrime = (float) v/100;

        float c = sPrime*vPrime;
        //TODO: C' dans l'énoncé??
        float delta = vPrime-c;
        float x = 1-Math.abs((hPrime%2)-1);

        float rPrime, gPrime, bPrime;

        if(0 <= hPrime && hPrime <= 1){
            rPrime = 1;
            gPrime = x;
            bPrime = 0;
        } else if(1 < hPrime && hPrime <= 2){
            rPrime = x;
            gPrime = 1;
            bPrime = 0;
        } else if(2 < hPrime && hPrime <= 3){
            rPrime = 0;
            gPrime = 1;
            bPrime = x;
        } else if(3 < hPrime && hPrime <= 4){
            rPrime = 0;
            gPrime = x;
            bPrime = 1;
        } else if(4 < hPrime && hPrime <= 5){
            rPrime = x;
            gPrime = 0;
            bPrime = 1;
        } else if(5 < hPrime && hPrime <= 6){
            rPrime = 1;
            gPrime = 0;
            bPrime = x;
        } else {
            rPrime = 0;
            gPrime = 0;
            bPrime = 0;
        }

        return new int[]{(int) (MAX_RGB_VALUE*(c*rPrime+delta)), (int) (MAX_RGB_VALUE*(c*gPrime+delta)),
                (int) (MAX_RGB_VALUE*(c*bPrime+delta))};
    }

    static private int[] RGBtoHSV(int r, int g, int b){
        // 3.8.1 RGB à HSV: elle doit convertir un trio de valeurs RGB à un trio de valeurs HSV
        if(r == 0 && g == 0 && b == 0)
            return new int[]{0, 0, 0};

        int cMax = Math.max(r, Math.max(g, b));
        int cMin = Math.min(r, Math.min(g, b));

        int delta = cMax-cMin;
        float hPrime, h, s, v;

        if(cMax == r) hPrime = delta == 0? 0 : (float) (g-b)/delta;
        else if(cMax == g) hPrime = delta == 0? 2 : 2+(float) (b-r)/delta;
        else hPrime = delta == 0 ? 4 : 4+(float)(r-g)/delta;

        h = hPrime >= 0 ? 60*hPrime : 60*(hPrime+6);
        //TODO: La formule du s dans le devoir n'est pas exacte...
        s = 100*(float) delta/cMax;
        v = 100*(float) cMax/MAX_RGB_VALUE;

        return new int[]{(int) h, (int) s, (int) v};
    }

    private void setOnColorPickedListener(OnColorPickedListener onColorPickedListener) {
        // 3.4 OnColorPickedListener
        this.onColorPickedListener = onColorPickedListener;
    }

    public interface OnColorPickedListener{
        void onColorPicked(ColorPickerDialog colorPickerDialog, @ColorInt int color);
    }
}
