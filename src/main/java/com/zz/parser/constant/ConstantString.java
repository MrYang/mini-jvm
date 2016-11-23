package com.zz.parser.constant;


import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantString extends Constant {

    private int string_index;

    ConstantString(DataInputStream file) throws IOException {
        super(Constants.CONSTANT_String);
        string_index = file.readUnsignedShort();
    }

    public int getStringIndex() {
        return string_index;
    }
}
