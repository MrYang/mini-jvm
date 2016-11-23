package com.zz.parser.attribute;


import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class PMGClass extends Attribute {

    private int pmg_class_index, pmg_index;

    PMGClass(int name_index, int length, DataInputStream file,
             ConstantPool constant_pool) throws IOException {
        super(Constants.ATTR_PMG, name_index, length, constant_pool);
        this.pmg_index = file.readUnsignedShort();
        this.pmg_class_index = file.readUnsignedShort();
    }
}
