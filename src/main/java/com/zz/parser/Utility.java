package com.zz.parser;


import com.zz.parser.attribute.LocalVariable;
import com.zz.parser.attribute.LocalVariableTable;

import java.io.IOException;

public class Utility {

    /* How many chars have been consumed during parsing in signatureToString().
     * Read by methodSignatureToString(). Set by side effect,but only internally.
     */
    private static int consumed_chars;

    /* The `WIDE' instruction is used in the byte code to allow 16-bit wide indices
     * for local variables. This opcode precedes an `ILOAD', e.g.. The opcode
     * immediately following takes an extra byte which is combined with the
     * following byte to form a 16-bit value.
     */
    private static boolean wide = false;

    public static String accessToString(int access_flags) {
        return accessToString(access_flags, false);
    }

    public static String accessToString(int access_flags, boolean is_class) {
        StringBuilder buf = new StringBuilder();

        int p = 0;
        for (int i = 0; p < Constants.MAX_ACC_FLAG; i++) {
            p = pow2(i);

            if ((access_flags & p) != 0) {
                /* Special case: Classes compiled with new compilers and with the
                 * `ACC_SUPER' flag would be said to be "synchronized". This is
                 * because SUN used the same value for the flags `ACC_SUPER' and
                 * `ACC_SYNCHRONIZED'.
                 */
                if (is_class && ((p == Constants.ACC_SUPER) || (p == Constants.ACC_INTERFACE))) {
                    continue;
                }

                buf.append(Constants.ACCESS_NAMES[i]).append(" ");
            }
        }

        return buf.toString().trim();
    }

    /**
     * @param code          byte code array
     * @param constant_pool constant_pool
     * @param index         code array 索引 <EM>(number of opcodes, not bytes!)</EM>
     * @param length        number of opcodes to decompile, -1 for all
     * @param verbose       be verbose, e.g. print constant pool index
     * @return String representation of byte codes
     */
    public static String codeToString(byte[] code, ConstantPool constant_pool,
                                      int index, int length, boolean verbose){

        StringBuilder buf = new StringBuilder(code.length * 20); // Should be sufficient
        ByteSequence stream = new ByteSequence(code);

        try {
            for (int i = 0; i < index; i++) {
                // Skip `index' lines of code
                codeToString(stream, constant_pool, verbose);
            }

            for (int i = 0; stream.available() > 0; i++) {
                if ((length < 0) || (i < length)) {
                    String indices = fillup(stream.getIndex() + ":", 6, true, ' ');
                    buf.append(indices + codeToString(stream, constant_pool, verbose) + '\n');
                }
            }
        } catch (IOException e) {
            System.out.println(buf.toString());
            e.printStackTrace();
            throw new ClassParseException("Byte code error: " + e);
        }

        return buf.toString();
    }

