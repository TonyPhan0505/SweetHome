package com.example.sweethome;

import org.junit.Test;
import static org.junit.Assert.*;

import android.graphics.Color;

public class ColorGeneratorTest {

    @Test
    public void testColorCodeGeneration() {
        ColorGenerator colorGenerator = new ColorGenerator();
        int colorCode = colorGenerator.getColorCode();

        // Ensure that the generated color code is not zero (default value)
        assertNotEquals(0, colorCode);
    }

    @Test
    public void testTextColorCodeCalculation() {
        // Test for a specific background color
        int backgroundColor = Color.parseColor("#FFFFFF"); // White background
        ColorGenerator colorGenerator = new ColorGenerator();
        colorGenerator.setColorCode(backgroundColor); // Set the background color

        int textColorCode = colorGenerator.getTextColorCode();

        // Ensure that the calculated text color is either Color.BLACK or Color.WHITE
        assertTrue(textColorCode == Color.BLACK || textColorCode == Color.WHITE);
    }
}
