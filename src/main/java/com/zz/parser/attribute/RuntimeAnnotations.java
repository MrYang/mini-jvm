package com.zz.parser.attribute;

import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class RuntimeAnnotations extends Attribute {

    private int num_annotations;
    private Annotation[] annotations;

    RuntimeAnnotations(int name_index, int length, boolean visible, DataInputStream file, ConstantPool constant_pool) throws IOException {
        super(visible ? Constants.ATTR_RUNTIME_VISIBLE_ANNOTATIONS : Constants.ATTR_RUNTIME_INVISIBLE_ANNOTATIONS,
                name_index, length, constant_pool);
        num_annotations = file.readUnsignedShort();
        annotations = new Annotation[num_annotations];
        for (int i = 0; i < num_annotations; i++) {
            annotations[i] = new Annotation(file);
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < this.annotations.length; ++i) {
            buf.append(annotations[i].toString(constant_pool)).append(",");
        }
        return buf.toString();
    }
}


