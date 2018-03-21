package com.bjz.imusicteacher.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static java.lang.System.arraycopy;

/**
 * Created by bjz on 3/20/2018.
 */

public class ProcessingUtils {

    public static Bitmap getGrayscale(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();

        Bitmap dest = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(dest);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0); //value of 0 maps the color to gray-scale
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(filter);
        canvas.drawBitmap(src, 0, 0, paint);
        return dest;
    }

    //todo add a cleaner way to convert byte[] - double[]
    public static double[][] reshapeAndConvertByteArray(byte[] target, int rows, int cols) {
        double[][] result = new double[rows][cols];
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = target[count++];
            }
        }

        return result;
    }

}
