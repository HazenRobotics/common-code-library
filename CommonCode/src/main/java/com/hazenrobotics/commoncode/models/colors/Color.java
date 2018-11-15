package com.hazenrobotics.commoncode.models.colors;

/**
 * An RGB 0-255 color.
 */
public class Color {
    /**
     * This is the default margin of error used by {@link #approximatelyEquals(Color)}.
     */
    public static final int DEFAULT_MAX_DIFFERENCE = 40;

    protected final int red, green, blue;

    /**
     * An RGB 0-255 color.
     * @param red Between 0 and 255.
     * @param green Between 0 and 255.
     * @param blue Between 0 and 255.
     */
    public Color(int red, int green, int blue) {
        if (red < 0 || green < 0 || blue < 0)
            throw new IllegalArgumentException("A color cannot have values outside the range of an unsigned byte (0-255). Was negative.");
        if (red > 255 || green > 255 || blue > 255)
            throw new IllegalArgumentException("A color cannot have values outside the range of an unsigned byte (0-255). Was above 255.");
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Returns the red value of the RGB color
     * @return A number between 0 and 255
     */
    public int getRed() {
        return red;
    }

    /**
     * Returns the green value of the RGB color
     * @return A number between 0 and 255
     */
    public int getGreen() {
        return green;
    }

    /**
     * Returns the blue value of the RGB color
     * @return A number between 0 and 255
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Compares if this color is the same as a named color
     * @param namedColor The named color to compare to
     * @return If the RGB values of this color and the named color are the same
     */
    public boolean equals(NamedColor namedColor) {
        return this.equals(namedColor.getColor());
    }

    /**
     * Compares if the difference between this color and another is less than a certain amount
     * @param namedColor The named color to compare to
     * @return If the colors' differences were less than the {@link #DEFAULT_MAX_DIFFERENCE default max difference}
     */
    public boolean approximatelyEquals(NamedColor namedColor) {
        return approximatelyEquals(namedColor.getColor());
    }

    /**
     * Compares if the difference between this color and another is less than a certain amount
     * @param colorEnum The named color to compare to
     * @param maxDifference The maximum difference between the two colors
     * @return If the colors' differences were less than the max difference
     */
    public boolean approximatelyEquals(NamedColor colorEnum, int maxDifference) {
        return approximatelyEquals(colorEnum.getColor(), maxDifference);
    }

    /**
     * Compares if the difference between this color and another is less than a certain amount
     * @param other The color to compare to
     * @return If the colors' differences were less than the {@link #DEFAULT_MAX_DIFFERENCE default max difference}
     *
     */
    public boolean approximatelyEquals(Color other) {
        return approximatelyEquals(other, DEFAULT_MAX_DIFFERENCE);
    }

    /**
     * Compares if the difference between this color and another is less than a certain amount
     * @param other The color to compare to
     * @param maxDifference The maximum difference between the two colors
     * @return If the colors' differences were less than the max difference
     */
    public boolean approximatelyEquals(Color other, int maxDifference) {
        return maxDifference >= difference(other);
    }

    /**
     * The difference between this color and another color
     * @param other The color to compare to
     * @return The summation of the difference of the rgb values
     */
    public int difference(Color other) {
        return Math.abs(red - other.getRed()) + Math.abs(green - other.getGreen()) + Math.abs(blue - other.getBlue());
    }
}