    public static String codeToString(ByteSequence bytes, ConstantPool constant_pool, boolean verbose) throws IOException {
        short opcode = (short) bytes.readUnsignedByte();
        int default_offset = 0;
        int no_pad_bytes = 0;

        int low, high, npairs;
        int index, vindex, constant;
        int offset;
        int[] match, jump_table;

        StringBuilder buf = new StringBuilder(Constants.OPCODE_NAMES[opcode]);

        /* Special case: Skip (0-3) padding bytes, i.e., the
         * following bytes are 4-byte-aligned
         */
        if ((opcode == Constants.TABLESWITCH) || (opcode == Constants.LOOKUPSWITCH)) {
            int remainder = bytes.getIndex() % 4;
            no_pad_bytes = (remainder == 0) ? 0 : 4 - remainder;

            for (int i = 0; i < no_pad_bytes; i++) {
                byte b;

                if ((b = bytes.readByte()) != 0) {
                    System.err.println("Warning: Padding byte != 0 in " + Constants.OPCODE_NAMES[opcode] + ":" + b);
                }
            }

            // Both cases have a field default_offset in common
            default_offset = bytes.readInt();
        }

        switch (opcode) {
            /*Table switch has variable length arguments.
            */
            case Constants.TABLESWITCH:
                low = bytes.readInt();
                high = bytes.readInt();

                offset = bytes.getIndex() - 12 - no_pad_bytes - 1;
                default_offset += offset;

                buf.append("\tdefault = " + default_offset + ", low = " + low + ", high = " + high + "(");

                jump_table = new int[high - low + 1];
                for (int i = 0; i < jump_table.length; i++) {
                    jump_table[i] = offset + bytes.readInt();
                    buf.append(jump_table[i]);

                    if (i < jump_table.length - 1)
                        buf.append(", ");
                }
                buf.append(")");
                break;

            // Lookup switch has variable length arguments
            case Constants.LOOKUPSWITCH:
                npairs = bytes.readInt();
                offset = bytes.getIndex() - 8 - no_pad_bytes - 1;

                match = new int[npairs];
                jump_table = new int[npairs];
                default_offset += offset;

                buf.append("\tdefault = " + default_offset + ", npairs = " + npairs + " (");

                for (int i = 0; i < npairs; i++) {
                    match[i] = bytes.readInt();
                    jump_table[i] = offset + bytes.readInt();
                    buf.append("(" + match[i] + ", " + jump_table[i] + ")");

                    if (i < npairs - 1) {
                        buf.append(", ");
                    }
                }

                buf.append(")");
                break;

            /* Two address bytes + offset from start of byte stream form the
             * jump target
             */
            case Constants.GOTO:
            case Constants.IFEQ:
            case Constants.IFGE:
            case Constants.IFGT:
            case Constants.IFLE:
            case Constants.IFLT:
            case Constants.JSR:
            case Constants.IFNE:
            case Constants.IFNONNULL:
            case Constants.IFNULL:
            case Constants.IF_ACMPEQ:
            case Constants.IF_ACMPNE:
            case Constants.IF_ICMPEQ:
            case Constants.IF_ICMPGE:
            case Constants.IF_ICMPGT:
            case Constants.IF_ICMPLE:
            case Constants.IF_ICMPLT:
            case Constants.IF_ICMPNE:
                buf.append("\t\t#" + ((bytes.getIndex() - 1) + bytes.readShort()));
                break;

            /* 32-bit wide jumps
            */
            case Constants.GOTO_W:
            case Constants.JSR_W:
                buf.append("\t\t#" + ((bytes.getIndex() - 1) + bytes.readInt()));
                break;

            /* Index byte references local variable (register)
            */
            case Constants.ALOAD:
            case Constants.ASTORE:
            case Constants.DLOAD:
            case Constants.DSTORE:
            case Constants.FLOAD:
            case Constants.FSTORE:
            case Constants.ILOAD:
            case Constants.ISTORE:
            case Constants.LLOAD:
            case Constants.LSTORE:
            case Constants.RET:
                if (wide) {
                    vindex = bytes.readUnsignedShort();
                    wide = false; // Clear flag
                } else {
                    vindex = bytes.readUnsignedByte();
                }

                buf.append("\t\t%" + vindex);
                break;

            /*
            * Remember wide byte which is used to form a 16-bit address in the
            * following instruction. Relies on that the method is called again with
            * the following opcode.
            */
            case Constants.WIDE:
                wide = true;
                buf.append("\t(wide)");
                break;

            /* Array of basic type.
            */
            case Constants.NEWARRAY:
                buf.append("\t\t<" + Constants.TYPE_NAMES[bytes.readByte()] + ">");
                break;

            /* Access object/class fields.
            */
            case Constants.GETFIELD:
            case Constants.GETSTATIC:
            case Constants.PUTFIELD:
            case Constants.PUTSTATIC:
                index = bytes.readUnsignedShort();
                buf.append("\t\t" + constant_pool.constantToString(index, Constants.CONSTANT_Fieldref) + (verbose ? " (" + index + ")" : ""));
                break;

            /* Operands are references to classes in constant pool
            */
            case Constants.NEW:
            case Constants.CHECKCAST:
                buf.append("\t");
            case Constants.INSTANCEOF:
                index = bytes.readUnsignedShort();
                buf.append("\t<" + constant_pool.constantToString(index, Constants.CONSTANT_Class) + ">" + (verbose ? " (" + index + ")" : ""));
                break;

            /* Operands are references to methods in constant pool
            */
            case Constants.INVOKESPECIAL:
            case Constants.INVOKESTATIC:
            case Constants.INVOKEVIRTUAL:
                index = bytes.readUnsignedShort();
                buf.append("\t" + constant_pool.constantToString(index, Constants.CONSTANT_Methodref) + (verbose ? " (" + index + ")" : ""));
                break;

            case Constants.INVOKEINTERFACE:
                index = bytes.readUnsignedShort();
                int nargs = bytes.readUnsignedByte(); // historical, redundant
                buf.append("\t" + constant_pool.constantToString(index, Constants.CONSTANT_InterfaceMethodref) +
                        (verbose ? " (" + index + ")\t" : "") + nargs + "\t" + bytes.readUnsignedByte()); // Last byte is a reserved space
                break;

            /* Operands are references to items in constant pool
            */
            case Constants.LDC_W:
            case Constants.LDC2_W:
                index = bytes.readUnsignedShort();

                buf.append("\t\t" + constant_pool.constantToString(index, constant_pool.getConstant(index).getTag()) +
                        (verbose ? " (" + index + ")" : ""));
                break;

            case Constants.LDC:
                index = bytes.readUnsignedByte();

                buf.append("\t\t" + constant_pool.constantToString(index, constant_pool.getConstant(index).getTag()) +
                        (verbose ? " (" + index + ")" : ""));
                break;

            /* Array of references.
            */
            case Constants.ANEWARRAY:
                index = bytes.readUnsignedShort();

                buf.append("\t\t<" + compactClassName(constant_pool.getConstantString(index, Constants.CONSTANT_Class), false) +
                        ">" + (verbose ? " (" + index + ")" : ""));
                break;

            /* Multidimensional array of references.
            */
            case Constants.MULTIANEWARRAY: {
                index = bytes.readUnsignedShort();
                int dimensions = bytes.readUnsignedByte();

                buf.append("\t<" + compactClassName(constant_pool.getConstantString(index, Constants.CONSTANT_Class), false) +
                        ">\t" + dimensions + (verbose ? " (" + index + ")" : ""));
            }
            break;

            /* Increment local variable.
            */
            case Constants.IINC:
                if (wide) {
                    vindex = bytes.readUnsignedShort();
                    constant = bytes.readShort();
                    wide = false;
                } else {
                    vindex = bytes.readUnsignedByte();
                    constant = bytes.readByte();
                }
                buf.append("\t\t%" + vindex + "\t" + constant);
                break;

            default:
                if (Constants.NO_OF_OPERANDS[opcode] > 0) {
                    for (int i = 0; i < Constants.TYPE_OF_OPERANDS[opcode].length; i++) {
                        buf.append("\t\t");
                        switch (Constants.TYPE_OF_OPERANDS[opcode][i]) {
                            case Constants.T_BYTE:
                                buf.append(bytes.readByte());
                                break;
                            case Constants.T_SHORT:
                                buf.append(bytes.readShort());
                                break;
                            case Constants.T_INT:
                                buf.append(bytes.readInt());
                                break;

                            default: // Never reached
                                System.err.println("Unreachable default case reached!");
                                buf.setLength(0);
                        }
                    }
                }
        }

        return buf.toString();
    }

