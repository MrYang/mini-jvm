package com.zz.parser.constant;

import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantMethodref extends ConstantCP {

    public ConstantMethodref(DataInputStream file) throws IOException {
        super(Constants.CONSTANT_Methodref, file);
    }
}
