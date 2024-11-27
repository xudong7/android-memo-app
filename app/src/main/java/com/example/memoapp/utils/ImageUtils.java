package com.example.memoapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static String saveImageToInternalStorage(Context context, Bitmap bitmap) {
        File directory = context.getFilesDir();
        File imageFile = new File(directory, System.currentTimeMillis() + ".png");

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return imageFile.getAbsolutePath();
    }
}