    /**
     * Shorten long class names, <em>java/lang/String</em> becomes
     * <em>String</em>.
     *
     * @param str The long class name
     * @return Compacted class name
     */
    public static String compactClassName(String str) {
        return compactClassName(str, true);
    }

    /**
     * Shorten long class names, <em>java/lang/String</em> becomes
     * <em>java.lang.String</em>,
     * e.g.. If <em>chopit</em> is <em>true</em> the prefix <em>java.lang</em>
     * is also removed.
     *
     * @param str    The long class name
     * @param chopit Flag that determines whether chopping is executed or not
     * @return Compacted class name
     */
    public static String compactClassName(String str, boolean chopit) {
        return compactClassName(str, "java.lang.", chopit);
    }

    /**
     * Shorten long class name <em>str</em>, i.e., chop off the <em>prefix</em>,
     * if the
     * class name starts with this string and the flag <em>chopit</em> is true.
     * Slashes <em>/</em> are converted to dots <em>.</em>.
     *
     * @param str    The long class name
     * @param prefix The prefix the get rid off
     * @param chopit Flag that determines whether chopping is executed or not
     * @return Compacted class name
     */
    public static String compactClassName(String str, String prefix, boolean chopit) {
        int len = prefix.length();

        str = str.replace('/', '.'); // Is `/' on all systems, even DOS

        if (chopit) {
            // If string starts with `prefix' and contains no further dots
            if (str.startsWith(prefix) && (str.substring(len).indexOf('.') == -1)) {
                str = str.substring(len);
            }
        }

        return str;
    }

