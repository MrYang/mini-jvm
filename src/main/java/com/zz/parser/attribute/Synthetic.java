package com.zz.parser.attribute;


import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class Synthetic extends Attribute {

    private byte[] bytes;

    public Synthetic(int name_index, int length,
                     ConstantPool constant_pool) {
        super(Constants.ATTR_SYNTHETIC, name_index, length, constant_pool);
    }

    Synthetic(int name_index, int length, DataInputStream file,
              ConstantPool constant_pool) throws IOException {
        this(name_index, length, constant_pool);

        if (length > 0) {
            bytes = new byte[length];
            file.readFully(bytes);
            System.err.println("Synthetic attribute with length > 0");
        }
    }
}