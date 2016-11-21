package com.zz.parser.attribute;


import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class StackMap extends Attribute {

    private int map_length;
    private StackMapEntry[] map;


    public StackMap(int name_index, int length,
                    ConstantPool constant_pool) {
        super(Constants.ATTR_STACK_MAP, name_index, length, constant_pool);

    }

    /**
     * Construct object from file stream.
     *
     * @param name_index    Index of name
     * @param length        Content length in bytes
     * @param file          Input stream
     * @param constant_pool Array of constants
     * @throws IOException
     */
    StackMap(int name_index, int length, DataInputStream file,
             ConstantPool constant_pool) throws IOException {
        this(name_index, length, constant_pool);

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
        this(file.readShort(), file.readShort(), -1, constant_pool);

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

    public StackMapEntry(int byte_code_offset, int number_of_locals,
                         int number_of_stack_items,
                         ConstantPool constant_pool) {
        this.byte_code_offset = byte_code_offset;
        this.number_of_locals = number_of_locals;
        this.number_of_stack_items = number_of_stack_items;
        this.constant_pool = constant_pool;
    }
}

class StackMapType {
    private byte type;
    private int index = -1; // Index to CONSTANT_Class or offset
    private ConstantPool constant_pool;

    StackMapType(DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(file.readByte(), -1);

        if (hasIndex()) {
            this.index = file.readShort();
        }
        this.constant_pool = constant_pool;
    }

    /**
     * @param type  type tag as defined in the Constants interface
     * @param index index to constant pool, or byte code offset
     */
    public StackMapType(byte type, int index) {
        this.type = type;
        this.index = index;
    }

    public final boolean hasIndex() {
        return ((type == Constants.ITEM_Object) ||
                (type == Constants.ITEM_NewObject));
    }
}