package com.zz.parser.constant;

import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantInvokeDynamic extends Constant {

    private int bootstrap_method_attr_index;
    private int name_and_type_index;

    public ConstantInvokeDynamic(DataInputStream file) throws IOException {
        super(Constants.CONSTANT_InvokeDynamic);
        bootstrap_method_attr_index = file.readUnsignedShort();
        name_and_type_index = file.readUnsignedShort();
    }

    public int getNameAndTypeIndex() {
        return name_and_type_index;
    }

    public int getBootstrapMethodAttrIndex() {
        return bootstrap_method_attr_index;
    }
}
