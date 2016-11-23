package com.zz.parser.attribute;

import com.zz.parser.ClassParseException;
import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;
import com.zz.parser.Utility;

import java.io.DataInputStream;
import java.io.IOException;

public class RuntimeVisibleAnnotations extends Attribute {

    private int num_annoations;
    private Annotation[] annoations;

    RuntimeVisibleAnnotations(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
        super(Constants.ATTR_RUNTIME_VISIBLE_ANNOTATIONS, name_index, length, constant_pool);
        num_annoations = file.readUnsignedShort();
        annoations = new Annotation[num_annoations];
        for (int i = 0; i < num_annoations; i++) {
            annoations[i] = new Annotation(file);
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < this.annoations.length; ++i) {
            buf.append(annoations[i].toString(constant_pool)).append(",");
        }
        return buf.toString();
    }
}

class Annotation {
    public final int type_index;
    public final int num_element_value_pairs;
    public final Annotation.element_value_pair[] element_value_pairs;

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
        int pairs_length = element_value_pairs.length;

        for (int i = 0; i < pairs_length; ++i) {
            Annotation.element_value_pair element_value_pair = element_value_pairs[i];
            length += element_value_pair.length();
        }

        return length;
    }

    public String toString(ConstantPool constant_pool) {
        StringBuilder buf = new StringBuilder();
        String type = constant_pool.constantToString(type_index, Constants.CONSTANT_Utf8);
        buf.append("Annotation(").append(Utility.compactClassName(type)).append(")");
        for (int i = 0; i < this.element_value_pairs.length; ++i) {
            buf.append(element_value_pairs[i].toString(constant_pool));
        }

        return buf.toString();
    }

    public static class element_value_pair {
        public final int element_name_index;
        public final Annotation.element_value value;

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

    public static class Array_element_value extends Annotation.element_value {
        public final int num_values;
        public final Annotation.element_value[] values;

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

            for (int i = 0; i < this.values.length; ++i) {
                length += this.values[i].length();
            }

            return length;
        }

        public String toString(ConstantPool constant_pool) {
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < this.values.length; ++i) {
                buf.append(values[i].toString(constant_pool)).append(",");
            }
            return buf.toString();
        }

    }

    public static class Annotation_element_value extends Annotation.element_value {
        public final Annotation annotation_value;

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

    public static class Class_element_value extends Annotation.element_value {
        public final int class_info_index;

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

    public static class Enum_element_value extends Annotation.element_value {
        public final int type_name_index;
        public final int const_name_index;

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

    public static class Primitive_element_value extends Annotation.element_value {
        public final int const_value_index;

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

    public abstract static class element_value {
        public final int tag;

        public static Annotation.element_value read(DataInputStream file) throws IOException {
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

        protected element_value(int tag) {
            this.tag = tag;
        }

        public abstract int length();

        public abstract String toString(ConstantPool constant_pool);
    }
}

