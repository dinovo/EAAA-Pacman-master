package org.example.canvasdemo.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

import static java.lang.Math.pow;

public class CollisionDetector {
    public static boolean isCollisionDetected(int x1, int y1, int x2, int y2) {
        if(Math.sqrt(Math.pow(x2-x1, 2)+ Math.pow(y2-y1, 2)) < 100) {
            return true;
        }
        return false;
    }
}
