package com.zz.parser.attribute;

import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class SourceDebugExtension extends Attribute {

    private String bytes;

    public SourceDebugExtension(int name_index, int length, DataInputStream file,
                           ConstantPool constant_pool) throws IOException {
        super(Constants.ATTR_SOURCE_DEBUG_EXTENSION, name_index, length, constant_pool);
        bytes = file.readUTF();
    }

    public String getBytes() {
        return bytes;
    }

}
