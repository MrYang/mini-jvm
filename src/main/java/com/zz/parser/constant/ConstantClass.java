package com.zz.parser.constant;

import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantClass extends Constant {
    private int name_index;

    public ConstantClass(DataInputStream file) throws IOException {
        super(Constants.CONSTANT_Class);
        this.name_index = file.readUnsignedShort();
    }

    public String getConstantValue(ConstantPool constant_pool) {
        return constant_pool.constantToString(name_index, Constants.CONSTANT_Utf8);
    }

    public int getNameIndex() {
        return name_index;
    }

    public void setName_index(int name_index) {
        this.name_index = name_index;
    }
}
