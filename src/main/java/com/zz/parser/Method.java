package com.zz.parser;

import com.zz.parser.attribute.Attribute;
import com.zz.parser.attribute.Code;
import com.zz.parser.attribute.ExceptionTable;
import com.zz.parser.attribute.LineNumberTable;
import com.zz.parser.attribute.LocalVariableTable;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class Method extends FieldOrMethod {

    protected Method(DataInputStream file, ConstantPool constant_pool) throws IOException {
        super(file, constant_pool);
    }

    public Code getCode() {
        Optional<Attribute> optional = Arrays.stream(attributes)
                .filter(attribute -> attribute instanceof Code)
                .findFirst();
        if (optional.isPresent()) {
            return (Code) optional.get();
        }

        return null;
    }

    public ExceptionTable getExceptionTable() {
        Optional<Attribute> optional = Arrays.stream(attributes)
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
        String signature = constant_pool.constantToString(signature_index, Constants.CONSTANT_Utf8);
        String name = constant_pool.constantToString(name_index, Constants.CONSTANT_Utf8);
        StringBuilder buf = new StringBuilder(Utility.methodSignatureToString(signature, name, access));

        for(int i=0; i < attributes_count; i++) {
            Attribute a = attributes[i];

            if(!((a instanceof Code) || (a instanceof ExceptionTable))) {
                buf.append(" [" + a.toString() + "]");
            }
        }

        ExceptionTable e = getExceptionTable();
        if(e != null) {
            String str = e.toString();
            if(!str.equals("")) {
                buf.append("\n\t\tthrows " + str);
            }
        }

        return buf.toString();
    }
}
