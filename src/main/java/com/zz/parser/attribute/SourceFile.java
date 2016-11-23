package com.zz.parser.attribute;


import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class SourceFile extends Attribute {

    private int sourcefile_index;

    SourceFile(int name_index, int length, DataInputStream file,
               ConstantPool constant_pool) throws IOException {
        super(Constants.ATTR_SOURCE_FILE, name_index, length, constant_pool);
        this.sourcefile_index = file.readUnsignedShort();
    }

    public final String getSourceFileName() {
        return constant_pool.constantToString(sourcefile_index, Constants.CONSTANT_Utf8);
    }

    public final String toString() {
        return "SourceFile(" + getSourceFileName() + ")";
    }
}
