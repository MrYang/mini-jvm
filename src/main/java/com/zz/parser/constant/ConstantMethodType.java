package com.zz.parser.constant;

import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantMethodType extends Constant {

    private int descriptor_index;

    public ConstantMethodType(DataInputStream file) throws IOException {
        super(Constants.CONSTANT_MethodType);
        descriptor_index = file.readUnsignedShort();
    }

    public int getDescriptorIndex() {
        return descriptor_index;
    }
}
