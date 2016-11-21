package com.zz.parser.attribute;


import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;
import com.zz.parser.constant.ConstantUtf8;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class Attribute {

    protected int name_index;
    protected int length;
    protected byte tag;
    protected ConstantPool constant_pool;

    protected Attribute(byte tag, int name_index, int length, ConstantPool constant_pool) {
        this.tag = tag;
        this.name_index = name_index;
        this.length = length;
        this.constant_pool = constant_pool;
    }

    public static Attribute readAttribute(DataInputStream file, ConstantPool constant_pool) throws IOException {
        byte tag = Constants.ATTR_UNKNOWN;
        int name_index = file.readUnsignedShort();
        ConstantUtf8 c = (ConstantUtf8) constant_pool.getConstant(name_index, Constants.CONSTANT_Utf8);
        String name = c.getBytes();

        int length = file.readInt();

        // Compare strings to find known attribute
        for (byte i = 0; i < Constants.KNOWN_ATTRIBUTES; i++) {
            if (name.equals(Constants.ATTRIBUTE_NAMES[i])) {
                tag = i;
                break;
            }
        }

        // Call proper constructor, depending on `tag'
        switch (tag) {
            case Constants.ATTR_UNKNOWN:
                return new Unknown(name_index, length, file, constant_pool);
            case Constants.ATTR_CONSTANT_VALUE:
                return new ConstantValue(name_index, length, file, constant_pool);
            case Constants.ATTR_SOURCE_FILE:
                return new SourceFile(name_index, length, file, constant_pool);
            case Constants.ATTR_CODE:
                return new Code(name_index, length, file, constant_pool);
            case Constants.ATTR_EXCEPTIONS:
                return new ExceptionTable(name_index, length, file, constant_pool);
            case Constants.ATTR_LINE_NUMBER_TABLE:
                return new LineNumberTable(name_index, length, file, constant_pool);
            case Constants.ATTR_LOCAL_VARIABLE_TABLE:
                return new LocalVariableTable(name_index, length, file, constant_pool);
            case Constants.ATTR_LOCAL_VARIABLE_TYPE_TABLE:
                return new LocalVariableTypeTable(name_index, length, file, constant_pool);
            case Constants.ATTR_INNER_CLASSES:
                return new InnerClasses(name_index, length, file, constant_pool);
            case Constants.ATTR_SYNTHETIC:
                return new Synthetic(name_index, length, file, constant_pool);
            case Constants.ATTR_DEPRECATED:
                return new Deprecated(name_index, length, file, constant_pool);
            case Constants.ATTR_PMG:
                return new PMGClass(name_index, length, file, constant_pool);
            case Constants.ATTR_SIGNATURE:
                return new Signature(name_index, length, file, constant_pool);
            case Constants.ATTR_STACK_MAP:
                return new StackMap(name_index, length, file, constant_pool);
            default:
                throw new IllegalStateException("Ooops! default case reached.");
        }
    }
}
