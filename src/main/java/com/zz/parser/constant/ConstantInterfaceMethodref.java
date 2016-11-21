package com.zz.parser.constant;

import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantInterfaceMethodref extends ConstantCP {

    public ConstantInterfaceMethodref(DataInputStream file) throws IOException {
        super(Constants.CONSTANT_InterfaceMethodref, file);
    }

    public ConstantInterfaceMethodref(int class_index, int name_and_type_index) {
        super(Constants.CONSTANT_InterfaceMethodref, class_index, name_and_type_index);
    }
}
