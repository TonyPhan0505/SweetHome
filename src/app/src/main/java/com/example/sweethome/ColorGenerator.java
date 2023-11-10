package com.example.sweethome;

import android.graphics.Color;

import java.util.Random;

public class ColorGenerator {
    private int colorCode;
    private int textColorCode;

    public ColorGenerator() {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        String colorCode = String.format("#%02X%02X%02X", red, green, blue);
        this.colorCode = Color.parseColor(colorCode);
    }

    public int getColorCode() {
        return this.colorCode;
    }

    public int getTextColorCode() {
        int backgroundColorInt = this.colorCode;
        double darkness = 1 - (0.299 * Color.red(backgroundColorInt) + 0.587 * Color.green(backgroundColorInt) + 0.114 * Color.blue(backgroundColorInt)) / 255;
        if (darkness < 0.5) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }
}
