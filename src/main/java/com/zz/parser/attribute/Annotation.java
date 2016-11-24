package com.zz.parser.attribute;

import com.zz.parser.ClassParseException;
import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;
import com.zz.parser.Utility;

import java.io.DataInputStream;
import java.io.IOException;

public class Annotation {

    private int type_index;
    private int num_element_value_pairs;
    private Annotation.element_value_pair[] element_value_pairs;

    Annotation(DataInputStream file) throws IOException {
        this.type_index = file.readUnsignedShort();
        this.num_element_value_pairs = file.readUnsignedShort();
        this.element_value_pairs = new Annotation.element_value_pair[this.num_element_value_pairs];

        for (int i = 0; i < this.element_value_pairs.length; ++i) {
            this.element_value_pairs[i] = new Annotation.element_value_pair(file);
        }
    }

    public int length() {
        int length = 4;
        Annotation.element_value_pair[] element_value_pairs = this.element_value_pairs;

        for (Annotation.element_value_pair element_value_pair : element_value_pairs) {
            length += element_value_pair.length();
        }

        return length;
    }

    public String toString(ConstantPool constant_pool) {
        StringBuilder buf = new StringBuilder();
        String type = constant_pool.constantToString(type_index, Constants.CONSTANT_Utf8);
        buf.append("Annotation(").append(Utility.compactClassName(type)).append(")");
        for (Annotation.element_value_pair element_value_pair : this.element_value_pairs) {
            buf.append(element_value_pair.toString(constant_pool));
        }

        return buf.toString();
    }

    private static class element_value_pair {
        private final int element_name_index;
        final Annotation.element_value value;

        element_value_pair(DataInputStream file) throws IOException {
            this.element_name_index = file.readUnsignedShort();
            this.value = Annotation.element_value.read(file);
        }

        public int length() {
            return 2 + this.value.length();
        }

        public String toString(ConstantPool constant_pool) {
            StringBuilder buf = new StringBuilder();
            String name = constant_pool.constantToString(element_name_index, Constants.CONSTANT_Utf8);
            buf.append("pairs(").append(name).append("=").append(value.toString(constant_pool)).append(")");
            return buf.toString();
        }
    }

    private static class Array_element_value extends Annotation.element_value {
        final int num_values;
        final Annotation.element_value[] values;

        Array_element_value(DataInputStream file, int tag) throws IOException {
            super(tag);
            this.num_values = file.readUnsignedShort();
            this.values = new Annotation.element_value[this.num_values];

            for (int i = 0; i < this.values.length; ++i) {
                this.values[i] = Annotation.element_value.read(file);
            }

        }

        public int length() {
            int length = 2;

            for (element_value value : this.values) {
                length += value.length();
            }

            return length;
        }

        public String toString(ConstantPool constant_pool) {
            StringBuilder buf = new StringBuilder();
            for (element_value value : this.values) {
                buf.append(value.toString(constant_pool)).append(",");
            }
            return buf.toString();
        }

    }

    private static class Annotation_element_value extends Annotation.element_value {
        final Annotation annotation_value;

        Annotation_element_value(DataInputStream file, int tag) throws IOException {
            super(tag);
            this.annotation_value = new Annotation(file);
        }

        public int length() {
            return this.annotation_value.length();
        }

        public String toString(ConstantPool constant_pool) {
            return annotation_value.toString();
        }
    }

    private static class Class_element_value extends Annotation.element_value {
        final int class_info_index;

        Class_element_value(DataInputStream file, int tag) throws IOException {
            super(tag);
            this.class_info_index = file.readUnsignedShort();
        }

        public int length() {
            return 2;
        }

        public String toString(ConstantPool constant_pool) {
            String className = constant_pool.constantToString(class_info_index, Constants.CONSTANT_Utf8);
            return Utility.compactClassName(className);
        }

    }

    private static class Enum_element_value extends Annotation.element_value {
        final int type_name_index;
        final int const_name_index;

        Enum_element_value(DataInputStream file, int tag) throws IOException {
            super(tag);
            this.type_name_index = file.readUnsignedShort();
            this.const_name_index = file.readUnsignedShort();
        }

        public int length() {
            return 4;
        }


        public String toString(ConstantPool constant_pool) {
            String type = constant_pool.constantToString(type_name_index, Constants.CONSTANT_Utf8);
            String value = constant_pool.constantToString(const_name_index, Constants.CONSTANT_Utf8);
            return "enum:type(" + type + "),value(" + value + ")";
        }
    }

    private static class Primitive_element_value extends Annotation.element_value {
        final int const_value_index;

        Primitive_element_value(DataInputStream file, int tag) throws IOException {
            super(tag);
            this.const_value_index = file.readUnsignedShort();
        }

        public int length() {
            return 2;
        }

        public String toString(ConstantPool constant_pool) {
            return constant_pool.constantToString(const_value_index, Constants.CONSTANT_Utf8);
        }
    }

    abstract static class element_value {
        final int tag;

        static Annotation.element_value read(DataInputStream file) throws IOException {
            int tag = file.readUnsignedByte();
            switch (tag) {
                case '@':    // '@'
                    return new Annotation.Annotation_element_value(file, tag);
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                case 's':
                    return new Annotation.Primitive_element_value(file, tag);
                case '[':  // [
                    return new Annotation.Array_element_value(file, tag);
                case 'c':  // c
                    return new Annotation.Class_element_value(file, tag);
                case 'e': // e
                    return new Annotation.Enum_element_value(file, tag);
                default:
                    throw new ClassParseException("unrecognized tag: " + tag);
            }
        }

        element_value(int tag) {
            this.tag = tag;
        }

        public abstract int length();

        public abstract String toString(ConstantPool constant_pool);
    }
}