    public static String fillup(String str, int length, boolean left_justify, char fill) {
        int len = length - str.length();
        char[] buf = new char[(len < 0) ? 0 : len];

        for (int j = 0; j < buf.length; j++) {
            buf[j] = fill;
        }

        if (left_justify) {
            return str + new String(buf);
        }

        return new String(buf) + str;
    }

    /**
     * Converts method signature to string with all class names compacted.
     *
     * @param signature to convert
     * @param name      of method
     * @param access    flags of method
     * @return Human readable signature
     */
    public static final String methodSignatureToString(String signature, String name, String access) {
        return methodSignatureToString(signature, name, access, true);
    }

    public static final String methodSignatureToString(String signature, String name, String access, boolean chopit) {
        return methodSignatureToString(signature, name, access, chopit, null);
    }

    /**
     * A return type signature represents the return value from a method.
     * It is a series of bytes in the following grammar:
     * <p>
     * <return_signature> ::= <field_type> | V
     * <p>
     * The character V indicates that the method returns no value. Otherwise, the
     * signature indicates the type of the return value.
     * An argument signature represents an argument passed to a method:
     * <p>
     * <argument_signature> ::= <field_type>
     * <p>
     * A method signature represents the arguments that the method expects, and
     * the value that it returns.
     * <method_signature> ::= (<arguments_signature>) <return_signature>
     * <arguments_signature>::= <argument_signature>*
     * <p>
     * This method converts such a string into a Java type declaration like
     * `void _main(String[])' and throws a `ClassFormatException' when the parsed
     * type is invalid.
     *
     * @param signature Method signature
     * @param name      Method name
     * @param access    Method access rights
     * @return Java type declaration
     * @throws ClassParseException
     */
    public static final String methodSignatureToString(String signature, String name, String access, boolean chopit, LocalVariableTable vars)
            throws ClassParseException {
        StringBuffer buf = new StringBuffer("(");
        String type;
        int var_index = (access.contains("static")) ? 0 : 1;

        try { // Read all declarations between for `(' and `)'
            if (signature.charAt(0) != '(') {
                throw new ClassParseException("Invalid method signature: " + signature);
            }

            int index = 1; // current string position

            while (signature.charAt(index) != ')') {
                String param_type = signatureToString(signature.substring(index), chopit);
                buf.append(param_type);

                if (vars != null) {
                    LocalVariable l = vars.getLocalVariable(var_index);

                    if (l != null) {
                        buf.append(" " + l.getName());
                    }
                } else {
                    buf.append(" arg" + var_index);
                }

                if ("double".equals(param_type) || "long".equals(param_type)) {
                    var_index += 2;
                } else {
                    var_index++;
                }

                buf.append(", ");
                index += consumed_chars; // update position
            }

            index++; // update position

            // Read return type after `)'
            type = signatureToString(signature.substring(index), chopit);

        } catch (StringIndexOutOfBoundsException e) { // Should never occur
            throw new ClassParseException("Invalid method signature: " + signature);
        }

        if (buf.length() > 1){
            // Tack off the extra ", "
            buf.setLength(buf.length() - 2);
        }

        buf.append(")");

        return access + ((access.length() > 0) ? " " : "") + type + " " + name + buf.toString();
    }

