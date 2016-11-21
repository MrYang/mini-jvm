package com.zz.parser.attribute;


import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class Signature extends Attribute {

    private int signature_index;

    Signature(int name_index, int length, DataInputStream file,
              ConstantPool constant_pool) throws IOException {
        this(name_index, length, file.readUnsignedShort(), constant_pool);
    }

    public Signature(int name_index, int length, int signature_index,
                     ConstantPool constant_pool) {
        super(Constants.ATTR_SIGNATURE, name_index, length, constant_pool);
        this.signature_index = signature_index;
    }
}
