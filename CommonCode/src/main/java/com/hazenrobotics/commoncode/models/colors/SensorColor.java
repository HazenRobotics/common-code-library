package com.hazenrobotics.commoncode.models.colors;

/**
 * Colors and numbers from I2c Color Sensor's color number; See <a href="http://www.modernroboticsinc.com/Content/Images/uploaded/ColorNumber.png">color values here</a>.
 */
public enum SensorColor implements NamedColorEnum {
    BLACK(new Color(0, 0, 0), 0),
    PURPLE(new Color(130, 0, 203), 1),
    VIOLET(new Color(45, 0, 202), 2),
    BLUE(new Color(27, 68, 192), 3),
    TEAL(new Color(180, 87, 58), 4),
    GREEN(new Color(8, 195, 94), 5),
    LIME(new Color(84, 210, 73), 6),
    LEMON(new Color(246, 251, 30), 7),
    YELLOW(new Color(255, 240, 21), 8),
    ORANGE(new Color(255, 178, 17), 9),
    TOMATO(new Color(255, 78, 12), 10),
    RED(new Color(255, 12, 51), 11),
    MAGENTA(new Color(255, 12, 171), 12),
    PALE_RED(new Color(254, 199, 169), 13),
    PALE_GREEN(new Color(215, 255, 165), 14),
    PALE_BLUE(new Color(170, 211, 220), 15),
    WHITE(new Color(255, 255, 255), 16),
    NULL(null, -1);

    Color color;
    int numericId;

    SensorColor(Color color, int numeric) {
        this.color = color;
        this.numericId = numeric;
    }

    public Color getColor() {
        return color;
    }

    public boolean equals(Color other) {
        return other.equals(this);
    }

    /**
     * Gets the numeric id associated with this color
     * @return A number between 0 and 16 (or -1 if the color is null)
     */
    public int getNumber() {
        return numericId;
    }

    /**
     * Gets the color associated with a number. Can be used to convert an I2c sensor number into a
     * color
     * @param numeric The number of the color between 0 and 16
     * @return The color which has the specified numeric id. NULL color if the number was
     * not between 0 and 16.
     */
    public static SensorColor getByNumber(int numeric) {
        switch (numeric) {
            case 0: return BLACK;
            case 1: return PURPLE;
            case 2: return VIOLET;
            case 3: return BLUE;
            case 4: return TEAL;
            case 5: return GREEN;
            case 6: return LIME;
            case 7: return LEMON;
            case 8: return YELLOW;
            case 9: return ORANGE;
            case 10: return TOMATO;
            case 11: return RED;
            case 12: return MAGENTA;
            case 13: return PALE_RED;
            case 14: return PALE_GREEN;
            case 15: return PALE_BLUE;
            case 16: return WHITE;
            default: return NULL;
        }
    }

    /**
     * @return The numeric id and the name of the color as declared inside the enum within a string
     */
    @Override
    public String toString() {
        return numericId + " - " + super.toString();
    }
}
