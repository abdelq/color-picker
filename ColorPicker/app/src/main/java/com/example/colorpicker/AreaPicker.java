package com.example.colorpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.InsetDrawable;
import android.support.v4.math.MathUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static android.graphics.Color.rgb;

/**
 * La classe AreaPicker fournit des méthodes qui traitent le déplacement du
 * marqueur à l'intérieur de l'espace créé par le SaturationValueGradient.
 */
public class AreaPicker extends View {
    private float x = 0, y = 1;

    private static int thumbRadius, padding;
    private Paint thumbPaint;
    private SaturationValueGradient gradient;
    private OnPickedListener onPickedListener;

    /**
     * Initialise le marqueur et son arrière-plan
     */
    AreaPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        thumbPaint = new Paint();
        thumbPaint.setStyle(Paint.Style.FILL);
        thumbPaint.setColor(context.getColor(R.color.thumb_color));

        thumbRadius = getResources().getInteger(R.integer.thumb_radius);
        padding = thumbRadius * 2;

        /*
         * On utilise un InsetDrawable comme arrière-plan, pour que, lorsque l'usager fournira son
         * propre Drawable à afficher dans le plan de sélection, celui-ci ne se rendent pas
         * jusqu'au bord du View, mais plutôt laisse une petite marge pour que le le marqueur de
         * sélection puisse déborder du plan de sélection sans déborder du View.
         */
        gradient = new SaturationValueGradient(Color.RED);
        setBackground(new InsetDrawable(gradient, padding));
    }

    /**
     * Met à jour la couleur de l'arrière-plan
     *
     * @param rgb couleur choisie par l'usager
     */
    void updateGradient(int[] rgb) {
        gradient.setColor(rgb(rgb[0], rgb[1], rgb[2]));
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        x = screenToFracCoord(event.getX(), getWidth());
        y = screenToFracCoord(event.getY(), getHeight());

        onChange(true);
        return true;
    }

    /**
     * @return valeur du x choisi
     */
    private int getPickedX() {
        return (int) (x * 100);
    }

    /**
     * @return valeur du y choisi
     */
    private int getPickedY() {
        return (int) (100 - (y * 100));
    }

    /**
     * Change les valeurs x et y du marqueur
     *
     * @param x nouveau x
     * @param y nouveau y
     */
    void setPick(float x, float y) {
        this.x = x;
        this.y = y;

        onChange(false);
    }

    /**
     * Cette fonction doit être appelée immédiatement après que la coordonnée représentée par cet
     * AreaPicker a été mise à jour. (Bref, dès que this.x et/ou this.y a changé, il faut appeler
     * onChange.)
     */
    private void onChange(boolean fromUser) {
        if (onPickedListener != null) {
            onPickedListener.onPicked(this, getPickedX(), getPickedY(), fromUser);
        }

        invalidate();
    }

    /**
     * Convertit une coordonnée exprimée en espace-écran en une coordonée
     * exprimée en fraction de l'étendue (0 à 1).
     */
    private float screenToFracCoord(float screenCoord, float screenRange) {
        float fracCoord = (screenCoord - padding) / (screenRange - 2 * padding);
        return MathUtils.clamp(fracCoord, 0, 1);
    }

    /**
     * Convertit une coordonnée exprimée en fraction de l'étendue (0 à 1) en espace-écran.
     */
    private float fracToScreenCoord(float fracCoord, float screenRange) {
        return fracCoord * (screenRange - 2 * padding) + padding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float ax = fracToScreenCoord(x, getWidth());
        float ay = fracToScreenCoord(y, getHeight());

        canvas.drawCircle(ax, ay, thumbRadius, thumbPaint);
    }

    void setOnPickedListener(OnPickedListener listener) {
        onPickedListener = listener;
    }

    interface OnPickedListener {
        void onPicked(AreaPicker areaPicker, int x, int y, boolean fromUser);
    }
}
