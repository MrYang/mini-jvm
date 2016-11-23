package com.zz.parser.attribute;

import com.sun.org.apache.bcel.internal.classfile.Utility;
import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;
import com.zz.parser.constant.ConstantUtf8;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

public class Unknown extends Attribute {

    private byte[] bytes;
    private String name;

    private static HashMap<String, Unknown> unknown_attributes = new HashMap<>();


    public Unknown(int name_index, int length, ConstantPool constant_pool) {
        super(Constants.ATTR_UNKNOWN, name_index, length, constant_pool);

        name = ((ConstantUtf8) constant_pool.getConstant(name_index, Constants.CONSTANT_Utf8)).getBytes();
        unknown_attributes.put(name, this);
    }

    Unknown(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(name_index, length, constant_pool);

        if (length > 0) {
            bytes = new byte[length];
            file.readFully(bytes);
        }
    }
    public final String toString() {
        if(length == 0 || bytes == null) {
            return "(Unknown attribute " + name + ")";
        }

        String hex;
        if(length > 10) {
            byte[] tmp = new byte[10];
            System.arraycopy(bytes, 0, tmp, 0, 10);
            hex = Utility.toHexString(tmp) + "... (truncated)";
        }
        else
            hex = Utility.toHexString(bytes);

        return "(Unknown attribute " + name + ": " + hex + ")";
    }
}
