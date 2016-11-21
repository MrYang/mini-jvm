package com.zz.parser.constant;


import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantNameAndType extends Constant {

    private int name_index;
    private int signature_index;

    public ConstantNameAndType(DataInputStream file) throws IOException {
        this(file.readUnsignedShort(), file.readUnsignedShort());
    }

    public ConstantNameAndType(int name_index, int signature_index) {
        super(Constants.CONSTANT_NameAndType);
        this.name_index = name_index;
        this.signature_index = signature_index;
    }


    public int getNameIndex() {
        return name_index;
    }

    public int getSignatureIndex() {
        return signature_index;
    }
}
