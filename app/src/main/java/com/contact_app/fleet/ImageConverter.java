package com.contact_app.fleet;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

public class ImageConverter {

    public static Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap yourSelectedImage = Bitmap.createBitmap(source, x, y, size, size);
        if (yourSelectedImage != source) {
            source.recycle();
        }
        return yourSelectedImage;
    }

    public static Bitmap rotateImageBy(int degree , Bitmap yourSelectedImage) {

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        yourSelectedImage = Bitmap.createBitmap(yourSelectedImage, 0, 0,
                yourSelectedImage.getWidth(),
                yourSelectedImage.getHeight(), matrix, true);
        return yourSelectedImage;
    }

    public static byte[] convertToByteArray(Bitmap yourSelectedImage) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (yourSelectedImage != null) {
            yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        }
        return stream.toByteArray();
    }
}
