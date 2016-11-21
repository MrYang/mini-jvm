package com.zz.parser;


import com.zz.parser.attribute.Attribute;
import com.zz.parser.attribute.SourceFile;

public class JavaClass extends AccessFlags {

    private int major, minor;
    private String file_name;
    private String package_name;
    private String source_file_name = "<Unknown>";
    private int class_name_index;
    private int superclass_name_index;
    private String class_name;
    private String superclass_name;
    private ConstantPool constant_pool;
    private int[] interfaces;
    private String[] interface_names;
    private Field[] fields;
    private Method[] methods;
    private Attribute[] attributes;

    public JavaClass(int class_name_index,
                     int superclass_name_index,
                     String file_name,
                     int major, int minor, int access_flags,
                     ConstantPool constant_pool, int[] interfaces,
                     Field[] fields,
                     Method[] methods,
                     Attribute[] attributes) {
        if (interfaces == null) {
            interfaces = new int[0];
        }
        if (attributes == null) {
            attributes = new Attribute[0];
        }
        if (fields == null) {
            fields = new Field[0];
        }
        if (methods == null) {
            methods = new Method[0];
        }

        this.class_name_index = class_name_index;
        this.superclass_name_index = superclass_name_index;
        this.file_name = file_name;
        this.major = major;
        this.minor = minor;
        this.access_flags = access_flags;
        this.constant_pool = constant_pool;
        this.interfaces = interfaces;
        this.fields = fields;
        this.methods = methods;
        this.attributes = attributes;

        for (Attribute attribute : attributes) {
            if (attribute instanceof SourceFile) {
                source_file_name = ((SourceFile) attribute).getSourceFileName();
                break;
            }
        }
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public String getFileName() {
        return file_name;
    }

    public String getPackageName() {
        return package_name;
    }

    public String getSourceFileName() {
        return source_file_name;
    }

    public int getClassNameIndex() {
        return class_name_index;
    }

    public int getSuperclassNameIndex() {
        return superclass_name_index;
    }

    public String getClassName() {
        return class_name;
    }

    public String getSuperclassName() {
        return superclass_name;
    }

    public ConstantPool getConstant_pool() {
        return constant_pool;
    }

    public int[] getInterfaces() {
        return interfaces;
    }

    public String[] getInterfaceNames() {
        return interface_names;
    }

    public Field[] getFields() {
        return fields;
    }

    public Method[] getMethods() {
        return methods;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }
}
