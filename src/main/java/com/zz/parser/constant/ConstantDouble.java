package com.zz.parser.constant;

import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantDouble extends Constant {

    private double bytes;

    public ConstantDouble(double bytes) {
        super(Constants.CONSTANT_Double);
        this.bytes = bytes;
    }

    public ConstantDouble(DataInputStream file) throws IOException {
        this(file.readDouble());
    }

    public double getBytes() {
        return bytes;
    }
}
