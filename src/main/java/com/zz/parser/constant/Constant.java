package com.zz.parser.constant;

import com.zz.parser.ClassParseException;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

import static com.zz.parser.Constants.*;

public abstract class Constant {

    protected byte tag;

    Constant(byte tag) {
        this.tag = tag;
    }

    public static Constant readConstant(DataInputStream file) throws IOException  {
        byte b = file.readByte(); // tag

        switch (b) {
            case CONSTANT_Utf8: return new ConstantUtf8(file);
            case CONSTANT_Integer: return new ConstantInteger(file);
            case CONSTANT_Float: return new ConstantFloat(file);
            case CONSTANT_Long: return new ConstantLong(file);
            case CONSTANT_Double: return new ConstantDouble(file);
            case CONSTANT_Class: return new ConstantClass(file);
            case CONSTANT_Fieldref: return new ConstantFieldref(file);
            case CONSTANT_String: return new ConstantString(file);
            case CONSTANT_Methodref: return new ConstantMethodref(file);
            case CONSTANT_InterfaceMethodref: return new ConstantInterfaceMethodref(file);
            case CONSTANT_NameAndType: return new ConstantNameAndType(file);
            case CONSTANT_MethodHandle_info:
            case CONSTANT_MethodType_info:
            case CONSTANT_InvokeDynamic_info:
                throw new ClassParseException("稍等一下,还没实现");
            default: throw new ClassParseException("invalid tag:" + b);
        }
    }

    public byte getTag() {
        return tag;
    }


    @Override
    public String toString() {
        return Constants.CONSTANT_NAMES[tag] + "[" + tag + "]";
    }
}
