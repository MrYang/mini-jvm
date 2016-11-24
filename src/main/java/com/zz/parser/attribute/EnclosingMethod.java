package com.zz.parser.attribute;

import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class EnclosingMethod extends Attribute {

    private int class_index;
    private int method_index;
    private ConstantPool constant_pool;

    public EnclosingMethod(int name_index, int length, DataInputStream file,
                           ConstantPool constant_pool) throws IOException {
        super(Constants.ATTR_ENCLOSING_METHOD, name_index, length, constant_pool);

        class_index = file.readUnsignedShort();
        method_index = file.readUnsignedShort();
        this.constant_pool = constant_pool;
    }

    public String toString() {
        String className = constant_pool.constantToString(class_index, Constants.CONSTANT_Class);
        String methodName = constant_pool.constantToString(method_index, Constants.CONSTANT_NameAndType);

        return "class:" + className + ",method:" + methodName;
    }
}
