package com.zz.parser;


import com.zz.parser.attribute.Attribute;
import com.zz.parser.attribute.SourceFile;

import java.util.StringTokenizer;

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

        class_name = constant_pool.constantToString(class_name_index, Constants.CONSTANT_Class);
        class_name = Utility.compactClassName(class_name, false);

        int index = class_name.lastIndexOf('.');
        if (index < 0) {
            package_name = "";
        } else {
            package_name = class_name.substring(0, index);
        }

        if (superclass_name_index > 0) { // May be zero -> class is java.lang.Object
            superclass_name = constant_pool.getConstantString(superclass_name_index, Constants.CONSTANT_Class);
            superclass_name = Utility.compactClassName(superclass_name, false);
        } else {
            superclass_name = "java.lang.Object";
        }

        interface_names = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            String str = constant_pool.getConstantString(interfaces[i], Constants.CONSTANT_Class);
            interface_names[i] = Utility.compactClassName(str, false);
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

    public String toString() {
        String access = Utility.accessToString(access_flags, true);
        access = access.equals("") ? "" : (access + " ");

        StringBuilder buf = new StringBuilder(access +
                Utility.classOrInterface(access_flags) +
                " " +
                class_name + " extends " +
                Utility.compactClassName(superclass_name, false) + '\n');

        int size = interfaces.length;

        if (size > 0) {
            buf.append("implements\t\t");

            for (int i = 0; i < size; i++) {
                buf.append(interface_names[i]);
                if (i < size - 1)
                    buf.append(", ");
            }

            buf.append('\n');
        }

        buf.append("filename\t\t" + file_name + '\n');
        buf.append("compiled from\t\t" + source_file_name + '\n');
        buf.append("compiler version\t" + major + "." + minor + '\n');
        buf.append("access flags\t\t" + access_flags + '\n');
        buf.append("constant pool\t\t" + constant_pool.getLength() + " entries\n");
        buf.append("ACC_SUPER flag\t\t" + isSuper() + "\n");

        if (attributes.length > 0) {
            buf.append("\nAttribute(s):\n");
            for (int i = 0; i < attributes.length; i++)
                buf.append(indent(attributes[i]));
        }

        if (fields.length > 0) {
            buf.append("\n" + fields.length + " fields:\n");
            for (int i = 0; i < fields.length; i++)
                buf.append("\t" + fields[i] + '\n');
        }

        if (methods.length > 0) {
            buf.append("\n" + methods.length + " methods:\n");
            for (int i = 0; i < methods.length; i++)
                buf.append("\t" + methods[i] + '\n');
        }

        return buf.toString();
    }

    public boolean isSuper() {
        return (access_flags & Constants.ACC_SUPER) != 0;
    }

    private static String indent(Object obj) {
        StringTokenizer tok = new StringTokenizer(obj.toString(), "\n");
        StringBuffer buf = new StringBuffer();

        while (tok.hasMoreTokens()) {
            buf.append("\t" + tok.nextToken() + "\n");
        }

        return buf.toString();
    }
}
