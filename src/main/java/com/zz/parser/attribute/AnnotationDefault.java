package com.zz.parser.attribute;

import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class AnnotationDefault extends Attribute {

    private Annotation.element_value element_value;

    public AnnotationDefault(int name_index, int length, DataInputStream file,
                                ConstantPool constant_pool) throws IOException {
        super(Constants.ATTR_ANNOTATION_DEFAULT, name_index, length, constant_pool);
        element_value = Annotation.element_value.read(file);
    }

}