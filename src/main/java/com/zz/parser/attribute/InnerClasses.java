package com.zz.parser.attribute;

import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class InnerClasses extends Attribute {

    private InnerClass[] inner_classes;
    private int number_of_classes;

    InnerClasses(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
        super(Constants.ATTR_INNER_CLASSES, name_index, length, constant_pool);

        number_of_classes = file.readUnsignedShort();
        inner_classes = new InnerClass[number_of_classes];
        for (int i = 0; i < number_of_classes; i++) {
            inner_classes[i] = new InnerClass(file);
        }
    }
}

class InnerClass {

    private int inner_class_index;
    private int outer_class_index;
    private int inner_name_index;
    private int inner_access_flags;

    InnerClass(DataInputStream file) throws IOException {
        this.inner_class_index = file.readUnsignedShort();
        this.outer_class_index = file.readUnsignedShort();
        this.inner_name_index = file.readUnsignedShort();
        this.inner_access_flags = file.readUnsignedShort();
    }
}
