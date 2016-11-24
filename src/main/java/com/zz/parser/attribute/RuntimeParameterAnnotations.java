package com.zz.parser.attribute;

import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class RuntimeParameterAnnotations extends Attribute {

    private int num_parameters;
    private ParameterAnnotation[] parameterAnnotations;
    private ConstantPool constant_pool;

    public RuntimeParameterAnnotations(int name_index, int length, boolean visible,
                                       DataInputStream file, ConstantPool constant_pool) throws IOException {
        super(visible ? Constants.ATTR_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS : Constants.ATTR_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS,
                name_index, length, constant_pool);

        num_parameters = file.readUnsignedByte();
        parameterAnnotations = new ParameterAnnotation[num_parameters];
        for (int i = 0; i < num_parameters; i++) {
            parameterAnnotations[0] = new ParameterAnnotation(file);
        }

        this.constant_pool = constant_pool;
    }
}

class ParameterAnnotation {
    private int num_annotations;
    private Annotation[] annotations;

    public ParameterAnnotation(DataInputStream file) throws IOException {
        num_annotations = file.readUnsignedShort();
        annotations = new Annotation[num_annotations];
        for (int i = 0; i < num_annotations; i++) {
            annotations[i] = new Annotation(file);
        }
    }
}