package com.zz.parser.constant;


import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantInteger extends Constant {

    private int bytes;

    public ConstantInteger(DataInputStream file) throws IOException {
        this(file.readInt());
    }

    public ConstantInteger(int bytes) {
        super(Constants.CONSTANT_Integer);
        this.bytes = bytes;
    }

    public int getBytes() {
        return bytes;
    }
}
