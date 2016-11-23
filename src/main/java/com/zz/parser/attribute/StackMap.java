package com.zz.parser.attribute;


import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class StackMap extends Attribute {

    private int map_length;
    private StackMapEntry[] map;

    StackMap(int name_index, int length, DataInputStream file,
             ConstantPool constant_pool) throws IOException {
        super(Constants.ATTR_STACK_MAP, name_index, length, constant_pool);

        map_length = file.readUnsignedShort();
        map = new StackMapEntry[map_length];

        for (int i = 0; i < map_length; i++) {
            map[i] = new StackMapEntry(file, constant_pool);
        }
    }
}

class StackMapEntry {
    private int byte_code_offset;
    private int number_of_locals;
    private StackMapType[] types_of_locals;
    private int number_of_stack_items;
    private StackMapType[] types_of_stack_items;
    private ConstantPool constant_pool;

    StackMapEntry(DataInputStream file, ConstantPool constant_pool) throws IOException {
        this.byte_code_offset = file.readShort();
        this.number_of_locals = file.readShort();
        this.number_of_stack_items = -1;
        this.constant_pool = constant_pool;

        types_of_locals = new StackMapType[number_of_locals];
        for (int i = 0; i < number_of_locals; i++) {
            types_of_locals[i] = new StackMapType(file, constant_pool);
        }

        number_of_stack_items = file.readShort();
        types_of_stack_items = new StackMapType[number_of_stack_items];
        for (int i = 0; i < number_of_stack_items; i++) {
            types_of_stack_items[i] = new StackMapType(file, constant_pool);
        }
    }
}

class StackMapType {
    private byte type;
    private int index = -1; // Index to CONSTANT_Class or offset
    private ConstantPool constant_pool;

    StackMapType(DataInputStream file, ConstantPool constant_pool) throws IOException {
        this.type = file.readByte();
        this.index = -1;

        if (hasIndex()) {
            this.index = file.readShort();
        }
        this.constant_pool = constant_pool;
    }

    public final boolean hasIndex() {
        return ((type == Constants.ITEM_Object) ||
                (type == Constants.ITEM_NewObject));
    }
}