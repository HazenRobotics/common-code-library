package com.hazenrobotics.commoncode.models.colors;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamedColorListTest {

    NamedColorList list1;
    NamedColorList list2;

    @Before
    public void setUp()  {
        list1 = new NamedColorList();
        list2 = new NamedColorList();
    }

    @Test
    public void addColorAndContains() {
        list1.addColor(SimpleColor.BLACK);
        assertTrue(list1.contains(SimpleColor.BLACK));
        assertFalse(list1.contains(SimpleColor.BLUE));
    }

    @Test
    public void addColorList() {
        list2.addColor(SimpleColor.BLACK);
        list1.addColorList(list2);
        assertTrue(list1.contains(SimpleColor.BLACK));
    }

    @Test
    public void clear() {
        list1.addColor(SimpleColor.BLACK);
        assertTrue(list1.contains(SimpleColor.BLACK));
        list1.clear();
        assertFalse(list1.contains(SimpleColor.BLACK));
    }
}