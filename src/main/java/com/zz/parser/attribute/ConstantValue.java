package com.zz.parser.attribute;

import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;
import com.zz.parser.Utility;
import com.zz.parser.constant.*;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantValue extends Attribute {

    private int constantvalue_index;

    protected ConstantValue(int name_index, int length,
                            DataInputStream file, ConstantPool constant_pool) throws IOException {
        super(Constants.ATTR_CONSTANT_VALUE, name_index, length, constant_pool);
        this.constantvalue_index = file.readUnsignedShort();
    }

    public final String toString() {
        Constant c = constant_pool.getConstant(constantvalue_index);

        String buf;

        // Print constant to string depending on its type
        switch (c.getTag()) {
            case Constants.CONSTANT_Long:
                buf = "" + ((ConstantLong) c).getBytes();
                break;
            case Constants.CONSTANT_Float:
                buf = "" + ((ConstantFloat) c).getBytes();
                break;
            case Constants.CONSTANT_Double:
                buf = "" + ((ConstantDouble) c).getBytes();
                break;
            case Constants.CONSTANT_Integer:
                buf = "" + ((ConstantInteger) c).getBytes();
                break;
            case Constants.CONSTANT_String:
                int i = ((ConstantString) c).getStringIndex();
                c = constant_pool.getConstant(i, Constants.CONSTANT_Utf8);
                buf = "\"" + Utility.convertString(((ConstantUtf8) c).getBytes()) + "\"";
                break;

            default:
                throw new IllegalStateException("Type of ConstValue invalid: " + c);
        }

        return buf;
    }
}
