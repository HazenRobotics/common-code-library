package com.hazenrobotics.commoncode.models.colors;

@SuppressWarnings("unused")
public enum SimpleColor implements NamedColor {
    BLACK(new Color(0, 0, 0)),
    WHITE(new Color(255, 255, 255)),
    RED(new Color(255, 0, 0)),
    GREEN(new Color(0, 255, 0)),
    BLUE(new Color(0, 0, 255));

    protected Color color;

    SimpleColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Color other) {
        return other.equals(this);
    }
}
