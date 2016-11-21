package com.zz.parser;

import com.zz.parser.attribute.*;
import com.zz.parser.constant.ConstantUtf8;

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

    public LocalVariableTable getLocalVariableTable() {
        Code code = getCode();
        if (code != null) {
            return code.getLocalVariableTable();
        }

        return null;
    }

    public LineNumberTable getLineNumberTable() {
        Code code = getCode();
        if (code != null) {
            return code.getLineNumberTable();
        }

        return null;
    }

    @Override
    public String toString() {
        String access = Utility.accessToString(access_flags);
        ConstantUtf8 c = (ConstantUtf8) constant_pool.getConstant(signature_index, Constants.CONSTANT_Utf8);
        String signature = c.getBytes();

        return "";
    }
}
