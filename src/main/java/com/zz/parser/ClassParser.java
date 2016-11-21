package com.zz.parser;

import com.zz.parser.attribute.Attribute;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class ClassParser {

    private DataInputStream file;
    private String file_name;

    private int class_name_index, superclass_name_index;    // 指向常量池的索引
    private int major, minor;
    private int access_flags;
    private int[] interfaces;
    private ConstantPool constant_pool;
    private Field[] fields;
    private Method[] methods;
    private Attribute[] attributes;

    private static final int BUFF_SIZE = 8192;

    public ClassParser(String file_name) throws IOException {
        this.file_name = file_name;
        file = new DataInputStream(new BufferedInputStream(new FileInputStream(file_name), BUFF_SIZE));
    }

    public JavaClass parse() throws IOException {
        readID();
        readVersion();
        readConstantPool();
        readClassInfo();
        readInterfaces();
        readFields();
        readMethods();
        readAttributes();

        file.close();

        return new JavaClass(class_name_index, superclass_name_index,
                file_name, major, minor, access_flags, constant_pool,
                interfaces, fields, methods, attributes);
    }

    private void readID() throws IOException {
        int magic = 0xCAFEBABE;
        if (file.readInt() != magic) {
            throw new RuntimeException("not class file");
        }
    }

    private void readVersion() throws IOException {
        minor = file.readUnsignedShort();
        major = file.readUnsignedShort();
    }

    private void readConstantPool() throws IOException {
        constant_pool = new ConstantPool(file);
    }

    private void readClassInfo() throws IOException {
        access_flags = file.readUnsignedShort();

        // 如果是接口的话必须添加抽象类型
        if ((access_flags & Constants.ACC_INTERFACE) != 0) {
            access_flags |= Constants.ACC_ABSTRACT;
        }

        if (((access_flags & Constants.ACC_ABSTRACT) != 0) &&
                ((access_flags & Constants.ACC_FINAL) != 0)) {
            throw new RuntimeException("Class can't be both final and abstract");
        }

        class_name_index = file.readUnsignedShort();
        superclass_name_index = file.readUnsignedShort();
    }

    private void readInterfaces() throws IOException {
        int interfaces_count = file.readUnsignedShort();
        interfaces = new int[interfaces_count];
        for (int i = 0; i < interfaces_count; i++) {
            interfaces[i] = file.readUnsignedShort();
        }
    }

    private void readFields() throws IOException {
        int field_count = file.readUnsignedShort();
        fields = new Field[field_count];
        for (int i = 0; i < field_count; i++) {
            fields[i] = new Field(file, constant_pool);
        }
    }

    private void readMethods() throws IOException {
        int method_count = file.readUnsignedShort();
        methods = new Method[method_count];
        for (int i = 0; i < method_count; i++) {
            methods[i] = new Method(file, constant_pool);
        }
    }

    private void readAttributes() throws IOException {
        int attribute_count = file.readUnsignedShort();
        attributes = new Attribute[attribute_count];
        for (int i = 0; i < attribute_count; i++) {
            attributes[i] = Attribute.readAttribute(file, constant_pool);
        }
    }

}
