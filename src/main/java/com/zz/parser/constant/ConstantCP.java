package com.zz.parser.constant;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class ConstantCP extends Constant {

    protected int class_index, name_and_type_index;

    public ConstantCP(byte tag, DataInputStream file) throws IOException {
        super(tag);
        this.class_index = file.readUnsignedShort();
        this.name_and_type_index = file.readUnsignedShort();
    }

    public int getClassIndex() {
        return class_index;
    }

    public int getNameAndTypeIndex() {
        return name_and_type_index;
    }
}
