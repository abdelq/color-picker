package com.example.colorpicker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;

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

    public ColoredSeekBar redSeekBar, greenSeekBar, blueSeekBar;
    public HSeekBar hSeekBar;


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
        setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 3.4 OnColorPickedListener
                onColorPickedListener.onColorPicked(ColorPickerDialog.this, getColor());
            }
        });
        setButton(DialogInterface.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
            }
        });
        setCancelable(true);

        // Initialize SV gradient
        seekSV = v.findViewById(R.id.seekSV);
        redSeekBar = v.findViewById(R.id.seekR);
        greenSeekBar = v.findViewById(R.id.seekG);
        blueSeekBar = v.findViewById(R.id.seekB);
        hSeekBar = v.findViewById(R.id.seekH);

        saturationValueGradient = new SaturationValueGradient();
        seekSV.setInsetDrawable(saturationValueGradient);

        // Exemple pour afficher un gradient SV centré sur du rouge pur.
        saturationValueGradient.setColor(Color.RED);

        // 3.6 + 3.7
        setSeekBarSGB();

        hSeekBar.setHSeekBar(this, redSeekBar, greenSeekBar,
                blueSeekBar, saturationValueGradient, seekSV);

        seekSV.setBars(redSeekBar, greenSeekBar, blueSeekBar, hSeekBar);

        // Default color
        setColor(getContext().getColor(R.color.defaultColor));
    }

    // 3.6 + 3.7 SeekBar avec gradient rgb
    public void setSeekBarSGB(){

        redSeekBar.setElements(this, hSeekBar, seekSV);
        redSeekBar.setColorSeekBar(1, 0, 0);

        greenSeekBar.setElements(this, hSeekBar, seekSV);
        greenSeekBar.setColorSeekBar(0, 1, 0);

        blueSeekBar.setElements(this, hSeekBar, seekSV);
        blueSeekBar.setColorSeekBar(0, 0, 1);

        updateColors();
    }

    public void updateColors(){
        redSeekBar.updateColor();
        greenSeekBar.updateColor();
        blueSeekBar.updateColor();
    }

    public int getR(){ return r;}
    public int getG(){ return g;}
    public int getB(){ return b;}

    public float getH(){ return RGBtoHSV(r,g,b)[0]; }
    public float getS(){ return RGBtoHSV(r,g,b)[1]; }
    public float getV(){ return RGBtoHSV(r,g,b)[2]; }

    public int getRFromHSV(float h, float s, float v) {
        return HSVtoRGB(h,s,v)[0];
    }
    public int getGFromHSV(float h, float s, float v) {
        return HSVtoRGB(h,s,v)[1];
    }
    public int getBFromHSV(float h, float s, float v) {
        return HSVtoRGB(h,s,v)[2];
    }

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

    static private int[] HSVtoRGB(float h, float s, float v){
        // 3.8.2 HSV à RGB: Elle doit convertir un trio de valeurs HSL à un trio de valeurs RGB

        float hPrime, sPrime, vPrime;
        hPrime = h/60;
        sPrime = s/100;
        vPrime = v/100;

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

    static private float[] RGBtoHSV(int r, int g, int b){
        // 3.8.1 RGB à HSV: elle doit convertir un trio de valeurs RGB à un trio de valeurs HSV
        if(r == 0 && g == 0 && b == 0)
            return new float[]{0, 0, 0};

        int cMax = Math.max(r, Math.max(g, b));
        int cMin = Math.min(r, Math.min(g, b));

        int delta = cMax-cMin;
        float hPrime, h, s, v;

        if(cMax == r) hPrime = delta == 0? 0 : (float) (g-b)/delta;
        else if(cMax == g) hPrime = delta == 0? 2 : 2+(float) (b-r)/delta;
        else hPrime = delta == 0 ? 4 : 4+(float)(r-g)/delta;

        h = hPrime >= 0 ? 60*hPrime : 60*(hPrime+6);
        //TODO: La formule du s dans le devoir n'est pas exacte...
        s = 100*delta/cMax;
        v = 100*cMax/MAX_RGB_VALUE;

        return new float[]{ h, s, v};
    }

    public void setOnColorPickedListener(OnColorPickedListener onColorPickedListener) {
        // 3.4 OnColorPickedListener
        this.onColorPickedListener = onColorPickedListener;
    }

    public interface OnColorPickedListener{
        void onColorPicked(ColorPickerDialog colorPickerDialog, @ColorInt int color);
    }
}

