package com.zz.parser.constant;


import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantNameAndType extends Constant {

    private int name_index;
    private int signature_index;

    public ConstantNameAndType(DataInputStream file) throws IOException {
        super(Constants.CONSTANT_NameAndType);
        this.name_index = file.readUnsignedShort();
        this.signature_index = file.readUnsignedShort();
    }

    public int getNameIndex() {
        return name_index;
    }

    public int getSignatureIndex() {
        return signature_index;
    }
}
