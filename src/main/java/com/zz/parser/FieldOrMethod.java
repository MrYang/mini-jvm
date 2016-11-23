package com.zz.parser;

import com.zz.parser.attribute.Attribute;

import java.io.DataInputStream;
import java.io.IOException;

public class FieldOrMethod extends AccessFlags {

    protected int name_index;
    protected int signature_index;
    protected int attributes_count;
    protected Attribute[] attributes;
    protected ConstantPool constant_pool;

    protected FieldOrMethod(DataInputStream file, ConstantPool constant_pool) throws IOException {
        access_flags = file.readUnsignedShort();
        name_index = file.readUnsignedShort();
        signature_index = file.readUnsignedShort();
        this.constant_pool = constant_pool;
        attributes_count = file.readUnsignedShort();
        attributes = new Attribute[attributes_count];

        for (int i = 0; i < attributes_count; i++) {
            attributes[i] = Attribute.readAttribute(file, constant_pool);
        }
    }

    public String getName() {
        return constant_pool.constantToString(name_index, Constants.CONSTANT_Utf8);
    }

    public String getSignature() {
        return constant_pool.constantToString(signature_index, Constants.CONSTANT_Utf8);
    }

}
