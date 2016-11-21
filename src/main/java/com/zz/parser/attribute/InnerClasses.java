package com.zz.parser.attribute;

import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class InnerClasses extends Attribute {

    private InnerClass[] inner_classes;
    private int number_of_classes;

    InnerClasses(int name_index, int length, ConstantPool constant_pool) {
        super(Constants.ATTR_INNER_CLASSES, name_index, length, constant_pool);
    }

    InnerClasses(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(name_index, length, constant_pool);

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
        this(file.readUnsignedShort(), file.readUnsignedShort(),
                file.readUnsignedShort(), file.readUnsignedShort());
    }

    public InnerClass(int inner_class_index, int outer_class_index,
                      int inner_name_index, int inner_access_flags) {
        this.inner_class_index = inner_class_index;
        this.outer_class_index = outer_class_index;
        this.inner_name_index = inner_name_index;
        this.inner_access_flags = inner_access_flags;
    }
}
