package com.zz.parser.constant;

import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantFloat extends Constant {

    private float bytes;

    public ConstantFloat(DataInputStream file) throws IOException {
        super(Constants.CONSTANT_Float);
        this.bytes = file.readFloat();
    }

    public float getBytes() {
        return bytes;
    }
}
