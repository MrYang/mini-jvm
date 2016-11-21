package com.zz.parser.attribute;

import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantValue extends Attribute {

    private int constantvalue_index;

    protected ConstantValue(int name_index, int length,
                            DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(name_index, length, file.readUnsignedShort(), constant_pool);
    }

    public ConstantValue(int name_index, int length,
                         int constantvalue_index, ConstantPool constant_pool) throws IOException {
        super(Constants.ATTR_CONSTANT_VALUE, name_index, length, constant_pool);
        this.constantvalue_index = constantvalue_index;
    }
}
