package com.zz.parser.attribute;

import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;
import com.zz.parser.Utility;

import java.io.DataInputStream;
import java.io.IOException;

public class LocalVariable {
    private int start_pc;        // Range in which the variable is valid
    private int length;
    private int name_index;      // Index in constant pool of variable name
    private int signature_index; // Index of variable signature
    private int index;

    private ConstantPool constant_pool;

    LocalVariable(DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(file.readUnsignedShort(), file.readUnsignedShort(),
                file.readUnsignedShort(), file.readUnsignedShort(),
                file.readUnsignedShort(), constant_pool);
    }

    LocalVariable(int start_pc, int length, int name_index,
                  int signature_index, int index,
                  ConstantPool constant_pool) {
        this.start_pc = start_pc;
        this.length = length;
        this.name_index = name_index;
        this.signature_index = signature_index;
        this.index = index;
        this.constant_pool = constant_pool;
    }

    public int getStartPc() {
        return start_pc;
    }

    public int getLength() {
        return length;
    }

    public int getNameIndex() {
        return name_index;
    }

    public int getSignatureIndex() {
        return signature_index;
    }

    public String getSignature() {
        return constant_pool.getConstantString(signature_index, Constants.CONSTANT_Utf8);
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return constant_pool.getConstantString(name_index, Constants.CONSTANT_Utf8);
    }

    public final String toString() {
        String name = getName(), signature = Utility.signatureToString(getSignature());

        return "LocalVariable(start_pc = " + start_pc + ", length = " + length +
                ", index = " + index + ":" + signature + " " + name + ")";
    }
}
