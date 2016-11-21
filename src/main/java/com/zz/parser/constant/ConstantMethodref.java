package com.zz.parser.constant;

import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantMethodref extends ConstantCP {

    public ConstantMethodref(DataInputStream file) throws IOException {
        super(Constants.CONSTANT_Methodref, file);
    }

    public ConstantMethodref(int class_index, int name_and_type_index) {
        super(Constants.CONSTANT_Methodref, class_index, name_and_type_index);
    }
}
