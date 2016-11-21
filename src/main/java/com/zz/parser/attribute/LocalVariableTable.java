package com.zz.parser.attribute;


import com.sun.org.apache.bcel.internal.Constants;
import com.zz.parser.ConstantPool;

import java.io.DataInputStream;
import java.io.IOException;

public class LocalVariableTable extends Attribute {

    private int local_variable_table_length; // Table of local
    private LocalVariable[] local_variable_table;        // variables

    public LocalVariableTable(int name_index, int length,
                              ConstantPool constant_pool) {
        super(Constants.ATTR_LOCAL_VARIABLE_TABLE, name_index, length, constant_pool);
    }


    LocalVariableTable(int name_index, int length, DataInputStream file,
                       ConstantPool constant_pool) throws IOException {
        this(name_index, length, constant_pool);

        local_variable_table_length = file.readUnsignedShort();
        local_variable_table = new LocalVariable[local_variable_table_length];

        for (int i = 0; i < local_variable_table_length; i++) {
            local_variable_table[i] = new LocalVariable(file, constant_pool);
        }
    }
}