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

    public ColoredSeekBar redSeekBar, greenSeekBar, blueSeekBar;


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
    public void setSeekBarSGB(View v){
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

    public void updateColors(){
        redSeekBar.updateColor();
        greenSeekBar.updateColor();
        blueSeekBar.updateColor();
    }

    public int getR(){ return r;}
    public int getG(){ return g;}
    public int getB(){ return b;}

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
        /* IMPLÉMENTER CETTE MÉTHODE
         * Elle doit convertir un trio de valeurs HSL à un trio de valeurs RGB
         * */

        return new int[3];
    }

    static private int[] RGBtoHSV(int r, int g, int b){
        // 3.8 RGB à HSV: elle doit convertir un trio de valeurs RGB à un trio de valeurs HSV
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
        s = 100*delta/cMax;
        v = 100*cMax/MAX_RGB_VALUE;

        return new int[]{(int) h, (int) s, (int) v};
    }

    public void setOnColorPickedListener(OnColorPickedListener onColorPickedListener) {
        // 3.4 OnColorPickedListener
        this.onColorPickedListener = onColorPickedListener;
    }

    public interface OnColorPickedListener{
        void onColorPicked(ColorPickerDialog colorPickerDialog, @ColorInt int color);
    }
}
