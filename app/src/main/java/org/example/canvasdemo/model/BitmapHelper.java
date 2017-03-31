package org.example.canvasdemo.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by viliu on 3/30/2017.
 */

public class BitmapHelper {
    public static Bitmap RotateBitmap(Bitmap source, MovementDirection direction) {
        Matrix m = new Matrix();

        switch(direction) {
            case LEFT:
                // Get the center of the bitmap to flip it
                m.postScale(-1, 1, source.getWidth()/2f, source.getHeight()/2);
                return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, true);
            default:
            case RIGHT:
                return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, true);
            case UP:
                m.postRotate(270f);
                return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, true);
            case DOWN:
                m.postRotate(90f);
                return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, true);
        }
    }
}
