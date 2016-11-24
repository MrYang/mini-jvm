package com.zz.parser.attribute;

import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class BootstrapMethods extends Attribute {

    private int num_bootstrap_methods;
    private BootstrapMethod[] bootstrap_methods;

    public BootstrapMethods(int name_index, int length, DataInputStream file,
                            ConstantPool constant_pool) throws IOException {
        super(Constants.ATTR_BOOTSTRAP_METHODS, name_index, length, constant_pool);

        num_bootstrap_methods = file.readUnsignedShort();
        bootstrap_methods = new BootstrapMethod[num_bootstrap_methods];
        for (int i = 0; i < num_bootstrap_methods; i++) {
            bootstrap_methods[i] = new BootstrapMethod(file);
        }
    }

}

class BootstrapMethod {

    private int bootstrap_method_ref;
    private int num_bootstrap_arguments;
    private int[] bootstrap_arguments;

    public BootstrapMethod(DataInputStream file) throws IOException {
        bootstrap_method_ref = file.readUnsignedShort();
        num_bootstrap_arguments = file.readUnsignedShort();
        bootstrap_arguments = new int[num_bootstrap_arguments];

        for (int i = 0; i < num_bootstrap_arguments; i++) {
            bootstrap_arguments[i] = file.readUnsignedShort();
        }
    }
}

