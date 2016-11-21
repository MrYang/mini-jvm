package com.zz.parser.constant;


import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantLong extends Constant {

    private long bytes;

    public ConstantLong(long bytes) {
        super(Constants.CONSTANT_Long);
        this.bytes = bytes;
    }

    public ConstantLong(DataInputStream file) throws IOException {
        this(file.readLong());
    }

    public long getBytes() {
        return bytes;
    }
}
