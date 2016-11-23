package com.zz.parser.constant;


import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantFieldref extends ConstantCP {

    public ConstantFieldref(DataInputStream file) throws IOException {
        super(Constants.CONSTANT_Fieldref, file);
    }
}
