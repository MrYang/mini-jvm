package com.zz.parser.constant;


import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantString extends Constant {

    private int string_index;

    ConstantString(DataInputStream file) throws IOException {
        this(file.readUnsignedShort());
    }

    public ConstantString(int string_index) {
        super(Constants.CONSTANT_String);
        this.string_index = string_index;
    }

    public int getStringIndex() {
        return string_index;
    }
}
