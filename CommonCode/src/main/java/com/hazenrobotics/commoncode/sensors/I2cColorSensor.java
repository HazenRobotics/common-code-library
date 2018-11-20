package com.hazenrobotics.commoncode.sensors;

import com.hazenrobotics.commoncode.models.colors.SensorColor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;

/**
 * An I2c color sensor object which can determine a {@link #getColor() color} being sensor and detect
 * the red, green, blue, and white values being seen.
 */
@SuppressWarnings("unused,WeakerAccess")
public class I2cColorSensor extends I2cSensor implements ColorSensor<SensorColor> {
    protected static final int DEFAULT_ADDRESS = 0x3c;
    protected static final int COLOR_REG_START = 0x04; //Register to start reading
    protected static final int COLOR_READ_LENGTH = 1; //Number of byte to read

    protected static final int COLOR_RED = 0x05;
    protected static final int COLOR_GREEN = 0x06;
    protected static final int COLOR_BLUE = 0x07;
    protected static final int COLOR_WHITE = 0x08;
    protected static final int LIGHT_OFF = 0x01;
    protected static final int LIGHT_ON = 0x00;

    /**
     * Creates a sensor with an address of {@link #DEFAULT_ADDRESS}
     * @param sensorDevice The sensor object on the hardware map to be used
     */
    public I2cColorSensor(I2cDevice sensorDevice) {
        super(sensorDevice, I2cAddr.create8bit((DEFAULT_ADDRESS)));
    }

    /**
     * Creates a sensor with the given address
     * @param address The address which references the given sensor
     * @param sensorDevice The sensor object on the hardware map to be used
     */
    public I2cColorSensor(I2cDevice sensorDevice, I2cAddr address) {
        super(sensorDevice, address);
    }

    /**
     * Creates a sensor with the given address
     * @param address The address which references the given sensor
     * @param sensorDevice The sensor object on the hardware map to be used
     */
    public I2cColorSensor(I2cDevice sensorDevice, int address) {
        this(sensorDevice, I2cAddr.create8bit(address));
    }

    //returns color
    //key:
    /* 0 - black
     * 1 - purple
     * 2 - violet/dark blue
     * 3 - blue
     * 4 - teal/light blue
     * 5 - green
     * 6 - lime
     * 7 - yellow
     * 8 - and yellow?
     * 9 - orange
     * 10 - red
     * 11 - redder red?
     * 12 - magenta
     * 13 - red but white (pink?)
     * 14 - green but white (new word?)
     * 15 - blue but white (isn't that just light blue?)
     * 16 - white (finally)
     */

    /**
     * Determines the color the sensor is currently seeing
     * @return Returns a number between 0 and 16 representing the color. See <a href="http://www.modernroboticsinc.com/Content/Images/uploaded/ColorNumber.png">color values here</a>.
     */
    public int getColorValue() {
        cache = sensorReader.read(COLOR_REG_START, COLOR_READ_LENGTH);
        return cache[0] & 0xFF;
    }

    /**
     * Determines the color the sensor is currently seeing
     * @return Returns one of 17 different types of named sensor color
     */
    @Override
    public SensorColor getColor() {
        return SensorColor.getByNumber(getColorValue());
    }

    /**
     * Determines the red value the sensor is currently seeing
     * @return A RGB red value between 0 and 255
     */
    public int getRed()
    {
        cache = sensorReader.read(COLOR_RED, COLOR_READ_LENGTH);
        return cache[0]&0xFF;
    }

    /**
     * Determines the green value the sensor is currently seeing
     * @return A RGB green value between 0 and 255
     */
    public int getGreen(){
        cache = sensorReader.read(COLOR_GREEN, COLOR_READ_LENGTH);
        return cache[0]&0xFF;

    }

    /**
     * Determines the blue value the sensor is currently seeing
     * @return A RGB blue value between 0 and 255
     */
    public int getBlue()
    {
        cache = sensorReader.read(COLOR_BLUE, COLOR_READ_LENGTH);
        return cache[0]&0xFF;
    }

    /**
     * Determines the white value the sensor is currently seeing
     * @return A white value between 0 and 255
     */
    public int getWhite()
    {
        cache = sensorReader.read(COLOR_WHITE, COLOR_READ_LENGTH);
        return cache[0]&0xFF;
    }

    /**
     * Turns the LED on the sensor either off or on
     * @param state The illumination state to set the LED to: True turns it on, False turns it off.
     */
    public void enableLed(boolean state) {
        if (state) {
            sensorReader.write8(COMMAND_REG_START, LIGHT_ON);
        } else {
            sensorReader.write8(COMMAND_REG_START, LIGHT_OFF);
        }
    }
}
