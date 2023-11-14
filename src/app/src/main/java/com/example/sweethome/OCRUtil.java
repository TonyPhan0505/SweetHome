package com.example.sweethome;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

public class OCRUtil {
    private static final String TAG = "OCRUtil";

    public static String performOCR(Bitmap bitmap) {
        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init(Environment.getExternalStorageDirectory().toString(), "eng"); // Replace "eng" with your language code
        tessBaseAPI.setImage(bitmap);
        String result = tessBaseAPI.getUTF8Text();
        tessBaseAPI.end();

        Log.d(TAG, "OCR Result: " + result);

        return result;
    }
}
