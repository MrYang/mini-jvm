package com.zz.parser.attribute;


import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class LocalVariableTypeTable extends Attribute {

    private int local_variable_type_table_length;
    private LocalVariable[] local_variable_type_table;

    public LocalVariableTypeTable(int name_index, int length,
                                  ConstantPool constant_pool) {
        super(Constants.ATTR_LOCAL_VARIABLE_TYPE_TABLE, name_index, length, constant_pool);
    }

    LocalVariableTypeTable(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(name_index, length, constant_pool);

        local_variable_type_table_length = file.readUnsignedShort();
        local_variable_type_table = new LocalVariable[local_variable_type_table_length];

        for (int i = 0; i < local_variable_type_table_length; i++) {
            local_variable_type_table[i] = new LocalVariable(file, constant_pool);
        }
    }

    public final String toString() {
        StringBuilder buf = new StringBuilder("LocalVariableTypeTable:\n");

        for (int i = 0; i < local_variable_type_table_length; i++) {
            buf.append(local_variable_type_table[i].toString());

            if (i < local_variable_type_table_length - 1) {
                buf.append('\n');
            }
        }

        return buf.toString();
    }
}
