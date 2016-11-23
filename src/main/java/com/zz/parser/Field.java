package com.zz.parser;

import com.zz.parser.attribute.Attribute;
import com.zz.parser.attribute.ConstantValue;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class Field extends FieldOrMethod {

    protected Field(DataInputStream file, ConstantPool constant_pool) throws IOException {
        super(file, constant_pool);
    }


    public ConstantValue getConstantValue() {
        Optional<Attribute> optional = Arrays.stream(attributes)
                .filter(attribute -> attribute.getTag() == Constants.ATTR_CONSTANT_VALUE)
                .findFirst();

        if (optional.isPresent()) {
            return (ConstantValue) optional.get();
        }

        return null;
    }

    public String toString() {
        String access = Utility.accessToString(access_flags);
        String signature = Utility.signatureToString(getSignature());
        String name = getName();
        StringBuilder buf = new StringBuilder(access + " " + signature + " " + name);
        ConstantValue cv = getConstantValue();

        if (cv != null) {
            buf.append(" = " + cv);
        }

        for (int i = 0; i < attributes_count; i++) {
            Attribute a = attributes[i];

            if (!(a instanceof ConstantValue)) {
                buf.append(" [" + a.toString() + "]");
            }
        }

        return buf.toString();
    }
}
