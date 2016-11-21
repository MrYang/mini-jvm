package com.zz.parser.constant;

import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantClass extends Constant {
    private int name_index;

    public ConstantClass(DataInputStream file) throws IOException {
        this(file.readUnsignedShort());
    }

    public ConstantClass(int name_index) {
        super(Constants.CONSTANT_Class);
        this.name_index = name_index;
    }

    public String getConstantValue(ConstantPool cp) {
        Constant c = cp.getConstant(name_index, Constants.CONSTANT_Utf8);
        return ((ConstantUtf8)c).getBytes();
    }

    public int getName_index() {
        return name_index;
    }

    public void setName_index(int name_index) {
        this.name_index = name_index;
    }
}
