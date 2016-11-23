package com.zz.parser;

import com.zz.parser.constant.*;

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
                idx = ((ConstantClass) c).getNameIndex();
                break;
            case Constants.CONSTANT_String:
                idx = ((ConstantString) c).getStringIndex();
                break;
            default:
                throw new ClassParseException("getConstantString called with illegal tag " + tag);
        }

        c = getConstant(idx, Constants.CONSTANT_Utf8);
        return ((ConstantUtf8) c).getBytes();
    }

    private String constantToString(Constant c) throws ClassParseException {
        String str;
        int idx;
        byte tag = c.getTag();

        switch (tag) {
            case Constants.CONSTANT_Class:
                idx = ((ConstantClass) c).getNameIndex();
                c = getConstant(idx, Constants.CONSTANT_Utf8);
                str = Utility.compactClassName(((ConstantUtf8) c).getBytes(), false);
                break;
            case Constants.CONSTANT_String:
                idx = ((ConstantString) c).getStringIndex();
                c = getConstant(idx, Constants.CONSTANT_Utf8);
                str = "\"" + escape(((ConstantUtf8) c).getBytes()) + "\"";
                break;
            case Constants.CONSTANT_Utf8:
                str = ((ConstantUtf8) c).getBytes();
                break;
            case Constants.CONSTANT_Double:
                str = "" + ((ConstantDouble) c).getBytes();
                break;
            case Constants.CONSTANT_Float:
                str = "" + ((ConstantFloat) c).getBytes();
                break;
            case Constants.CONSTANT_Long:
                str = "" + ((ConstantLong) c).getBytes();
                break;
            case Constants.CONSTANT_Integer:
                str = "" + ((ConstantInteger) c).getBytes();
                break;
            case Constants.CONSTANT_NameAndType:
                str = constantToString(((ConstantNameAndType) c).getNameIndex(), Constants.CONSTANT_Utf8) + ":" +
                        constantToString(((ConstantNameAndType) c).getSignatureIndex(), Constants.CONSTANT_Utf8);
                break;
            case Constants.CONSTANT_InterfaceMethodref:
            case Constants.CONSTANT_Methodref:
            case Constants.CONSTANT_Fieldref:
                str = constantToString(((ConstantCP) c).getClassIndex(), Constants.CONSTANT_Class) + "." +
                        constantToString(((ConstantCP) c).getNameAndTypeIndex(), Constants.CONSTANT_NameAndType);
                break;
            case Constants.CONSTANT_MethodHandle:
                ConstantMethodHandler constantMethodHandler = (ConstantMethodHandler) c;
                int reference_kind = constantMethodHandler.getReferenceKind();
                int reference_index = constantMethodHandler.getReferenceIndex();
                switch (reference_kind) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        str = constantToString(reference_index, Constants.CONSTANT_Fieldref);
                        break;
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        str = constantToString(reference_index, Constants.CONSTANT_Methodref);
                        break;
                    case 9:
                        str = constantToString(reference_index, Constants.CONSTANT_InterfaceMethodref);
                        break;
                    default:
                        throw new ClassParseException("invalid reference kind:" + reference_kind);
                }
                break;
            case Constants.CONSTANT_MethodType:
                str = constantToString(((ConstantMethodType) c).getDescriptorIndex(), Constants.CONSTANT_Utf8);
                break;
            case Constants.CONSTANT_InvokeDynamic:
                ConstantInvokeDynamic constantInvokeDynamic = (ConstantInvokeDynamic) c;
                int attrIdx = constantInvokeDynamic.getBootstrapMethodAttrIndex();
                str = "bootstrap_methods[" + attrIdx + "]:" +
                        constantToString(constantInvokeDynamic.getNameAndTypeIndex(), Constants.CONSTANT_NameAndType);
                break;

            default:
                throw new ClassParseException("Unknown constant type " + tag);
        }

        return str;
    }

    public String constantToString(int index, byte tag) throws ClassParseException {
        Constant c = getConstant(index, tag);
        return constantToString(c);
    }

    public int length() {
        return constant_pool_count;
    }

    private String escape(String str) {
        int len = str.length();
        StringBuilder buf = new StringBuilder(len + 5);
        char[] ch = str.toCharArray();

        for (int i = 0; i < len; i++) {
            switch (ch[i]) {
                case '\n':
                    buf.append("\\n");
                    break;
                case '\r':
                    buf.append("\\r");
                    break;
                case '\t':
                    buf.append("\\t");
                    break;
                case '\b':
                    buf.append("\\b");
                    break;
                case '"':
                    buf.append("\\\"");
                    break;
                default:
                    buf.append(ch[i]);
            }
        }

        return buf.toString();
    }

    public int getLength() {
        return constant_pool_count;
    }
}
