package com.zz.parser;

import com.zz.parser.attribute.Attribute;
import com.zz.parser.attribute.Code;
import com.zz.parser.attribute.ExceptionTable;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class Method extends FieldOrMethod {

    protected Method(DataInputStream file, ConstantPool constant_pool) throws IOException {
        super(file, constant_pool);
    }

    public Code getCode() {
        Optional<Attribute> optional =  Arrays.stream(attributes)
                .filter(attribute -> attribute instanceof Code)
                .findFirst();
        if (optional.isPresent()) {
            return (Code) optional.get();
        }

        return null;
    }

    public ExceptionTable getExceptionTable() {
        Optional<Attribute> optional =  Arrays.stream(attributes)
                .filter(attribute -> attribute instanceof ExceptionTable)
                .findFirst();
        if (optional.isPresent()) {
            return (ExceptionTable) optional.get();
        }

        return null;
    }
}
