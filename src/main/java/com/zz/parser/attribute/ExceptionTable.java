package com.zz.parser.attribute;


import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;
import com.zz.parser.Utility;

import java.io.DataInputStream;
import java.io.IOException;

public class ExceptionTable extends Attribute {

    private int number_of_exceptions;  // Table of indices into
    private int[] exception_index_table; // constant pool

    ExceptionTable(int name_index, int length, DataInputStream file,
                   ConstantPool constant_pool) throws IOException {
        super(Constants.ATTR_EXCEPTIONS, name_index, length, constant_pool);

        number_of_exceptions = file.readUnsignedShort();
        exception_index_table = new int[number_of_exceptions];

        for (int i = 0; i < number_of_exceptions; i++) {
            exception_index_table[i] = file.readUnsignedShort();
        }
    }

    public String[] getExceptionNames() {
        String[] names = new String[number_of_exceptions];
        for (int i = 0; i < number_of_exceptions; i++) {
            names[i] = constant_pool.getConstantString(exception_index_table[i], Constants.CONSTANT_Class)
                    .replace('/', '.');
        }

        return names;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("");
        String str;

        for (int i = 0; i < number_of_exceptions; i++) {
            str = constant_pool.getConstantString(exception_index_table[i], Constants.CONSTANT_Class);
            buf.append(Utility.compactClassName(str, false));

            if (i < number_of_exceptions - 1) {
                buf.append(", ");
            }
        }

        return buf.toString();
    }
}
