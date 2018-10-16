package com.hazenrobotics.commoncode.sensors;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

@SuppressWarnings("unused,WeakerAccess")
public abstract class I2cSensor  {
    protected I2cAddr address; //address in 7 bits
    protected byte[] cache; //storage of information
    protected I2cDevice sensorDevice; //legit sensor
    protected I2cDeviceSynch sensorReader; //reader to read and write
    protected int COMMAND_REG_START = 0x03;

    protected I2cSensor(I2cDevice sensorDevice, I2cAddr address) {
        this.sensorDevice = sensorDevice;
        changeAddress(address);
    }

    /**
     * Changes the sensor to use the given address
     * @param address The new address to be used
     */
    public void changeAddress(I2cAddr address) {
        this.address = address;
        if (sensorReader != null ) sensorReader.disengage();
        sensorReader = new I2cDeviceSynchImpl(sensorDevice, address, false);
        sensorReader.engage();
    }

}
