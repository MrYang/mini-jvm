package com.zz.parser.attribute;


import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ExceptionTable extends Attribute {

    private int number_of_exceptions;  // Table of indices into
    private int[] exception_index_table; // constant pool

    public ExceptionTable(int name_index, int length,
                          ConstantPool constant_pool) {
        super(Constants.ATTR_EXCEPTIONS, name_index, length, constant_pool);
    }

    ExceptionTable(int name_index, int length, DataInputStream file,
                   ConstantPool constant_pool) throws IOException {
        this(name_index, length, constant_pool);

        number_of_exceptions = file.readUnsignedShort();
        exception_index_table = new int[number_of_exceptions];

        for (int i = 0; i < number_of_exceptions; i++) {
            exception_index_table[i] = file.readUnsignedShort();
        }
    }
}
