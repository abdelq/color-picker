package com.example.colorpicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import static android.graphics.Color.*;
import static android.view.View.inflate;

class ColorPickerDialog extends AlertDialog {
    private final static int MAX_RGB_VALUE = 255,
            MAX_H_VALUE = 360, MAX_SV_VALUE = 100;

    private @ColorInt int color = Color.BLACK;
    private OnColorPickedListener onColorPickedListener;

    private HueSeekBar seekH;
    private AreaPicker seekSV;
    private ColorSeekBar seekR, seekG, seekB;
    private AlphaSeekBar seekA;

    ColorPickerDialog(Context context) {
        super(context);
        setCancelable(true);

        // Initialize dialog
        View v = inflate(context, R.layout.dialog_picker, null);
        setView(v);

        // Title & Buttons
        Resources res = context.getResources();
        setTitle(res.getString(R.string.pick_color));
        setButton(res.getString(android.R.string.ok), (dialog, which) ->
                onColorPickedListener.onColorPicked(this, color));
        setButton2(res.getString(android.R.string.cancel), (dialog, which) -> {
        });

        // Initialize seek bars
        seekH = v.findViewById(R.id.seekH);
        seekSV = v.findViewById(R.id.seekSV);
        seekR = v.findViewById(R.id.seekR);
        seekG = v.findViewById(R.id.seekG);
        seekB = v.findViewById(R.id.seekB);
        seekA = v.findViewById(R.id.seekA);

        seekR.init(this, Color.RED);
        seekG.init(this, Color.GREEN);
        seekB.init(this, Color.BLUE);

        seekH.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekSV.updateGradient(HSVtoRGB(progress, MAX_SV_VALUE, MAX_SV_VALUE));

                if (fromUser) {
                    float[] hsv = RGBtoHSV(red(color), green(color), blue(color));
                    setProgress(HSVtoRGB(progress, hsv[1], hsv[2]));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekSV.setOnPickedListener((areaPicker, x, y, fromUser) -> {
            if (fromUser) {
                setProgress(HSVtoRGB(seekH.getProgress(), x, y));
            }
        });

        seekA.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setColor(progress);
                ((GradientDrawable) seekBar.getProgressDrawable()).setColors(new int[]{
                        Color.TRANSPARENT, color | Color.BLACK
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private static int[] HSVtoRGB(float h, float s, float v) {
        float hPrime = h / (MAX_H_VALUE / 6f);
        float sPrime = s / MAX_SV_VALUE;
        float vPrime = v / MAX_SV_VALUE;

        float c = sPrime * vPrime;
        float delta = vPrime - c;
        float x = 1 - Math.abs(hPrime % 2 - 1);

        float rPrime = 0, gPrime = 0, bPrime = 0;
        if (0 <= hPrime && hPrime <= 1) {
            rPrime = 1;
            gPrime = x;
            bPrime = 0;
        } else if (1 < hPrime && hPrime <= 2) {
            rPrime = x;
            gPrime = 1;
            bPrime = 0;
        } else if (2 < hPrime && hPrime <= 3) {
            rPrime = 0;
            gPrime = 1;
            bPrime = x;
        } else if (3 < hPrime && hPrime <= 4) {
            rPrime = 0;
            gPrime = x;
            bPrime = 1;
        } else if (4 < hPrime && hPrime <= 5) {
            rPrime = x;
            gPrime = 0;
            bPrime = 1;
        } else if (5 < hPrime && hPrime <= 6) {
            rPrime = 1;
            gPrime = 0;
            bPrime = x;
        }

        return new int[]{
                (int) (MAX_RGB_VALUE * (c * rPrime + delta)),
                (int) (MAX_RGB_VALUE * (c * gPrime + delta)),
                (int) (MAX_RGB_VALUE * (c * bPrime + delta))
        };
    }

    static float[] RGBtoHSV(int r, int g, int b) {
        int cMax = Math.max(r, Math.max(g, b));
        int cMin = Math.min(r, Math.min(g, b));
        int delta = cMax - cMin;

        float hPrime = 0;
        if (delta > 0) {
            if (cMax == r) {
                hPrime = 0 + (float) (g - b) / delta;
            } else if (cMax == g) {
                hPrime = 2 + (float) (b - r) / delta;
            } else {
                hPrime = 4 + (float) (r - g) / delta;
            }
        }

        return new float[]{
                (MAX_H_VALUE / 6f) * (hPrime + (hPrime < 0 ? 6 : 0)),
                cMax > 0 ? (float) MAX_SV_VALUE * delta / cMax : 0,
                (float) MAX_SV_VALUE * cMax / MAX_RGB_VALUE
        };
    }

    @ColorInt int getColor() {
        return color;
    }

    void setColor(int red, int green, int blue) {
        color = argb(alpha(color), red, green, blue);
    }

    private void setColor(int alpha) {
        color = argb(alpha, red(color), green(color), blue(color));
    }

    private void setProgress(int[] rgb) {
        seekR.setProgress(rgb[0]);
        seekG.setProgress(rgb[1]);
        seekB.setProgress(rgb[2]);
    }

    void setProgress(float hue) {
        seekH.setProgress((int) hue);
    }

    void setPick(float x, float y) {
        seekSV.setPick(x, y);
    }

    void updateAlphaGradient() {
        seekA.updateGradient(color);
    }

    void setOnColorPickedListener(OnColorPickedListener listener) {
        onColorPickedListener = listener;
    }

    interface OnColorPickedListener {
        void onColorPicked(ColorPickerDialog colorPickerDialog, @ColorInt int color);
    }
}

