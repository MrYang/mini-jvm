package com.zz.parser.attribute;

import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;
import com.zz.parser.Utility;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class Code extends Attribute {

    private int max_stack;
    private int max_locals;
    private int code_length;
    private byte[] code;

    private int exception_table_length;
    private CodeException[] exception_table;
    private int attributes_count;
    private Attribute[] attributes;

    Code(int name_index, int length, DataInputStream file,
         ConstantPool constant_pool) throws IOException {

        this(name_index, length, file.readUnsignedShort(), file.readUnsignedShort(), constant_pool);

        code_length = file.readInt();
        code = new byte[code_length];
        file.readFully(code);

        exception_table_length = file.readUnsignedShort();
        exception_table = new CodeException[exception_table_length];

        for (int i = 0; i < exception_table_length; i++) {
            exception_table[i] = new CodeException(file);
        }
        attributes_count = file.readUnsignedShort();
        attributes = new Attribute[attributes_count];
        for (int i = 0; i < attributes_count; i++) {
            attributes[i] = Attribute.readAttribute(file, constant_pool);
        }

        this.length = length;
    }

    public Code(int name_index, int length,
                int max_stack, int max_locals,
                ConstantPool constant_pool) {
        super(Constants.ATTR_CODE, name_index, length, constant_pool);

        this.max_stack = max_stack;
        this.max_locals = max_locals;
    }

    public LineNumberTable getLineNumberTable() {
        Optional<Attribute> optional = Arrays.stream(attributes)
                .filter(attribute -> attribute instanceof LineNumberTable)
                .findFirst();
        if (optional.isPresent()) {
            return (LineNumberTable) optional.get();
        }

        return null;
    }

    public LocalVariableTable getLocalVariableTable() {
        Optional<Attribute> optional = Arrays.stream(attributes)
                .filter(attribute -> attribute instanceof LocalVariableTable)
                .findFirst();
        if (optional.isPresent()) {
            return (LocalVariableTable) optional.get();
        }

        return null;
    }

    public int getMaxStack() {
        return max_stack;
    }

    public int getMaxLocals() {
        return max_locals;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("Code(max_stack = " + max_stack +
                ", max_locals = " + max_locals +
                ", code_length = " + code_length + ")\n" +
                Utility.codeToString(code, constant_pool, 0, -1, true));

        if (exception_table_length > 0) {
            buf.append("\nException handler(s) = \n" + "From\tTo\tHandler\tType\n");

            for (int i = 0; i < exception_table_length; i++) {
                buf.append(exception_table[i].toString(constant_pool, true) + "\n");
            }
        }

        if (attributes_count > 0) {
            buf.append("\nAttribute(s) = \n");

            for (int i = 0; i < attributes_count; i++) {
                buf.append(attributes[i].toString() + "\n");
            }
        }

        return buf.toString();
    }
}

class CodeException {

    private int start_pc;
    private int end_pc;
    private int handler_pc;
    private int catch_type;

    public CodeException(DataInputStream file) throws IOException {
        this(file.readUnsignedShort(), file.readUnsignedShort(),
                file.readUnsignedShort(), file.readUnsignedShort());
    }

    public CodeException(int start_pc, int end_pc, int handler_pc, int catch_type) {
        this.start_pc = start_pc;
        this.end_pc = end_pc;
        this.handler_pc = handler_pc;
        this.catch_type = catch_type;
    }


    public int getStartPc() {
        return start_pc;
    }

    public int getEndPc() {
        return end_pc;
    }

    public int getHandlerPc() {
        return handler_pc;
    }

    public int getCatchType() {
        return catch_type;
    }

    public final String toString(ConstantPool cp, boolean verbose) {
        String str;

        if (catch_type == 0) {
            str = "<Any exception>(0)";
        } else {
            str = Utility.compactClassName(cp.getConstantString(catch_type, Constants.CONSTANT_Class), false)
                    + (verbose ? "(" + catch_type + ")" : "");
        }

        return start_pc + "\t" + end_pc + "\t" + handler_pc + "\t" + str;
    }

    public String toString(ConstantPool cp) {
        return toString(cp, true);
    }
}