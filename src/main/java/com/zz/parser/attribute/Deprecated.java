package com.zz.parser.attribute;


import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class Deprecated extends Attribute {

    private byte[] bytes;

    Deprecated(int name_index, int length, DataInputStream file,
               ConstantPool constant_pool) throws IOException {
        super(Constants.ATTR_DEPRECATED, name_index, length, constant_pool);

        if (length > 0) {
            bytes = new byte[length];
            file.readFully(bytes);
            System.err.println("Deprecated attribute with length > 0");
        }
    }

    public byte[] getBytes() {
        return bytes;
    }
}
