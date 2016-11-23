package com.zz.parser.attribute;


import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class LineNumberTable extends Attribute {

    private int line_number_table_length;
    private LineNumber[] line_number_table;

    public LineNumberTable(int name_index, int length,
                           ConstantPool constant_pool) {
        super(Constants.ATTR_LINE_NUMBER_TABLE, name_index, length, constant_pool);
    }

    LineNumberTable(int name_index, int length, DataInputStream file,
                    ConstantPool constant_pool) throws IOException {
        this(name_index, length, constant_pool);
        line_number_table_length = (file.readUnsignedShort());
        line_number_table = new LineNumber[line_number_table_length];

        for (int i = 0; i < line_number_table_length; i++) {
            line_number_table[i] = new LineNumber(file);
        }
    }


    public final String toString() {
        StringBuilder buf = new StringBuilder();
        StringBuilder line = new StringBuilder();

        for (int i = 0; i < line_number_table_length; i++) {
            line.append(line_number_table[i].toString());

            if (i < line_number_table_length - 1) {
                line.append(", ");
            }

            if (line.length() > 72) {
                line.append('\n');
                buf.append(line);
                line.setLength(0);
            }
        }

        buf.append(line);

        return buf.toString();
    }
}

class LineNumber {
    private int start_pc;
    private int line_number;

    LineNumber(DataInputStream file) throws IOException {
        this(file.readUnsignedShort(), file.readUnsignedShort());
    }

    public LineNumber(int start_pc, int line_number) {
        this.start_pc = start_pc;
        this.line_number = line_number;
    }

    public final String toString() {
        return "LineNumber(" + line_number + ": " + start_pc + ")";
    }
}