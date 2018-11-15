package com.hazenrobotics.commoncode.models.colors;

import java.util.LinkedList;
import java.util.List;

/**
 * A list of named color which can be used to quickly check if a color type is contained within
 * the list.
 */
public class NamedColorList {
    protected List<NamedColor> colors;

    /**
     * Initializes a new color list
     */
    public NamedColorList() {
        colors = new LinkedList<>();
    }

    /**
     * Adds a color name to the list
     * @param namedColor The color to add to the list
     */
    public void addColor(NamedColor namedColor) {
        colors.add(namedColor);
    }

    /**
     * Adds all the colors on another list to this one
     * @param other The list of colors to add
     */
    public void addColorList(NamedColorList other) {
        colors.addAll(other.colors);
    }

    /**
     * Removes all colors from the list
     */
    public void clear() {
        colors.clear();
    }

    /**
     * Checks if a color is on this list
     * @param color The color to check for on the list
     * @return If the color is on the list
     */
    public boolean contains(NamedColor color) {
        return colors.contains(color);
    }
}
