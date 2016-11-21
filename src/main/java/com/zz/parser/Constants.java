package com.zz.parser;

public class Constants {

    // 常量池
    public final static byte CONSTANT_Utf8               = 1;
    public final static byte CONSTANT_Integer            = 3;
    public final static byte CONSTANT_Float              = 4;
    public final static byte CONSTANT_Long               = 5;
    public final static byte CONSTANT_Double             = 6;
    public final static byte CONSTANT_Class              = 7;
    public final static byte CONSTANT_Fieldref           = 9;
    public final static byte CONSTANT_String             = 8;
    public final static byte CONSTANT_Methodref          = 10;
    public final static byte CONSTANT_InterfaceMethodref = 11;
    public final static byte CONSTANT_NameAndType        = 12;
    public final static byte CONSTANT_MethodHandle_info  = 15;
    public final static byte CONSTANT_MethodType_info    = 16;
    public final static byte CONSTANT_InvokeDynamic_info = 18;


    // 访问标志
    public final static short ACC_PUBLIC       = 0x0001;
    public final static short ACC_PRIVATE      = 0x0002;
    public final static short ACC_PROTECTED    = 0x0004;
    public final static short ACC_STATIC       = 0x0008;

    public final static short ACC_FINAL        = 0x0010;
    public final static short ACC_SYNCHRONIZED = 0x0020;
    public final static short ACC_VOLATILE     = 0x0040;
    public final static short ACC_TRANSIENT    = 0x0080;

    public final static short ACC_NATIVE       = 0x0100;
    public final static short ACC_INTERFACE    = 0x0200;
    public final static short ACC_ABSTRACT     = 0x0400;
    public final static short ACC_STRICT       = 0x0800;


    // 属性表
    public static final byte ATTR_UNKNOWN                                 = -1;
    public static final byte ATTR_SOURCE_FILE                             = 0;
    public static final byte ATTR_CONSTANT_VALUE                          = 1;
    public static final byte ATTR_CODE                                    = 2;
    public static final byte ATTR_EXCEPTIONS                              = 3;
    public static final byte ATTR_LINE_NUMBER_TABLE                       = 4;
    public static final byte ATTR_LOCAL_VARIABLE_TABLE                    = 5;
    public static final byte ATTR_INNER_CLASSES                           = 6;
    public static final byte ATTR_SYNTHETIC                               = 7;
    public static final byte ATTR_DEPRECATED                              = 8;
    public static final byte ATTR_PMG                                     = 9;
    public static final byte ATTR_SIGNATURE                               = 10;
    public static final byte ATTR_STACK_MAP                               = 11;
    public static final byte ATTR_LOCAL_VARIABLE_TYPE_TABLE               = 12;

    public static final short KNOWN_ATTRIBUTES = 13;

    public static final String[] ATTRIBUTE_NAMES = {
            "SourceFile", "ConstantValue", "Code", "Exceptions",
            "LineNumberTable", "LocalVariableTable",
            "InnerClasses", "Synthetic", "Deprecated",
            "PMGClass", "Signature", "StackMap",
            "LocalVariableTypeTable"
    };

    /** Constants used in the StackMap attribute.
     */
    public static final byte ITEM_Bogus      = 0;
    public static final byte ITEM_Integer    = 1;
    public static final byte ITEM_Float      = 2;
    public static final byte ITEM_Double     = 3;
    public static final byte ITEM_Long       = 4;
    public static final byte ITEM_Null       = 5;
    public static final byte ITEM_InitObject = 6;
    public static final byte ITEM_Object     = 7;
    public static final byte ITEM_NewObject  = 8;

    public static final String[] ITEM_NAMES = {
            "Bogus", "Integer", "Float", "Double", "Long",
            "Null", "InitObject", "Object", "NewObject"
    };
}
