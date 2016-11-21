package com.zz.parser;

import java.io.DataInputStream;
import java.io.IOException;

public class Field extends FieldOrMethod {

    protected Field(DataInputStream file, ConstantPool constant_pool) throws IOException {
        super(file, constant_pool);
    }


}
