package com.zz.parser.constant;


import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantInteger extends Constant {

    private int bytes;

    public ConstantInteger(DataInputStream file) throws IOException {
        super(Constants.CONSTANT_Integer);
        this.bytes = file.readInt();
    }

    public int getBytes() {
        return bytes;
    }
}
