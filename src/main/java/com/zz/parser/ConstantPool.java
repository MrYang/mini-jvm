package com.zz.parser;

import com.zz.parser.constant.Constant;
import com.zz.parser.constant.ConstantClass;
import com.zz.parser.constant.ConstantString;
import com.zz.parser.constant.ConstantUtf8;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPool {
    private int constant_pool_count;
    private Constant[] constant_pool;

    ConstantPool(DataInputStream file) throws IOException {
        constant_pool_count = file.readUnsignedShort();
        constant_pool = new Constant[constant_pool_count];

        // 从1开始计数
        for (int i = 1; i < constant_pool_count; i++) {
            constant_pool[i] = Constant.readConstant(file);
            byte tag = constant_pool[i].getTag();

            // 如果是类型是Long或者是Double,数量还要减1
            if (tag == Constants.CONSTANT_Long || tag == Constants.CONSTANT_Double) {
                i++;
            }
        }
    }

    public Constant getConstant(int index) {
        if (index >= constant_pool.length || index < 0) {
            throw new ClassParseException("constant_pool index error");
        }
        return constant_pool[index];
    }

    public Constant getConstant(int index, byte tag) {
        Constant c = getConstant(index);

        if (c == null) {
            throw new ClassParseException("get constant error");
        }

        if (c.getTag() == tag) {
            return c;
        }

        throw new ClassParseException("tag error");
    }

    public String getConstantString(int index, byte tag) {
        Constant c = getConstant(index, tag);
        int idx;
        switch (tag) {
            case Constants.CONSTANT_Class:
                idx = ((ConstantClass)c).getName_index();
                break;
            case Constants.CONSTANT_String:
                idx = ((ConstantString)c).getStringIndex();
                break;
            default:
                throw new ClassParseException("getConstantString called with illegal tag " + tag);
        }

        c = getConstant(idx, Constants.CONSTANT_Utf8);
        return ((ConstantUtf8)c).getBytes();
    }

    public int length() {
        return constant_pool_count;
    }
}