    /**
     * Converts signature to string with all class names compacted.
     *
     * @param signature to convert
     * @return Human readable signature
     */
    public static final String signatureToString(String signature) {
        return signatureToString(signature, true);
    }

    /**
     * The field signature represents the value of an argument to a function or
     * the value of a variable. It is a series of bytes generated by the
     * following grammar:
     * <p>
     * <PRE>
     * <field_signature> ::= <field_type>
     * <field_type>      ::= <base_type>|<object_type>|<array_type>
     * <base_type>       ::= B|C|D|F|I|J|S|Z
     * <object_type>     ::= L<fullclassname>;
     * <array_type>      ::= [<field_type>
     * <p>
     * The meaning of the base types is as follows:
     * B byte signed byte
     * C char character
     * D double double precision IEEE float
     * F float single precision IEEE float
     * I int integer
     * J long long integer
     * L<fullclassname>; ... an object of the given class
     * S short signed short
     * Z boolean true or false
     * [<field sig> ... array
     * </PRE>
     * <p>
     * This method converts this string into a Java type declaration such as
     * `String[]' and throws a `ClassFormatException' when the parsed type is
     * invalid.
     *
     * @param signature Class signature
     * @param chopit    Flag that determines whether chopping is executed or not
     * @return Java type declaration
     * @throws ClassParseException
     */
    public static final String signatureToString(String signature, boolean chopit) {
        consumed_chars = 1; // This is the default, read just one char like `B'

        try {
            switch (signature.charAt(0)) {
                case 'B':
                    return "byte";
                case 'C':
                    return "char";
                case 'D':
                    return "double";
                case 'F':
                    return "float";
                case 'I':
                    return "int";
                case 'J':
                    return "long";

                case 'L': { // Full class name
                    int index = signature.indexOf(';'); // Look for closing `;'

                    if (index < 0) {
                        throw new ClassParseException("Invalid signature: " + signature);
                    }

                    consumed_chars = index + 1; // "L blabla;" `L' and `;' are removed

                    return compactClassName(signature.substring(1, index), chopit);
                }

                case 'S':
                    return "short";
                case 'Z':
                    return "boolean";

                case '[': { // Array declaration
                    StringBuffer brackets = new StringBuffer(); // Accumulate []'s

                    // Count opening brackets and look for optional size argument
                    int n;
                    for (n = 0; signature.charAt(n) == '['; n++) {
                        brackets.append("[]");
                    }

                    int consumed_chars = n; // Remember value

                    // The rest of the string denotes a `<field_type>'
                    String type = signatureToString(signature.substring(n), chopit);

                    Utility.consumed_chars += consumed_chars;
                    return type + brackets.toString();
                }

                case 'V':
                    return "void";

                default:
                    throw new ClassParseException("Invalid signature: `" + signature + "'");
            }
        } catch (StringIndexOutOfBoundsException e) { // Should never occur
            throw new ClassParseException("Invalid signature: " + e + ":" + signature);
        }
    }

    // 向左移一位
    private static int pow2(int n) {
        return 1 << n;
    }
}
